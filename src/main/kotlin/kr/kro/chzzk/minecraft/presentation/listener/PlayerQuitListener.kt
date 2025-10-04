package kr.kro.chzzk.minecraft.presentation.listener

import kr.kro.chzzk.minecraft.application.service.UserLinkService
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent
import kotlinx.coroutines.runBlocking

class PlayerQuitListener(
    private val userLinkService: UserLinkService
) : Listener {
    
    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        val player = event.player
        
        runBlocking {
            try {
                // 플레이어 마지막 접속 시간 업데이트나 기타 정리 작업
                // 현재는 특별한 작업 없음
            } catch (e: Exception) {
                // TODO: 로깅
            }
        }
    }
}