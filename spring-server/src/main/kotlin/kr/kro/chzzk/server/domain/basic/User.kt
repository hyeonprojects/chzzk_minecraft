package kr.kro.chzzk.server.domain.basic

import java.time.Instant

/**
 * 유저 정보
 */
data class User(
    val id: Long,
    val userId: String,
    val clientId: String,
    val nickname: String,
    val createdAt: Instant = Instant.now(),
    val deletedAt: Instant? = null,
) {

    /**
     * 유저가 삭제되었는지 여부
     */
    fun isDeleted(): Boolean {
        return deletedAt != null
    }

    /**
     * 유저 삭제
     */
    fun delete(): User {
        return this.copy(
            deletedAt = Instant.now()
        )
    }
}
