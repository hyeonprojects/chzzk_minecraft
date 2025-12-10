package kr.kro.chzzk.minecraft.util

import org.bukkit.plugin.java.JavaPlugin

/**
 * 설정 관리 유틸리티
 */
class ConfigUtil(private val plugin: JavaPlugin) {
    
    init {
        loadDefaults()
    }
    
    private fun loadDefaults() {
        plugin.config.addDefault("database.path", "chzzk_minecraft.db")
        plugin.config.addDefault("api.timeout", 30)
        plugin.config.addDefault("api.base-url", "https://api.chzzk.naver.com")
        
        plugin.config.options().copyDefaults(true)
        plugin.saveConfig()
    }
    
    fun getDatabasePath(): String {
        return plugin.config.getString("database.path", "chzzk_minecraft.db")!!
    }
    
    fun getApiTimeout(): Int {
        return plugin.config.getInt("api.timeout", 30)
    }
    
    fun getApiBaseUrl(): String {
        return plugin.config.getString("api.base-url", "https://api.chzzk.naver.com")!!
    }
    
    fun reloadConfig() {
        plugin.reloadConfig()
    }
}