package kr.kro.chzzk.minecraft.command

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

/**
 * 치지직 연결 명령어를 관리하는 클래스
 */
class AuthChzzk : CommandExecutor {
    override fun onCommand(
        commandSender: CommandSender,
        command: Command,
        label: String,
        arg: Array<out String>
    ): Boolean {
        // 도움말 출력
        if (arg.isEmpty()) {
            sendHelp(commandSender)
            return true
        }

        // 하위 명령어 처리
        when (arg[0].lowercase()) {
            "link" -> handlerLink(commandSender, arg)
            "unlink" -> TODO("Handle unlink command")
            "status" -> TODO("Handle status command")
            else -> {
                commandSender.sendMessage("알 수 없는 명령어입니다.")
                sendHelp(commandSender)
            }
        }


        return TODO("Provide the return value")
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
    private fun handlerLink(commandSender: CommandSender, args: Array<String>) {
        if (args.size != 2) {
            commandSender.sendMessage("사용법: /chzzk link <코드>")
            return
        }

        val code = args[1]

        // TODO: DB에 연결시키는 코드 구현하기

        commandSender.sendMessage("치지직 계정이 성공적으로 연결되었습니다.")
    }
}