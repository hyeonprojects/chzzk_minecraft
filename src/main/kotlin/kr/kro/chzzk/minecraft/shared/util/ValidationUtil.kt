package kr.kro.chzzk.minecraft.shared.util

/**
 * 유효성 검증 유틸리티
 * 
 * 애플리케이션 전반에서 사용되는 다양한 데이터의 유효성을 검증하는 함수들을 제공합니다.
 * 마인크래프트와 치지직 플랫폼의 규칙에 맞는 검증 로직을 포함합니다.
 * 
 * 지원하는 검증:
 * - 마인크래프트 사용자명 규칙
 * - 치지직 ID 및 이름 형식
 * - 인증 코드 형식
 * - 이메일 주소 형식
 * - 입력 데이터 안전화
 * 
 * @author Hyeonprojects
 * @since 1.0
 */
object ValidationUtil {
    
    /** 마인크래프트 사용자명 정규식 (3-16자, 영문자/숫자/언더스코어) */
    private val MINECRAFT_USERNAME_REGEX = Regex("^[a-zA-Z0-9_]{3,16}$")
    
    /** 치지직 ID 정규식 (영문자/숫자/언더스코어/하이픈) */
    private val CHZZK_ID_REGEX = Regex("^[a-zA-Z0-9_-]+$")
    
    /** 인증 코드 정규식 (영문자/숫자/언더스코어/하이픈) */
    private val AUTH_CODE_REGEX = Regex("^[a-zA-Z0-9_-]+$")
    
    /**
     * 마인크래프트 사용자명이 유효한지 검증합니다.
     * 
     * 마인크래프트 공식 규칙:
     * - 3-16자 길이
     * - 영문자(a-z, A-Z), 숫자(0-9), 언더스코어(_)만 허용
     * 
     * @param username 검증할 사용자명
     * @return Boolean 유효한 경우 true
     */
    fun isValidMinecraftUsername(username: String): Boolean {
        return username.matches(MINECRAFT_USERNAME_REGEX)
    }
    
    fun isValidChzzkId(chzzkId: String): Boolean {
        return chzzkId.isNotBlank() && 
               chzzkId.length <= 256 && 
               chzzkId.matches(CHZZK_ID_REGEX)
    }
    
    fun isValidChzzkName(chzzkName: String): Boolean {
        return chzzkName.isNotBlank() && chzzkName.length <= 256
    }
    
    fun isValidAuthCode(authCode: String): Boolean {
        return authCode.isNotBlank() && 
               authCode.length in 6..512 && 
               authCode.matches(AUTH_CODE_REGEX)
    }
    
    fun validateMinecraftUsername(username: String) {
        require(isValidMinecraftUsername(username)) {
            "유효하지 않은 마인크래프트 사용자명: $username (3-16자, 영문자/숫자/언더스코어만 허용)"
        }
    }
    
    fun validateChzzkId(chzzkId: String) {
        require(isValidChzzkId(chzzkId)) {
            "유효하지 않은 치지직 ID: $chzzkId"
        }
    }
    
    fun validateChzzkName(chzzkName: String) {
        require(isValidChzzkName(chzzkName)) {
            "유효하지 않은 치지직 이름: $chzzkName"
        }
    }
    
    fun validateAuthCode(authCode: String) {
        require(isValidAuthCode(authCode)) {
            "유효하지 않은 인증 코드: 6-512자의 영문자/숫자/하이픈/언더스코어만 허용됩니다."
        }
    }
    
    fun sanitizeInput(input: String): String {
        return input.trim()
            .replace(Regex("\\s+"), " ")
            .take(1000) // 최대 1000자로 제한
    }
    
    fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
        return email.matches(emailRegex)
    }
}