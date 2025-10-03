package kr.kro.chzzk.minecraft.infrastructure.database.table

import org.jetbrains.exposed.sql.Table

// 치지직 사용자 테이블 정의
object ChzzkUser: Table() {
    val id = integer("id").autoIncrement()
    val minecraftName = varchar("minecraft_name", 256)
    val chzzkId = varchar("chzzk_id", 256).uniqueIndex()
    val chzzkName = varchar("chzzk_name", 256)
    val chzzkDevCode = varchar("chzzk_dev_code", 256)
    val createdAt = long("created_at")
    val updatedAt = long("updated_at")

    override val primaryKey = PrimaryKey(id)
}