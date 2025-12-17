package kr.kro.chzzk.server.infrastructure.entity.mysql

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "token")
data class TokenEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long,

    @Column(name = "nickname", nullable = false)
    val nickName: String,

    @Column(name = "client_id", nullable = false)
    val clientId: String,

    @Column(name = "access_token", nullable = false, length = 500)
    val accessToken: String,

    @Column(name = "refresh_token", nullable = false, length = 500)
    val refreshToken: String,

    @Column(name = "created_at", nullable = false)
    val createdAt: Long,

    @Column(name = "expired_at", nullable = false)
    val expiredAt: Long,

    @Column(name = "revoked_at")
    val revokedAt: Long? = null,
) {

}