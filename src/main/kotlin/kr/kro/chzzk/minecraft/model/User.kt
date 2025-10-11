package kr.kro.chzzk.minecraft.model

import java.util.*

/**
 * 치지직 연동 사용자 데이터 모델
 */
data class User(
    val id: Int, // DB 고유 ID
    val minecraftUuid: UUID, // 마인크래프트 UUID
    val minecraftName: String, // 마인크래프트 닉네임
    val chzzkId: String, // 치지직 ID
    val chzzkName: String, // 치지직 닉네임
    val chzzkDevCode: String, // 치지직 개발자 코드
    val createdAt: Long,
    val updatedAt: Long
)