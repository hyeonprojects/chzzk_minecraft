package kr.kro.chzzk.minecraft.domain.exception

/**
 * 사용자를 찾을 수 없는 경우 발생하는 예외
 * 
 * 데이터베이스나 외부 시스템에서 요청한 사용자 정보를 찾을 수 없을 때 발생합니다.
 * 도메인 레이어의 예외로서 비즈니스 로직 상의 오류를 나타냅니다.
 * 
 * @param message 예외 메시지
 * @author Hyeonprojects
 * @since 1.0
 */
class UserNotFoundException(message: String) : Exception(message)