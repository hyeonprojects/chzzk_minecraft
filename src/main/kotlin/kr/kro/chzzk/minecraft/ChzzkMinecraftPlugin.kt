package kr.kro.chzzk.minecraft

import org.bukkit.plugin.java.JavaPlugin

class ChzzkMinecraftPlugin : JavaPlugin() {
    
    // Configuration manager instance
    lateinit var config: Config
    
    // This method is called when the plugin is enabled
    override fun onEnable() {
        // Initialize configuration
        config = Config(this)
        
        // Event 활성화

        // 활성화 완료에 대한 구성
        logger.info("치즈마인크래프트 연동 플러그인이 활성화되었습니다.")
        logger.info("관련정보 : Plugin ${config.name} v${config.version} by ${config.author}")
    }

    // This method is called when the plugin is disabled
    override fun onDisable() {
        // Plugin shutdown logic
        logger.info("치즈마인크래프트 연동 플러그인이 비활성화되었습니다.")
    }
}