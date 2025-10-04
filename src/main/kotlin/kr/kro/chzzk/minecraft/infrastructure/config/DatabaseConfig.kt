package kr.kro.chzzk.minecraft.infrastructure.config

import org.bukkit.plugin.java.JavaPlugin

class DatabaseConfig(private val plugin: JavaPlugin) {
    
    val databasePath: String
        get() = plugin.config.getString("database.path", "chzzk_minecraft.db") ?: "chzzk_minecraft.db"
    
    val connectionTimeout: Int
        get() = plugin.config.getInt("database.connection-timeout", 30)
    
    val maxConnections: Int
        get() = plugin.config.getInt("database.max-connections", 10)
    
    val enableWAL: Boolean
        get() = plugin.config.getBoolean("database.enable-wal", true)
    
    val enableForeignKeys: Boolean
        get() = plugin.config.getBoolean("database.enable-foreign-keys", true)
    
    val backupEnabled: Boolean
        get() = plugin.config.getBoolean("database.backup.enabled", true)
    
    val backupInterval: Int
        get() = plugin.config.getInt("database.backup.interval-hours", 24)
    
    val maxBackupFiles: Int
        get() = plugin.config.getInt("database.backup.max-files", 7)
    
    fun loadDefaults() {
        plugin.config.addDefault("database.path", "chzzk_minecraft.db")
        plugin.config.addDefault("database.connection-timeout", 30)
        plugin.config.addDefault("database.max-connections", 10)
        plugin.config.addDefault("database.enable-wal", true)
        plugin.config.addDefault("database.enable-foreign-keys", true)
        plugin.config.addDefault("database.backup.enabled", true)
        plugin.config.addDefault("database.backup.interval-hours", 24)
        plugin.config.addDefault("database.backup.max-files", 7)
        
        plugin.config.options().copyDefaults(true)
        plugin.saveConfig()
    }
    
    fun printDebugInfo() {
        plugin.logger.info("=== Database Configuration ===")
        plugin.logger.info("Path: $databasePath")
        plugin.logger.info("Connection Timeout: ${connectionTimeout}s")
        plugin.logger.info("Max Connections: $maxConnections")
        plugin.logger.info("WAL Mode: $enableWAL")
        plugin.logger.info("Foreign Keys: $enableForeignKeys")
        plugin.logger.info("Backup Enabled: $backupEnabled")
        if (backupEnabled) {
            plugin.logger.info("Backup Interval: ${backupInterval}h")
            plugin.logger.info("Max Backup Files: $maxBackupFiles")
        }
    }
}