package kr.kro.chzzk.minecraft.util

/**
 * 데이터 검증 유틸리티
 */
object ValidationUtil {
    
    private val MINECRAFT_USERNAME_REGEX = Regex("^[a-zA-Z0-9_]{3,16}$")
    private val CHZZK_ID_REGEX = Regex("^[a-zA-Z0-9_-]+$")
    private val AUTH_CODE_REGEX = Regex("^[a-zA-Z0-9_-]+$")
    
    fun isValidMinecraftUsername(username: String): Boolean {
        return username.matches(MINECRAFT_USERNAME_REGEX)
    }
    
    fun isValidChzzkId(chzzkId: String): Boolean {
        return chzzkId.isNotBlank() && 
               chzzkId.length <= 256 && 
               chzzkId.matches(CHZZK_ID_REGEX)
    }
    
    fun isValidAuthCode(authCode: String): Boolean {
        return authCode.isNotBlank() && 
               authCode.length in 6..512 && 
               authCode.matches(AUTH_CODE_REGEX)
    }
}