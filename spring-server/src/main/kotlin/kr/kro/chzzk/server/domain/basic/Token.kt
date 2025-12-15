package kr.kro.chzzk.server.domain.basic

import java.time.Instant

/**
 * 토큰 정보 및 계정 정보
 */
data class Token(
    val id: Long,
    val nickName: String,
    val clientId: String,
    val accessToken: String,
    val refreshToken: String,
    val createdAt: Instant,
    val expiredAt: Instant,
    val revokedAt: Instant? = null,
    ) {

    /**
     * 토큰이 만료되었는지 여부
     */
    fun isExpired(): Boolean {
        return Instant.now().isAfter(expiredAt)
    }

    /**
     * 토큰이 취소되었는지 여부
     */
    fun isRevoked(): Boolean {
        return revokedAt != null
    }

    /**
     * 토큰 데이터 변경
     */
    fun rotateTokens(newAccessToken: String, newRefreshToken: String, newExpiredAt: Instant): Token {
        return this.copy(
            accessToken = newAccessToken,
            refreshToken = newRefreshToken,
            expiredAt = newExpiredAt,
            revokedAt = null
        )
    }

    /**
     * 토큰 취소
     */
    fun revoke(): Token {
        return this.copy(
            revokedAt = Instant.now()
        )
    }
}