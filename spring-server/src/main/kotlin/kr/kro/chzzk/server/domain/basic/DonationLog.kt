package kr.kro.chzzk.server.domain.basic

data class DonationLog(
    val id: Long,
    val userId: String,
    val eventType: String,
    val donationType: String,
    val channelId: String,
    val donatorChannelId: String,
    val donatorNickName: String,
    val payAmount: String,
    val donationText: String?,
    val createdAt: Long,
)
