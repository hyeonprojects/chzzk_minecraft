package kr.kro.chzzk.minecraft.dto

import kotlinx.serialization.Serializable
import kr.kro.chzzk.minecraft.enum.ChzzkAuthGrandType

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
@Serializable
data class ChzzkAuthCodeResponseDto(
    val code: String,
    val state: String,
)

/**
 * 토큰 발급 요청을 위한 데이터 클래스
 *
 * @property grantType 인증 방식 (예: "authorization_code")
 * @property clientId 클라이언트 ID
 * @property clientSecret 클라이언트 시크릿
 * @property code 인증 코드
 * @property state 상태 값
 */
@Serializable
data class ChzzkTokenRequest(
    val grantType: ChzzkAuthGrandType,
    val clientId: String,
    val clientSecret: String,
    val code: String?,
    val state: String?,
    val refreshToken: String?
)

/**
 * 토큰 발급 응답을 위한 데이터 클래스
 *
 * @property accessToken 액세스 토큰
 * @property refreshToken 리프레시 토큰
 * @property tokenType 토큰 타입 (예: "Bearer")
 * @property expiresIn 토큰 만료 시간 (초 단위)
 */
@Serializable
data class ChzzkTokenResponse(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String,
    val expiresIn: String // 경우에 따라 Int 또는 Long 타입일 수 있습니다.
)

/**
 * 토큰 폐기(Revocation) 요청을 위한 데이터 클래스
 *
 * @property clientId 클라이언트 ID
 * @property clientSecret 클라이언트 시크릿
 * @property token 폐기할 토큰 (Access Token 또는 Refresh Token)
 * @property tokenTypeHint 폐기할 토큰의 종류를 알려주는 힌트 (선택 사항)
 */
data class RevokeTokenRequest(
    val clientId: String,
    val clientSecret: String,
    val token: String,
    val tokenTypeHint: ChzzkAuthGrandType? // 선택 사항일 수 있으므로 Nullable로 선언하는 것을 고려할 수 있습니다.
)