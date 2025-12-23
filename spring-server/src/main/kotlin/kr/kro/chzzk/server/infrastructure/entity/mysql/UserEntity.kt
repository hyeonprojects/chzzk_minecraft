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
@Table(name = "user")
data class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long,

    @Column(name = "user_id", nullable = false, unique = true, length = 100)
    val userId: String,

    @Column(name = "nickname", nullable = false, length = 50)
    val nickName: String,

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    val createdAt: Instant,

    @Column(name = "deleted_at", nullable = false)
    val deletedAt: Instant,
)
