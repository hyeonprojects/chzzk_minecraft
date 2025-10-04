package kr.kro.chzzk.minecraft.presentation.message

import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

/**
 * 다국어 메시지 관리자
 * 
 * 플러그인에서 사용되는 모든 메시지를 중앙에서 관리하고 다국어를 지원합니다.
 * YAML 파일을 사용하여 메시지를 외부에서 수정할 수 있도록 하며,
 * 색상 코드 변환과 변수 치환 기능을 제공합니다.
 * 
 * 주요 기능:
 * - 외부 YAML 파일에서 메시지 로드
 * - 색상 코드 자동 변환 (&코드 → §코드)
 * - 변수 치환 지원 (String.format 사용)
 * - 기본값 메시지 제공
 * - 런타임 메시지 리로드
 * 
 * @param plugin JavaPlugin 인스턴스
 * @author Hyeonprojects
 * @since 1.0
 */
class MessageManager(private val plugin: JavaPlugin) {
    
    /** 메시지 설정 파일 */
    private var messagesConfig: FileConfiguration? = null
    
    /** 메시지 파일 객체 */
    private var messagesFile: File? = null
    
    init {
        loadMessages()
    }
    
    /**
     * 지정된 키의 메시지를 가져와 변수를 치환하고 색상 코드를 변환합니다.
     * 
     * @param key 메시지 키 (예: "command.link.success")
     * @param args 메시지에 치환할 변수들
     * @return String 완전히 처리된 메시지
     */
    fun getMessage(key: String, vararg args: Any): String {
        val message = messagesConfig?.getString("messages.$key") 
            ?: getDefaultMessage(key)
        
        return if (args.isNotEmpty()) {
            String.format(message, *args)
        } else {
            message
        }.replace("&", "§")
    }
    
    /**
     * 메시지 파일을 로드합니다.
     * 파일이 존재하지 않으면 기본 메시지로 새로 생성합니다.
     */
    private fun loadMessages() {
        messagesFile = File(plugin.dataFolder, "messages.yml")
        
        if (!messagesFile!!.exists()) {
            createDefaultMessages()
        }
        
        messagesConfig = YamlConfiguration.loadConfiguration(messagesFile!!)
    }
    
    /**
     * 기본 메시지가 포함된 messages.yml 파일을 생성합니다.
     */
    private fun createDefaultMessages() {
        plugin.dataFolder.mkdirs()
        messagesFile!!.createNewFile()
        
        val config = YamlConfiguration()
        
        // 기본 메시지들
        config.set("messages.join.linked", "&a치지직 계정 &e%s&a와 연결되어 있습니다!")
        config.set("messages.join.not-linked", "&7치지직 계정과 연결되지 않았습니다. &e/chzzk link <코드>&7 명령어로 연결하세요.")
        config.set("messages.join.name-updated", "&7마인크래프트 이름이 &e%s&7에서 &e%s&7로 업데이트되었습니다.")
        
        config.set("messages.command.help.title", "&6=== 치지직 연동 도움말 ===")
        config.set("messages.command.help.link", "&e/chzzk link <코드> &7- 치지직 계정과 연결합니다.")
        config.set("messages.command.help.unlink", "&e/chzzk unlink &7- 치지직 계정과의 연결을 해제합니다.")
        config.set("messages.command.help.status", "&e/chzzk status &7- 현재 연결 상태를 확인합니다.")
        config.set("messages.command.help.admin", "&e/chzzk admin <stats|list> &7- 관리자 명령어 (관리자만)")
        
        config.set("messages.command.link.success", "&a치지직 계정이 성공적으로 연결되었습니다!")
        config.set("messages.command.link.already-linked", "&c이미 연결된 계정입니다.")
        config.set("messages.command.link.invalid-code", "&c유효하지 않은 인증 코드입니다.")
        config.set("messages.command.link.chzzk-already-linked", "&c이미 다른 마인크래프트 계정과 연결된 치지직 계정입니다.")
        
        config.set("messages.command.unlink.success", "&a치지직 계정 연결이 해제되었습니다.")
        config.set("messages.command.unlink.not-linked", "&c연결된 치지직 계정이 없습니다.")
        
        config.set("messages.command.status.linked", "&a치지직 계정과 연결되어 있습니다.")
        config.set("messages.command.status.linked-info", "&7연결된 계정: &e%s")
        config.set("messages.command.status.not-linked", "&c치지직 계정과 연결되어 있지 않습니다.")
        config.set("messages.command.status.how-to-link", "&7/chzzk link <코드> 명령어로 연결할 수 있습니다.")
        
        config.set("messages.error.general", "&c오류가 발생했습니다. 관리자에게 문의하세요.")
        config.set("messages.error.permission", "&c이 명령어를 사용할 권한이 없습니다.")
        config.set("messages.error.player-only", "&c이 명령어는 플레이어만 사용할 수 있습니다.")
        config.set("messages.error.invalid-usage", "&c사용법: %s")
        config.set("messages.error.unknown-command", "&c알 수 없는 명령어입니다.")
        
        config.set("messages.admin.stats.title", "&6=== 치지직 연동 통계 ===")
        config.set("messages.admin.stats.total", "&7전체 연결: &e%d명")
        config.set("messages.admin.stats.this-week", "&7이번 주 신규: &e%d명")
        config.set("messages.admin.stats.this-month", "&7이번 달 신규: &e%d명")
        config.set("messages.admin.stats.active", "&7활성 사용자: &e%d명")
        
        config.save(messagesFile!!)
    }
    
    /**
     * 키에 해당하는 기본 메시지를 반환합니다.
     * 설정 파일에서 메시지를 찾을 수 없을 때 사용됩니다.
     * 
     * @param key 메시지 키
     * @return String 기본 메시지
     */
    private fun getDefaultMessage(key: String): String {
        return when (key) {
            "error.general" -> "§c오류가 발생했습니다."
            "error.permission" -> "§c권한이 없습니다."
            "error.player-only" -> "§c플레이어만 사용 가능합니다."
            else -> "§7메시지를 찾을 수 없습니다: $key"
        }
    }
    
    /**
     * 메시지 파일을 다시 로드합니다.
     * 런타임 중에 메시지를 수정한 후 다시 로드할 때 사용됩니다.
     */
    fun reloadMessages() {
        loadMessages()
    }
}