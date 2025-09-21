package kr.kro.chzzk.minecraft

import org.bukkit.plugin.java.JavaPlugin

class main : JavaPlugin() {
    // This method is called when the plugin is enabled
    override fun onEnable() {
        // Plugin startup logic
        logger.info("Paper plugin has been enabled")
    }

    // This method is called when the plugin is disabled
    override fun onDisable() {
        // Plugin shutdown logic
        logger.info("Paper plugin has been disabled")
    }
}