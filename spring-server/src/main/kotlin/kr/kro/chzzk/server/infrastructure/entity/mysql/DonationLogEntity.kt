package kr.kro.chzzk.server.infrastructure.entity.mysql

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import java.time.Instant

@Entity
@Table(name = "donation_log")
data class DonationLogEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long = 0,

    @Column(name = "user_id", nullable = false, length = 100)
    val userId: String,

    @Column(name = "event_type", nullable = false, length = 50)
    val eventType: String,

    @Column(name = "donation_type", nullable = false, length = 50)
    val donationType: String,

    @Column(name = "channel_id", nullable = false, length = 100)
    val channelId: String,

    @Column(name = "donator_channel_id", nullable = false, length = 100)
    val donatorChannelId: String,

    @Column(name = "donator_nick_name", nullable = false, length = 100)
    val donatorNickName: String,

    @Column(name = "pay_amount", nullable = false, length = 100)
    val payAmount: String,

    @Column(name = "donation_text", length = 500)
    val donationText: String? = null,

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    val createdAt: Instant = Instant.now(),
)
