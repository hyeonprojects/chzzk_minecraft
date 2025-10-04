package kr.kro.chzzk.minecraft.application.usecase

import kr.kro.chzzk.minecraft.application.dto.User
import kr.kro.chzzk.minecraft.application.service.AuthService
import kr.kro.chzzk.minecraft.domain.exception.LinkageException
import java.util.*

/**
 * 계정 연결 유스케이스
 * 
 * 마인크래프트 계정과 치지직 계정을 연결하는 비즈니스 유스케이스를 담당합니다.
 * Clean Architecture의 Use Case 패턴을 구현하여 애플리케이션의 핵심 기능을 캡슐화합니다.
 * 
 * 이 유스케이스는 다음과 같은 흐름으로 동작합니다:
 * 1. 요청 데이터 검증
 * 2. 중복 연결 확인
 * 3. 치지직 API를 통한 인증
 * 4. 계정 연결 정보 저장
 * 5. 결과 반환
 * 
 * @param authService 인증 관련 비즈니스 로직을 담당하는 서비스
 * @author Hyeonprojects
 * @since 1.0
 */
class LinkAccountUseCase(
    private val authService: AuthService
) {
    
    /**
     * 계정 연결 유스케이스를 실행합니다.
     * 
     * @param request 계정 연결 요청 정보
     * @return LinkAccountResponse 연결 결과 (성공 시 사용자 정보, 실패 시 오류 메시지)
     */
    suspend fun execute(request: LinkAccountRequest): LinkAccountResponse {
        try {
            val user = authService.linkAccount(
                minecraftUuid = request.minecraftUuid,
                minecraftName = request.minecraftName,
                authCode = request.authCode
            )
            
            return LinkAccountResponse.Success(user)
        } catch (e: LinkageException) {
            return LinkAccountResponse.Error(e.message ?: "연결 중 오류가 발생했습니다.")
        } catch (e: Exception) {
            return LinkAccountResponse.Error("시스템 오류가 발생했습니다.")
        }
    }
    
    /**
     * 계정 연결 요청 데이터
     * 
     * @property minecraftUuid 마인크래프트 플레이어의 고유 UUID
     * @property minecraftName 마인크래프트 플레이어 이름
     * @property authCode 치지직에서 발급받은 인증 코드
     */
    data class LinkAccountRequest(
        val minecraftUuid: UUID,
        val minecraftName: String,
        val authCode: String
    )
    
    /**
     * 계정 연결 응답 데이터
     * 
     * Sealed class를 사용하여 성공/실패 상태를 명확히 구분합니다.
     */
    sealed class LinkAccountResponse {
        /**
         * 연결 성공 응답
         * @property user 생성된 사용자 정보
         */
        data class Success(val user: User) : LinkAccountResponse()
        
        /**
         * 연결 실패 응답
         * @property message 실패 사유 메시지
         */
        data class Error(val message: String) : LinkAccountResponse()
    }
}