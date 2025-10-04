package kr.kro.chzzk.minecraft.infrastructure.chzzk

import kr.kro.chzzk.minecraft.application.dto.ChzzkProfile
import kr.kro.chzzk.minecraft.domain.exception.LinkageException
import java.security.SecureRandom
import java.util.*

class ChzzkAuthenticator(
    private val chzzkApiClient: ChzzkApiClient
) {
    
    private val secureRandom = SecureRandom()
    private val pendingAuthentications = mutableMapOf<String, PendingAuth>()
    
    suspend fun initiateAuthentication(minecraftUuid: UUID): String {
        val authToken = generateAuthToken()
        val pendingAuth = PendingAuth(
            minecraftUuid = minecraftUuid,
            timestamp = System.currentTimeMillis(),
            attempts = 0
        )
        
        pendingAuthentications[authToken] = pendingAuth
        cleanupExpiredAuthentications()
        
        return authToken
    }
    
    suspend fun completeAuthentication(authToken: String, chzzkCode: String): ChzzkProfile {
        val pendingAuth = pendingAuthentications[authToken]
            ?: throw LinkageException("유효하지 않은 인증 토큰입니다.")
        
        if (isAuthenticationExpired(pendingAuth)) {
            pendingAuthentications.remove(authToken)
            throw LinkageException("인증 시간이 만료되었습니다.")
        }
        
        if (pendingAuth.attempts >= MAX_ATTEMPTS) {
            pendingAuthentications.remove(authToken)
            throw LinkageException("최대 시도 횟수를 초과했습니다.")
        }
        
        try {
            val chzzkProfile = chzzkApiClient.authenticateWithCode(chzzkCode)
                ?: throw LinkageException("유효하지 않은 치지직 인증 코드입니다.")
            
            pendingAuthentications.remove(authToken)
            return chzzkProfile
        } catch (e: Exception) {
            pendingAuth.attempts++
            pendingAuthentications[authToken] = pendingAuth
            throw LinkageException("치지직 인증 중 오류가 발생했습니다: ${e.message}")
        }
    }
    
    fun cancelAuthentication(authToken: String): Boolean {
        return pendingAuthentications.remove(authToken) != null
    }
    
    fun getAuthenticationStatus(authToken: String): AuthStatus {
        val pendingAuth = pendingAuthentications[authToken]
            ?: return AuthStatus.NOT_FOUND
        
        return when {
            isAuthenticationExpired(pendingAuth) -> {
                pendingAuthentications.remove(authToken)
                AuthStatus.EXPIRED
            }
            pendingAuth.attempts >= MAX_ATTEMPTS -> {
                pendingAuthentications.remove(authToken)
                AuthStatus.MAX_ATTEMPTS_REACHED
            }
            else -> AuthStatus.PENDING
        }
    }
    
    private fun generateAuthToken(): String {
        val bytes = ByteArray(32)
        secureRandom.nextBytes(bytes)
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes)
    }
    
    private fun isAuthenticationExpired(pendingAuth: PendingAuth): Boolean {
        return System.currentTimeMillis() - pendingAuth.timestamp > AUTH_EXPIRY_TIME
    }
    
    private fun cleanupExpiredAuthentications() {
        val now = System.currentTimeMillis()
        val iterator = pendingAuthentications.iterator()
        
        while (iterator.hasNext()) {
            val entry = iterator.next()
            if (now - entry.value.timestamp > AUTH_EXPIRY_TIME) {
                iterator.remove()
            }
        }
    }
    
    private data class PendingAuth(
        val minecraftUuid: UUID,
        val timestamp: Long,
        var attempts: Int
    )
    
    enum class AuthStatus {
        PENDING,
        EXPIRED,
        MAX_ATTEMPTS_REACHED,
        NOT_FOUND
    }
    
    companion object {
        private const val AUTH_EXPIRY_TIME = 5 * 60 * 1000L // 5분
        private const val MAX_ATTEMPTS = 3
    }
}