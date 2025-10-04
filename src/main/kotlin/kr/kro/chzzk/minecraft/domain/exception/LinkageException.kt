package kr.kro.chzzk.minecraft.domain.exception

/**
 * 계정 연결 과정에서 발생하는 예외
 * 
 * 마인크래프트 계정과 치지직 계정 간의 연결/해제 과정에서
 * 비즈니스 규칙 위반이나 시스템 오류가 발생할 때 사용됩니다.
 * 
 * 발생 상황:
 * - 이미 연결된 계정을 다시 연결하려는 경우
 * - 유효하지 않은 인증 코드 사용
 * - 연결 규칙 위반 (예: 중복 연결)
 * - 외부 API 호출 실패
 * 
 * @param message 예외 메시지
 * @param cause 원인이 되는 예외 (선택적)
 * @author Hyeonprojects
 * @since 1.0
 */
class LinkageException(message: String, cause: Throwable? = null) : Exception(message, cause)