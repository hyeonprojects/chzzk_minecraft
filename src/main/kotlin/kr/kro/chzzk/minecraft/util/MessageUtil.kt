package kr.kro.chzzk.minecraft.util

import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

/**
 * 메시지 관리 유틸리티
 */
class MessageUtil(private val plugin: JavaPlugin) {
    
    private var messagesConfig: FileConfiguration? = null
    private var messagesFile: File? = null
    
    init {
        loadMessages()
    }
    
    fun getMessage(key: String, vararg args: Any): String {
        val message = messagesConfig?.getString("messages.$key") 
            ?: getDefaultMessage(key)
        
        return if (args.isNotEmpty()) {
            String.format(message, *args)
        } else {
            message
        }.replace("&", "§")
    }
    
    fun reloadMessages() {
        loadMessages()
    }
    
    private fun loadMessages() {
        messagesFile = File(plugin.dataFolder, "messages.yml")
        
        if (!messagesFile!!.exists()) {
            createDefaultMessages()
        }
        
        messagesConfig = YamlConfiguration.loadConfiguration(messagesFile!!)
    }
    
    private fun createDefaultMessages() {
        plugin.dataFolder.mkdirs()
        messagesFile!!.createNewFile()
        
        val config = YamlConfiguration()
        
        // 기본 메시지들
        config.set("messages.link.success", "&a치지직 계정이 성공적으로 연결되었습니다!")
        config.set("messages.link.already-linked", "&c이미 연결된 계정입니다.")
        config.set("messages.unlink.success", "&a치지직 계정 연결이 해제되었습니다.")
        config.set("messages.unlink.not-linked", "&c연결된 치지직 계정이 없습니다.")
        config.set("messages.status.linked", "&a치지직 계정과 연결되어 있습니다.")
        config.set("messages.status.not-linked", "&c치지직 계정과 연결되어 있지 않습니다.")
        config.set("messages.error.permission", "&c권한이 없습니다.")
        config.set("messages.error.player-only", "&c이 명령어는 플레이어만 사용할 수 있습니다.")
        config.set("messages.error.general", "&c오류가 발생했습니다.")
        
        config.save(messagesFile!!)
    }
    
    private fun getDefaultMessage(key: String): String {
        return when (key) {
            "error.general" -> "§c오류가 발생했습니다."
            "error.permission" -> "§c권한이 없습니다."
            "error.player-only" -> "§c플레이어만 사용 가능합니다."
            else -> "§7메시지를 찾을 수 없습니다: $key"
        }
    }
}