package kr.kro.chzzk.minecraft.domain.model

data class ChzzkUser(
    val chzzkId: String,
    val chzzkName: String,
    val profileImageUrl: String? = null,
    val isVerified: Boolean = false,
    val followerCount: Int = 0,
    val description: String? = null
) {
    
    fun isValidChzzkId(): Boolean {
        return chzzkId.isNotBlank() && chzzkId.matches(Regex("^[a-zA-Z0-9_-]+$"))
    }
    
    fun getDisplayName(): String {
        return if (isVerified) "§6✓ $chzzkName" else chzzkName
    }
    
    fun getFormattedFollowerCount(): String {
        return when {
            followerCount >= 1_000_000 -> "%.1fM".format(followerCount / 1_000_000.0)
            followerCount >= 1_000 -> "%.1fK".format(followerCount / 1_000.0)
            else -> followerCount.toString()
        }
    }
    
    companion object {
        fun create(chzzkId: String, chzzkName: String): ChzzkUser {
            require(chzzkId.isNotBlank()) { "Chzzk ID cannot be blank" }
            require(chzzkName.isNotBlank()) { "Chzzk name cannot be blank" }
            require(chzzkId.matches(Regex("^[a-zA-Z0-9_-]+$"))) { 
                "Invalid Chzzk ID format: $chzzkId" 
            }
            
            return ChzzkUser(
                chzzkId = chzzkId,
                chzzkName = chzzkName
            )
        }
    }
}