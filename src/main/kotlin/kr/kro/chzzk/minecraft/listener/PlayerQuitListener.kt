package kr.kro.chzzk.minecraft.listener

import kr.kro.chzzk.minecraft.database.UserRepository
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

/**
 * 플레이어 퇴장 이벤트 처리
 */
class PlayerQuitListener(
    private val userRepository: UserRepository
) : Listener {
    
    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        // 현재는 특별한 처리 없음
        // 필요시 마지막 접속 시간 업데이트 등을 추가
    }
}