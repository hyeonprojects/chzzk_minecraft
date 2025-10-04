package kr.kro.chzzk.minecraft.presentation.listener

import kr.kro.chzzk.minecraft.application.service.AuthService
import kr.kro.chzzk.minecraft.application.service.UserLinkService
import kr.kro.chzzk.minecraft.presentation.message.MessageManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import kotlinx.coroutines.runBlocking

class PlayerJoinListener(
    private val authService: AuthService,
    private val userLinkService: UserLinkService,
    private val messageManager: MessageManager
) : Listener {
    
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        
        runBlocking {
            try {
                val linkedUser = authService.getLinkedAccount(player.uniqueId)
                
                if (linkedUser != null) {
                    // 연결된 계정이 있는 경우
                    player.sendMessage(
                        messageManager.getMessage(
                            "join.linked",
                            linkedUser.chzzkName
                        )
                    )
                    
                    // 마인크래프트 이름이 변경된 경우 업데이트
                    if (linkedUser.minecraftName != player.name) {
                        userLinkService.updateMinecraftName(player.uniqueId, player.name)
                        player.sendMessage(
                            messageManager.getMessage(
                                "join.name-updated",
                                linkedUser.minecraftName,
                                player.name
                            )
                        )
                    }
                } else {
                    // 연결된 계정이 없는 경우
                    player.sendMessage(
                        messageManager.getMessage("join.not-linked")
                    )
                }
            } catch (e: Exception) {
                player.sendMessage(
                    messageManager.getMessage("error.general")
                )
                // TODO: 로깅
            }
        }
    }
}