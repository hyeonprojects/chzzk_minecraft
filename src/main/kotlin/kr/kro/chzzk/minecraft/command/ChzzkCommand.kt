package kr.kro.chzzk.minecraft.command

import kr.kro.chzzk.minecraft.database.UserRepository
import kr.kro.chzzk.minecraft.api.ChzzkApiClient
import kr.kro.chzzk.minecraft.model.User
import kr.kro.chzzk.minecraft.util.MessageUtil
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/**
 * 치지직 연동 명령어 처리
 */
class ChzzkCommand(
    private val userRepository: UserRepository,
    private val chzzkApiClient: ChzzkApiClient,
    private val messageUtil: MessageUtil
) : CommandExecutor {
    
    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        if (sender !is Player) {
            sender.sendMessage("§c이 명령어는 플레이어만 사용할 수 있습니다.")
            return false
        }
        
        if (args.isEmpty()) {
            sendHelp(sender)
            return true
        }
        
        return when (args[0].lowercase()) {
            "link" -> handleLink(sender, args)
            "unlink" -> handleUnlink(sender)
            "status" -> handleStatus(sender)
            else -> {
                sender.sendMessage("§c알 수 없는 명령어입니다.")
                sendHelp(sender)
                false
            }
        }
    }
    
    private fun sendHelp(player: Player) {
        player.sendMessage("§6=== 치지직 연동 도움말 ===")
        player.sendMessage("§e/chzzk link <코드> §7- 치지직 계정과 연결")
        player.sendMessage("§e/chzzk unlink §7- 치지직 계정 연결 해제")
        player.sendMessage("§e/chzzk status §7- 현재 연결 상태 확인")
    }
    
    private fun handleLink(player: Player, args: Array<out String>): Boolean {
        if (!player.hasPermission("chzzk.link")) {
            player.sendMessage("§c권한이 없습니다.")
            return false
        }
        
        if (args.size != 2) {
            player.sendMessage("§c사용법: /chzzk link <코드>")
            return false
        }
        
        val authCode = args[1]
        
        // 기존 연결 확인 (임시 주석 처리)
        // userRepository.findByMinecraftUuid(player.uniqueId)?.let {
        //     player.sendMessage("§c이미 연결된 계정입니다.")
        //     return false
        // }
        
        // 치지직 API 호출 (임시 주석 처리)
        // val chzzkProfile = chzzkApiClient.getProfileByAuthCode(authCode)
        // if (chzzkProfile == null) {
        //     player.sendMessage("§c유효하지 않은 인증 코드입니다.")
        //     return false
        // }
        
        // 중복 연결 확인 (임시 주석 처리)
        // userRepository.findByChzzkId(chzzkProfile.chzzkId)?.let {
        //     player.sendMessage("§c이미 다른 마인크래프트 계정과 연결된 치지직 계정입니다.")
        //     return false
        // }
        
        // 사용자 저장 (임시 주석 처리)
        // val user = User(
        //     id = 0,
        //     minecraftUuid = player.uniqueId,
        //     minecraftName = player.name,
        //     chzzkId = chzzkProfile.chzzkId,
        //     chzzkName = chzzkProfile.chzzkName,
        //     chzzkDevCode = authCode,
        //     createdAt = System.currentTimeMillis(),
        //     updatedAt = System.currentTimeMillis()
        // )
        
        // userRepository.save(user)
        player.sendMessage("§a치지직 계정 연결 기능은 현재 개발 중입니다.")
        // player.sendMessage("§7연결된 계정: §e${chzzkProfile.chzzkName}")
        
        return true
    }
    
    private fun handleUnlink(player: Player): Boolean {
        if (!player.hasPermission("chzzk.unlink")) {
            player.sendMessage("§c권한이 없습니다.")
            return false
        }
        
        val user = userRepository.findByMinecraftUuid(player.uniqueId)
        if (user == null) {
            player.sendMessage("§c연결된 치지직 계정이 없습니다.")
            return false
        }
        
        userRepository.deleteById(user.id)
        player.sendMessage("§a치지직 계정 연결이 해제되었습니다.")
        
        return true
    }
    
    private fun handleStatus(player: Player): Boolean {
        if (!player.hasPermission("chzzk.status")) {
            player.sendMessage("§c권한이 없습니다.")
            return false
        }
        
        val user = userRepository.findByMinecraftUuid(player.uniqueId)
        if (user != null) {
            player.sendMessage("§a치지직 계정과 연결되어 있습니다.")
            player.sendMessage("§7연결된 계정: §e${user.chzzkName}")
        } else {
            player.sendMessage("§c치지직 계정과 연결되어 있지 않습니다.")
            player.sendMessage("§7/chzzk link <코드> 명령어로 연결할 수 있습니다.")
        }
        
        return true
    }
}