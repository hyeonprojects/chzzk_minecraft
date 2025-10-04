package kr.kro.chzzk.minecraft.application.usecase

import kr.kro.chzzk.minecraft.application.service.AuthService
import kr.kro.chzzk.minecraft.domain.exception.UserNotFoundException
import java.util.*

class UnlinkAccountUseCase(
    private val authService: AuthService
) {
    
    suspend fun execute(request: UnlinkAccountRequest): UnlinkAccountResponse {
        try {
            val success = authService.unlinkAccount(request.minecraftUuid)
            
            return if (success) {
                UnlinkAccountResponse.Success
            } else {
                UnlinkAccountResponse.Error("연결 해제에 실패했습니다.")
            }
        } catch (e: UserNotFoundException) {
            return UnlinkAccountResponse.Error(e.message ?: "연결된 계정을 찾을 수 없습니다.")
        } catch (e: Exception) {
            return UnlinkAccountResponse.Error("시스템 오류가 발생했습니다.")
        }
    }
    
    data class UnlinkAccountRequest(
        val minecraftUuid: UUID
    )
    
    sealed class UnlinkAccountResponse {
        object Success : UnlinkAccountResponse()
        data class Error(val message: String) : UnlinkAccountResponse()
    }
}