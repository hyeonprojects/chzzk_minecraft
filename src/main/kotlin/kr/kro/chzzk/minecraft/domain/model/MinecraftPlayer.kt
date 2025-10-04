package kr.kro.chzzk.minecraft.domain.model

import java.util.*

/**
 * 마인크래프트 플레이어 도메인 모델
 * 
 * 마인크래프트 플레이어의 핵심 정보와 비즈니스 규칙을 담고 있는 도메인 모델입니다.
 * 불변 객체로 설계되어 데이터의 일관성을 보장합니다.
 * 
 * @property uuid 마인크래프트 플레이어의 고유 UUID (변경 불가)
 * @property name 플레이어 이름 (3-16자, 영문자/숫자/언더스코어만 허용)
 * @property lastSeen 마지막 접속 시간 (밀리초 타임스탬프)
 * @property isOnline 현재 온라인 상태
 * 
 * @author Hyeonprojects
 * @since 1.0
 */
data class MinecraftPlayer(
    val uuid: UUID,
    val name: String,
    val lastSeen: Long = System.currentTimeMillis(),
    val isOnline: Boolean = false
) {
    
    /**
     * 플레이어 이름이 마인크래프트 규칙에 맞는지 검증합니다.
     * 
     * 마인크래프트 사용자명 규칙:
     * - 3-16자 길이
     * - 영문자(a-z, A-Z), 숫자(0-9), 언더스코어(_)만 허용
     * - 대소문자 구분
     * 
     * @return Boolean 유효한 이름이면 true, 아니면 false
     */
    fun isValidName(): Boolean {
        return name.matches(Regex("^[a-zA-Z0-9_]{3,16}$"))
    }
    
    /**
     * 온라인 상태에 따른 색상이 적용된 표시 이름을 반환합니다.
     * 
     * @return String 온라인이면 초록색(§a), 오프라인이면 회색(§7)으로 표시된 이름
     */
    fun getDisplayName(): String {
        return if (isOnline) "§a$name" else "§7$name"
    }
    
    companion object {
        /**
         * UUID와 이름으로부터 MinecraftPlayer 인스턴스를 생성합니다.
         * 
         * 생성 시 이름의 유효성을 검증하며, 유효하지 않은 경우 예외를 발생시킵니다.
         * 
         * @param uuid 플레이어의 UUID
         * @param name 플레이어 이름
         * @return MinecraftPlayer 생성된 플레이어 인스턴스
         * @throws IllegalArgumentException 플레이어 이름이 유효하지 않은 경우
         */
        fun from(uuid: UUID, name: String): MinecraftPlayer {
            require(name.matches(Regex("^[a-zA-Z0-9_]{3,16}$"))) {
                "Invalid Minecraft username: $name"
            }
            
            return MinecraftPlayer(
                uuid = uuid,
                name = name
            )
        }
    }
}