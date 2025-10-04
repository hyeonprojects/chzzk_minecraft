package kr.kro.chzzk.minecraft.domain.service

import kr.kro.chzzk.minecraft.domain.model.MinecraftPlayer
import kr.kro.chzzk.minecraft.domain.model.ChzzkUser
import kr.kro.chzzk.minecraft.domain.exception.LinkageException
import java.util.*

/**
 * 인증 도메인 서비스
 * 
 * 마인크래프트 계정과 치지직 계정 간의 연결에 대한 핵심 비즈니스 규칙과 검증 로직을 담당합니다.
 * 도메인 레이어의 서비스로서 여러 도메인 모델 간의 복잡한 비즈니스 로직을 처리합니다.
 * 
 * 주요 책임:
 * - 계정 연결 시 비즈니스 규칙 검증
 * - 인증 코드 유효성 검증
 * - 연결 만료 관리
 * - 고유 연결 ID 생성
 * 
 * @author Hyeonprojects
 * @since 1.0
 */
class AuthenticationDomainService {
    
    /**
     * 계정 연결에 필요한 모든 정보의 유효성을 검증합니다.
     * 
     * @param minecraftPlayer 마인크래프트 플레이어 정보
     * @param chzzkUser 치지직 사용자 정보
     * @param authCode 인증 코드
     * @throws LinkageException 검증에 실패한 경우
     */
    fun validateLinkage(
        minecraftPlayer: MinecraftPlayer,
        chzzkUser: ChzzkUser,
        authCode: String
    ) {
        validateMinecraftPlayer(minecraftPlayer)
        validateChzzkUser(chzzkUser)
        validateAuthCode(authCode)
    }
    
    private fun validateMinecraftPlayer(player: MinecraftPlayer) {
        if (!player.isValidName()) {
            throw LinkageException("유효하지 않은 마인크래프트 사용자명입니다: ${player.name}")
        }
    }
    
    private fun validateChzzkUser(user: ChzzkUser) {
        if (!user.isValidChzzkId()) {
            throw LinkageException("유효하지 않은 치지직 사용자 ID입니다: ${user.chzzkId}")
        }
    }
    
    private fun validateAuthCode(authCode: String) {
        when {
            authCode.isBlank() -> throw LinkageException("인증 코드가 비어있습니다.")
            authCode.length < 6 -> throw LinkageException("인증 코드가 너무 짧습니다.")
            authCode.length > 128 -> throw LinkageException("인증 코드가 너무 깁니다.")
            !authCode.matches(Regex("^[a-zA-Z0-9_-]+$")) -> {
                throw LinkageException("인증 코드 형식이 올바르지 않습니다.")
            }
        }
    }
    
    /**
     * 계정 연결에 대한 고유 ID를 생성합니다.
     * 
     * 생성되는 ID는 마인크래프트 UUID, 치지직 ID, 생성 시간을 조합하여
     * 시스템 내에서 고유성을 보장합니다.
     * 
     * @param minecraftUuid 마인크래프트 플레이어 UUID
     * @param chzzkId 치지직 사용자 ID
     * @return String 고유한 연결 ID
     */
    fun generateLinkageId(minecraftUuid: UUID, chzzkId: String): String {
        return "${minecraftUuid}_${chzzkId}_${System.currentTimeMillis()}"
    }
    
    /**
     * 계정 연결이 만료되었는지 확인합니다.
     * 
     * @param createdAt 연결 생성 시간 (밀리초 타임스탬프)
     * @param expiryDays 만료 기간 (일 단위, 기본 365일)
     * @return Boolean 만료된 경우 true, 아니면 false
     */
    fun isLinkageExpired(createdAt: Long, expiryDays: Int = 365): Boolean {
        val expiryTime = createdAt + (expiryDays * 24 * 60 * 60 * 1000L)
        return System.currentTimeMillis() > expiryTime
    }
}