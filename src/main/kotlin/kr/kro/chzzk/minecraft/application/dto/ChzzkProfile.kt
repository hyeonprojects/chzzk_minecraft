package kr.kro.chzzk.minecraft.application.dto

data class ChzzkProfile(
    val chzzkId: String,
    val chzzkName: String,
    val profileImageUrl: String?,
    val isVerified: Boolean,
    val followerCount: Int = 0
) {
    override fun toString(): String {
        return "ChzzkProfile(chzzkId='$chzzkId', chzzkName='$chzzkName', isVerified=$isVerified, followerCount=$followerCount)"
    }
}