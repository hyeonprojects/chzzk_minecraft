package kr.kro.chzzk.minecraft.dto

// 치지직 API 개발자 셋팅 DTO
data class ChzzkDevSettingDto(
    val clientId: String,
    val clientSecret: String,
    val applicationName: String,
    val applicationKoreaName: String,
    val redirectUrl: String,
)

// 인증 코드 요청 및 발급 Request
data class ChzzkAuthCodeRequestDto(
    val clientId: String,
    val redirectUri: String,
    val state: String,
)

// 인증 코드 발급 Response
data class ChzzkAuthCodeResponseDto(
    val code: String,
    val state: String,
)