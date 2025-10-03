package kr.kro.chzzk.minecraft

import org.bukkit.plugin.java.JavaPlugin
import kr.kro.chzzk.minecraft.presentation.command.AuthCommand
import kr.kro.chzzk.minecraft.infrastructure.config.PluginConfig

class Main : JavaPlugin() {
    
    // Configuration manager instance
    lateinit var config: PluginConfig
    
    // This method is called when the plugin is enabled
    override fun onEnable() {
        // DB Connection Open


        // Initialize configuration
        config = PluginConfig(this)
        
        // Command 등록
        getCommand("chzzk")?.setExecutor(AuthCommand())
        
        // Event 활성화

        // 활성화 완료에 대한 구성
        logger.info("치즈마인크래프트 연동 플러그인이 활성화되었습니다.")
        logger.info("관련정보 : Plugin ${config.name} v${config.version} by ${config.author}")
    }

    // This method is called when the plugin is disabled
    override fun onDisable() {
        // DB Connection Close

        // Plugin shutdown logic
        logger.info("치즈마인크래프트 연동 플러그인이 비활성화되었습니다.")
    }
}