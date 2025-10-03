package kr.kro.chzzk.minecraft.presentation.command

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/**
 * 치지직 연결 명령어를 관리하는 클래스
 */
class AuthCommand : CommandExecutor {
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

    /**
     * 치지직 연동 명령어 도움말을 출력합니다.
     */
    private fun sendHelp(commandSender: CommandSender) {
        commandSender.sendMessage("치지직 연동 명령어 도움말")
        commandSender.sendMessage("/chzzk link <코드> - 치지직 계정과 연결합니다.")
        commandSender.sendMessage("/chzzk unlink - 치지직 계정과의 연결을 해제합니다.")
        commandSender.sendMessage("/chzzk status - 현재 연결 상태를 확인합니다.")
    }

    /**
     * 치지직 계정과 연결하는 명령어를 처리합니다.
     */
    private fun handleLink(commandSender: CommandSender, args: Array<out String>): Boolean {
        // 플레이어만 사용 가능
        if (commandSender !is Player) {
            commandSender.sendMessage("§c이 명령어는 플레이어만 사용할 수 있습니다.")
            return false
        }

        // 권한 확인
        if (!commandSender.hasPermission("chzzk.link")) {
            commandSender.sendMessage("§c이 명령어를 사용할 권한이 없습니다.")
            return false
        }

        if (args.size != 2) {
            commandSender.sendMessage("§c사용법: /chzzk link <코드>")
            return false
        }

        val code = args[1]
        
        // 입력 검증
        if (!isValidCode(code)) {
            commandSender.sendMessage("§c유효하지 않은 코드 형식입니다.")
            return false
        }

        try {
            // TODO: 실제 치지직 API 연동 구현
            // 예시: ChzzkAPI.linkAccount(commandSender.uniqueId, code)
            
            commandSender.sendMessage("§a치지직 계정이 성공적으로 연결되었습니다!")
            commandSender.sendMessage("§7연결된 코드: ${code.take(4)}****")
            return true
        } catch (e: Exception) {
            commandSender.sendMessage("§c연결 중 오류가 발생했습니다. 나중에 다시 시도해주세요.")
            // TODO: 로깅 시스템 연동
            return false
        }
    }

    /**
     * 치지직 계정 연결을 해제하는 명령어를 처리합니다.
     */
    private fun handleUnlink(commandSender: CommandSender): Boolean {
        if (commandSender !is Player) {
            commandSender.sendMessage("§c이 명령어는 플레이어만 사용할 수 있습니다.")
            return false
        }

        if (!commandSender.hasPermission("chzzk.unlink")) {
            commandSender.sendMessage("§c이 명령어를 사용할 권한이 없습니다.")
            return false
        }

        try {
            // TODO: 실제 연결 해제 구현
            // 예시: ChzzkAPI.unlinkAccount(commandSender.uniqueId)
            
            commandSender.sendMessage("§a치지직 계정 연결이 해제되었습니다.")
            return true
        } catch (e: Exception) {
            commandSender.sendMessage("§c연결 해제 중 오류가 발생했습니다.")
            return false
        }
    }

    /**
     * 현재 치지직 연결 상태를 확인하는 명령어를 처리합니다.
     */
    private fun handleStatus(commandSender: CommandSender): Boolean {
        if (commandSender !is Player) {
            commandSender.sendMessage("§c이 명령어는 플레이어만 사용할 수 있습니다.")
            return false
        }

        if (!commandSender.hasPermission("chzzk.status")) {
            commandSender.sendMessage("§c이 명령어를 사용할 권한이 없습니다.")
            return false
        }

        try {
            // TODO: 실제 상태 확인 구현
            // 예시: val isLinked = ChzzkAPI.isLinked(commandSender.uniqueId)
            
            val isLinked = false // 임시값
            
            if (isLinked) {
                commandSender.sendMessage("§a치지직 계정이 연결되어 있습니다.")
                // TODO: 연결된 계정 정보 표시
            } else {
                commandSender.sendMessage("§c치지직 계정이 연결되어 있지 않습니다.")
                commandSender.sendMessage("§7/chzzk link <코드> 명령어로 연결할 수 있습니다.")
            }
            return true
        } catch (e: Exception) {
            commandSender.sendMessage("§c상태 확인 중 오류가 발생했습니다.")
            return false
        }
    }

    /**
     * 연결 코드의 유효성을 검증합니다.
     */
    private fun isValidCode(code: String): Boolean {
        // 기본 검증 (실제 구현 시 치지직 API 규격에 맞게 수정)
        return code.isNotBlank() && 
               code.length in 6..32 && 
               code.matches(Regex("^[a-zA-Z0-9]+$"))
    }
}