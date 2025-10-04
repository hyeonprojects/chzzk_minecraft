package kr.kro.chzzk.minecraft.infrastructure.chzzk.response

data class ChzzkApiResponse<T>(
    val code: Int,
    val message: String?,
    val content: T?
) {
    fun isSuccess(): Boolean = code == 200
    
    fun getContentOrThrow(): T {
        return content ?: throw IllegalStateException("API response content is null")
    }
}

data class ChzzkChannelResponse(
    val channelId: String,
    val channelName: String,
    val channelImageUrl: String?,
    val verifiedMark: Boolean,
    val channelDescription: String?,
    val followerCount: Int,
    val openLive: Boolean
)

data class ChzzkUserResponse(
    val hasProfile: Boolean,
    val userIdHash: String,
    val nickname: String,
    val profileImageUrl: String?,
    val penaltyInfo: ChzzkPenaltyInfo?
)

data class ChzzkPenaltyInfo(
    val penaltyType: String,
    val penaltyPeriod: String?
)