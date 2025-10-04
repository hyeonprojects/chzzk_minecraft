package kr.kro.chzzk.minecraft.application.dto

data class User (
    val id: Int,
    val minecraftUuid: java.util.UUID,
    val minecraftName: String,
    val chzzkId: String,
    val chzzkName: String,
    val chzzkDevCode: String,
    val createdAt: Long,
    val updatedAt: Long
) {
    override fun toString(): String {
        return "User(id=$id, minecraftName='$minecraftName', chzzkId='$chzzkId', chzzkName='$chzzkName', chzzkDevCode='$chzzkDevCode', createdAt=$createdAt, updatedAt=$updatedAt)"
    }

    fun toDebugString(): String {
        return "User {\n" +
                "  id: $id\n" +
                "  minecraftName: '$minecraftName'\n" +
                "  chzzkId: '$chzzkId'\n" +
                "  chzzkName: '$chzzkName'\n" +
                "  chzzkDevCode: '$chzzkDevCode'\n" +
                "  createdAt: $createdAt\n" +
                "  updatedAt: $updatedAt\n" +
                "}"
    }
}