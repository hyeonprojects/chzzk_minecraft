package kr.kro.chzzk.minecraft.model

/**
 * 치지직 채널 정보 응답 데이터
 */
data class ChzzkChannelResponse(
    val channelId: String,
    val channelName: String,
    val channelImageUrl: String?,
    val verifiedMark: Boolean = false
)

/**
 * 치지직 토큰 응답 데이터
 */
data class ChzzkTokenResponse(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String = "Bearer",
    val expiresIn: Long
)

/**
 * 치지직 API 에러 응답
 */
data class ChzzkErrorResponse(
    val code: String,
    val message: String
)