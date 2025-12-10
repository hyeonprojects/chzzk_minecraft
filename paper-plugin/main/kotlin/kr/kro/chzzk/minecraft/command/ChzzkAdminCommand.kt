package kr.kro.chzzk.minecraft.command

import kr.kro.chzzk.minecraft.database.UserRepository
import kr.kro.chzzk.minecraft.util.MessageUtil
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

/**
 * 치지직 관리자 명령어 처리
 */
class ChzzkAdminCommand(
    private val userRepository: UserRepository,
    private val messageUtil: MessageUtil
) : CommandExecutor {
    
    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        if (!sender.hasPermission("chzzk.admin")) {
            sender.sendMessage("§c권한이 없습니다.")
            return false
        }
        
        if (args.isEmpty()) {
            sendHelp(sender)
            return true
        }
        
        return when (args[0].lowercase()) {
            "stats" -> handleStats(sender)
            "list" -> handleList(sender, args)
            "reload" -> handleReload(sender)
            else -> {
                sender.sendMessage("§c알 수 없는 명령어입니다.")
                sendHelp(sender)
                false
            }
        }
    }
    
    private fun sendHelp(sender: CommandSender) {
        sender.sendMessage("§6=== 치지직 관리자 명령어 ===")
        sender.sendMessage("§e/chzzkadmin stats §7- 연동 통계")
        sender.sendMessage("§e/chzzkadmin list [page] §7- 연동 사용자 목록")
        sender.sendMessage("§e/chzzkadmin reload §7- 설정 다시 로드")
    }
    
    private fun handleStats(sender: CommandSender): Boolean {
        val allUsers = userRepository.findAll()
        val now = System.currentTimeMillis()
        val oneWeekAgo = now - (7 * 24 * 60 * 60 * 1000)
        val oneMonthAgo = now - (30 * 24 * 60 * 60 * 1000)
        
        sender.sendMessage("§6=== 치지직 연동 통계 ===")
        sender.sendMessage("§7전체 연결: §e${allUsers.size}명")
        sender.sendMessage("§7이번 주 신규: §e${allUsers.count { it.createdAt >= oneWeekAgo }}명")
        sender.sendMessage("§7이번 달 신규: §e${allUsers.count { it.createdAt >= oneMonthAgo }}명")
        sender.sendMessage("§7활성 사용자: §e${allUsers.count { it.updatedAt >= oneWeekAgo }}명")
        
        return true
    }
    
    private fun handleList(sender: CommandSender, args: Array<out String>): Boolean {
        val page = if (args.size > 1) args[1].toIntOrNull() ?: 1 else 1
        val pageSize = 10
        val allUsers = userRepository.findAll()
        val totalPages = (allUsers.size + pageSize - 1) / pageSize
        
        if (page < 1 || page > totalPages) {
            sender.sendMessage("§c유효하지 않은 페이지 번호입니다. (1-$totalPages)")
            return false
        }
        
        val startIndex = (page - 1) * pageSize
        val endIndex = minOf(startIndex + pageSize, allUsers.size)
        val pageUsers = allUsers.subList(startIndex, endIndex)
        
        sender.sendMessage("§6=== 연동된 사용자 목록 (페이지 $page/$totalPages) ===")
        
        pageUsers.forEach { user ->
            sender.sendMessage("§7- §e${user.minecraftName} §7→ §b${user.chzzkName}")
        }
        
        if (totalPages > 1) {
            sender.sendMessage("§7다음 페이지: §e/chzzkadmin list ${page + 1}")
        }
        
        return true
    }
    
    private fun handleReload(sender: CommandSender): Boolean {
        try {
            messageUtil.reloadMessages()
            sender.sendMessage("§a설정이 다시 로드되었습니다.")
        } catch (e: Exception) {
            sender.sendMessage("§c설정 로드 중 오류가 발생했습니다.")
        }
        return true
    }
}