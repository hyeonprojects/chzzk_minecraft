package kr.kro.chzzk.minecraft.model

import java.util.*

/**
 * 치지직 연동 사용자 데이터 모델
 */
data class User(
    val id: Int,
    val minecraftUuid: UUID,
    val minecraftName: String,
    val chzzkId: String,
    val chzzkName: String,
    val chzzkDevCode: String,
    val createdAt: Long,
    val updatedAt: Long
)