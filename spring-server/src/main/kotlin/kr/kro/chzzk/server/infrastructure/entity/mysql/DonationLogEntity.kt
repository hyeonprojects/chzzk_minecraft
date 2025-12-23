package kr.kro.chzzk.server.infrastructure.entity.mysql

data class DonationLogEntity(
    val id: Long,
    val userId: Long,
    val amount: Double,
    val donatedAt: Instant,
)
