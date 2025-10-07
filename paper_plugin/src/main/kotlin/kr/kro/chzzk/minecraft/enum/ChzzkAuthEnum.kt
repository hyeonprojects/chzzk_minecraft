package kr.kro.chzzk.minecraft.enum

// 치지직 인증 방식 Enum
enum class ChzzkAuthGrandType(val value: String) {
    AUTH("authorization_code"), // 인증 코드
    REFRESH("refresh_token") // 리프레시 토큰
}