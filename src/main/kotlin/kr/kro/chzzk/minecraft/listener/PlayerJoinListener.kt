package kr.kro.chzzk.minecraft.listener

import kr.kro.chzzk.minecraft.database.UserRepository
import kr.kro.chzzk.minecraft.util.MessageUtil
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

/**
 * 플레이어 접속 이벤트 처리
 */
class PlayerJoinListener(
    private val userRepository: UserRepository,
    private val messageUtil: MessageUtil
) : Listener {
    
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        
        try {
            val linkedUser = userRepository.findByMinecraftUuid(player.uniqueId)
            
            if (linkedUser != null) {
                // 연결된 계정이 있는 경우
                player.sendMessage("§a치지직 계정 §e${linkedUser.chzzkName}§a와 연결되어 있습니다!")
                
                // 마인크래프트 이름이 변경된 경우 업데이트
                if (linkedUser.minecraftName != player.name) {
                    userRepository.update(linkedUser.copy(minecraftName = player.name))
                    player.sendMessage("§7마인크래프트 이름이 업데이트되었습니다.")
                }
            } else {
                // 연결된 계정이 없는 경우
                player.sendMessage("§7치지직 계정과 연결되지 않았습니다. §e/chzzk link <코드>§7 명령어로 연결하세요.")
            }
        } catch (e: Exception) {
            player.sendMessage("§c오류가 발생했습니다. 관리자에게 문의하세요.")
        }
    }
}