package kr.kro.chzzk.minecraft.presentation.command

import kr.kro.chzzk.minecraft.application.service.UserLinkService
import kr.kro.chzzk.minecraft.presentation.message.MessageManager
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import kotlinx.coroutines.runBlocking

class AdminCommand(
    private val userLinkService: UserLinkService,
    private val messageManager: MessageManager
) : CommandExecutor {
    
    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        if (!sender.hasPermission("chzzk.admin")) {
            sender.sendMessage(messageManager.getMessage("error.permission"))
            return false
        }
        
        if (args.isEmpty()) {
            sendAdminHelp(sender)
            return true
        }
        
        return when (args[0].lowercase()) {
            "stats" -> handleStats(sender)
            "list" -> handleList(sender, args)
            "reload" -> handleReload(sender)
            else -> {
                sender.sendMessage(messageManager.getMessage("error.unknown-command"))
                sendAdminHelp(sender)
                false
            }
        }
    }
    
    private fun sendAdminHelp(sender: CommandSender) {
        sender.sendMessage("§6=== 치지직 관리자 명령어 ===")
        sender.sendMessage("§e/chzzk admin stats §7- 연동 통계 보기")
        sender.sendMessage("§e/chzzk admin list [page] §7- 연동된 사용자 목록")
        sender.sendMessage("§e/chzzk admin reload §7- 설정 다시 로드")
    }
    
    private fun handleStats(sender: CommandSender): Boolean {
        runBlocking {
            try {
                val stats = userLinkService.getLinkageStats()
                
                sender.sendMessage(messageManager.getMessage("admin.stats.title"))
                sender.sendMessage(
                    messageManager.getMessage("admin.stats.total", stats["total"] ?: 0)
                )
                sender.sendMessage(
                    messageManager.getMessage("admin.stats.this-week", stats["thisWeek"] ?: 0)
                )
                sender.sendMessage(
                    messageManager.getMessage("admin.stats.this-month", stats["thisMonth"] ?: 0)
                )
                sender.sendMessage(
                    messageManager.getMessage("admin.stats.active", stats["active"] ?: 0)
                )
                
            } catch (e: Exception) {
                sender.sendMessage(messageManager.getMessage("error.general"))
            }
        }
        return true
    }
    
    private fun handleList(sender: CommandSender, args: Array<out String>): Boolean {
        runBlocking {
            try {
                val page = if (args.size > 1) args[1].toIntOrNull() ?: 1 else 1
                val pageSize = 10
                val allUsers = userLinkService.getAllLinkedUsers()
                val totalPages = (allUsers.size + pageSize - 1) / pageSize
                
                if (page < 1 || page > totalPages) {
                    sender.sendMessage("§c유효하지 않은 페이지 번호입니다. (1-$totalPages)")
                    return@runBlocking
                }
                
                val startIndex = (page - 1) * pageSize
                val endIndex = minOf(startIndex + pageSize, allUsers.size)
                val pageUsers = allUsers.subList(startIndex, endIndex)
                
                sender.sendMessage("§6=== 연동된 사용자 목록 (페이지 $page/$totalPages) ===")
                
                pageUsers.forEach { user ->
                    sender.sendMessage("§7- §e${user.minecraftName} §7→ §b${user.chzzkName}")
                }
                
                if (totalPages > 1) {
                    sender.sendMessage("§7다음 페이지: §e/chzzk admin list ${page + 1}")
                }
                
            } catch (e: Exception) {
                sender.sendMessage(messageManager.getMessage("error.general"))
            }
        }
        return true
    }
    
    private fun handleReload(sender: CommandSender): Boolean {
        try {
            messageManager.reloadMessages()
            sender.sendMessage("§a설정이 다시 로드되었습니다.")
        } catch (e: Exception) {
            sender.sendMessage("§c설정 로드 중 오류가 발생했습니다.")
        }
        return true
    }
}