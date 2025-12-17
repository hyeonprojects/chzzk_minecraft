package kr.kro.chzzk.server.presentation.dto

object ChzzkAuthManagementDto {

    /**
     * 치지직 소셜 로그인 리다이렉트 요청 DTO
     */
    data class RedirectRequest(
        val code: String,
        val state: String,
    )

    data class RegisterRequest(
        val nickname: String,
        val email: String,
        val password: String
    )
}