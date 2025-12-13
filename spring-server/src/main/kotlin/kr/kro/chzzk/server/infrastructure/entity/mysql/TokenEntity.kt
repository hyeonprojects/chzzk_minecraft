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

    @Column(name)

    @Column(name = "access_token", nullable = false, length = 500)
    val accessToken: String,
    val refreshToken: String,
    val createdAt: Long,
    val expiredAt: Long,
    val revokedAt: Long? = null,
) {

}