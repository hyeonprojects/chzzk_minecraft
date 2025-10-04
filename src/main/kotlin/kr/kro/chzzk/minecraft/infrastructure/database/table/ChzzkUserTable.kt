package kr.kro.chzzk.minecraft.infrastructure.database.table

import org.jetbrains.exposed.sql.Table

object ChzzkUserTable: Table("chzzk_users") {
    val id = integer("id").autoIncrement()
    val minecraftUuid = varchar("minecraft_uuid", 36).uniqueIndex()
    val minecraftName = varchar("minecraft_name", 16)
    val chzzkId = varchar("chzzk_id", 256).uniqueIndex()
    val chzzkName = varchar("chzzk_name", 256)
    val chzzkDevCode = varchar("chzzk_dev_code", 512)
    val createdAt = long("created_at")
    val updatedAt = long("updated_at")

    override val primaryKey = PrimaryKey(id)
}