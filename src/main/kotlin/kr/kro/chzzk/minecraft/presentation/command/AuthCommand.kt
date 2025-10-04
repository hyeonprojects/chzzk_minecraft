package kr.kro.chzzk.minecraft.presentation.command

import kr.kro.chzzk.minecraft.application.usecase.LinkAccountUseCase
import kr.kro.chzzk.minecraft.application.usecase.UnlinkAccountUseCase
import kr.kro.chzzk.minecraft.application.service.AuthService
import kr.kro.chzzk.minecraft.presentation.message.MessageManager
import kr.kro.chzzk.minecraft.shared.extension.*
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import kotlinx.coroutines.runBlocking

/**
 * 인증 관련 명령어 처리기
 * 
 * 일반 사용자들이 사용하는 치지직 계정 연결 관련 명령어들을 처리합니다.
 * Clean Architecture의 Presentation Layer에서 사용자 입력을 받아
 * 적절한 Use Case나 Service로 요청을 전달하고 결과를 사용자에게 표시합니다.
 * 
 * 지원 명령어:
 * - /chzzk link <코드>: 치지직 계정 연결
 * - /chzzk unlink: 치지직 계정 연결 해제
 * - /chzzk status: 현재 연결 상태 확인
 * - /chzzk help: 도움말 표시
 * 
 * @param linkAccountUseCase 계정 연결 유스케이스
 * @param unlinkAccountUseCase 계정 연결 해제 유스케이스
 * @param authService 인증 관련 서비스
 * @param messageManager 메시지 관리자
 * @author Hyeonprojects
 * @since 1.0
 */
class AuthCommand(
    private val linkAccountUseCase: LinkAccountUseCase,
    private val unlinkAccountUseCase: UnlinkAccountUseCase,
    private val authService: AuthService,
    private val messageManager: MessageManager
) : CommandExecutor {
    override fun onCommand(
        commandSender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        // 도움말 출력
        if (args.isEmpty()) {
            sendHelp(commandSender)
            return true
        }

        // 하위 명령어 처리
        return when (args[0].lowercase()) {
            "link" -> handleLink(commandSender, args)
            "unlink" -> handleUnlink(commandSender)
            "status" -> handleStatus(commandSender)
            else -> {
                commandSender.sendMessage("§c알 수 없는 명령어입니다.")
                sendHelp(commandSender)
                false
            }
        }
    }

    private fun sendHelp(commandSender: CommandSender) {
        commandSender.sendMessage(messageManager.getMessage("command.help.title"))
        commandSender.sendMessage(messageManager.getMessage("command.help.link"))
        commandSender.sendMessage(messageManager.getMessage("command.help.unlink"))
        commandSender.sendMessage(messageManager.getMessage("command.help.status"))
        
        if (commandSender.hasPermission("chzzk.admin")) {
            commandSender.sendMessage(messageManager.getMessage("command.help.admin"))
        }
    }

    private fun handleLink(commandSender: CommandSender, args: Array<out String>): Boolean {
        if (commandSender !is Player) {
            commandSender.sendMessage(messageManager.getMessage("error.player-only"))
            return false
        }

        if (!commandSender.canLinkAccount()) {
            commandSender.sendMessage(messageManager.getMessage("error.permission"))
            return false
        }

        if (args.size != 2) {
            commandSender.sendMessage(
                messageManager.getMessage("error.invalid-usage", "/chzzk link <코드>")
            )
            return false
        }

        val code = args[1]
        
        runBlocking {
            try {
                val request = LinkAccountUseCase.LinkAccountRequest(
                    minecraftUuid = commandSender.uniqueId,
                    minecraftName = commandSender.name,
                    authCode = code
                )
                
                when (val result = linkAccountUseCase.execute(request)) {
                    is LinkAccountUseCase.LinkAccountResponse.Success -> {
                        commandSender.sendChzzkSuccess(
                            messageManager.getMessage("command.link.success")
                        )
                        commandSender.sendMessage(
                            messageManager.getMessage(
                                "command.status.linked-info", 
                                result.user.chzzkName
                            )
                        )
                    }
                    is LinkAccountUseCase.LinkAccountResponse.Error -> {
                        commandSender.sendChzzkError(result.message)
                    }
                }
            } catch (e: Exception) {
                commandSender.sendChzzkError(messageManager.getMessage("error.general"))
            }
        }
        
        return true
    }

    private fun handleUnlink(commandSender: CommandSender): Boolean {
        if (commandSender !is Player) {
            commandSender.sendMessage(messageManager.getMessage("error.player-only"))
            return false
        }

        if (!commandSender.canUnlinkAccount()) {
            commandSender.sendMessage(messageManager.getMessage("error.permission"))
            return false
        }

        runBlocking {
            try {
                val request = UnlinkAccountUseCase.UnlinkAccountRequest(commandSender.uniqueId)
                
                when (val result = unlinkAccountUseCase.execute(request)) {
                    is UnlinkAccountUseCase.UnlinkAccountResponse.Success -> {
                        commandSender.sendChzzkSuccess(
                            messageManager.getMessage("command.unlink.success")
                        )
                    }
                    is UnlinkAccountUseCase.UnlinkAccountResponse.Error -> {
                        commandSender.sendChzzkError(result.message)
                    }
                }
            } catch (e: Exception) {
                commandSender.sendChzzkError(messageManager.getMessage("error.general"))
            }
        }
        
        return true
    }

    private fun handleStatus(commandSender: CommandSender): Boolean {
        if (commandSender !is Player) {
            commandSender.sendMessage(messageManager.getMessage("error.player-only"))
            return false
        }

        if (!commandSender.canCheckStatus()) {
            commandSender.sendMessage(messageManager.getMessage("error.permission"))
            return false
        }

        runBlocking {
            try {
                val linkedUser = authService.getLinkedAccount(commandSender.uniqueId)
                
                if (linkedUser != null) {
                    commandSender.sendChzzkSuccess(
                        messageManager.getMessage("command.status.linked")
                    )
                    commandSender.sendMessage(
                        messageManager.getMessage(
                            "command.status.linked-info",
                            linkedUser.chzzkName
                        )
                    )
                } else {
                    commandSender.sendMessage(
                        messageManager.getMessage("command.status.not-linked")
                    )
                    commandSender.sendMessage(
                        messageManager.getMessage("command.status.how-to-link")
                    )
                }
            } catch (e: Exception) {
                commandSender.sendChzzkError(messageManager.getMessage("error.general"))
            }
        }
        
        return true
    }

}