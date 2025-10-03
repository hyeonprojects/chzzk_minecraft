package kr.kro.chzzk.minecraft.infrastructure.config

import org.bukkit.plugin.java.JavaPlugin

/**
 * Configuration manager for ChzzkMinecraftPlugin
 * Loads and provides access to plugin.yml values
 */
class PluginConfig(private val plugin: JavaPlugin) {
    
    /**
     * Plugin name from plugin.yml
     */
    val name: String = plugin.description.name
    
    /**
     * Plugin version from plugin.yml
     */
    val version: String = plugin.description.version ?: "Unknown"
    
    /**
     * Plugin description from plugin.yml
     */
    val description: String = plugin.description.description ?: ""
    
    /**
     * Plugin author from plugin.yml
     */
    val author: String = plugin.description.authors.firstOrNull() ?: "Unknown"
    
    /**
     * Plugin website from plugin.yml
     */
    val website: String = plugin.description.website ?: ""
    
    /**
     * Plugin API version from plugin.yml
     */
    val apiVersion: String = plugin.description.apiVersion ?: ""
    
    /**
     * Plugin main class from plugin.yml
     */
    val mainClass: String = plugin.description.main
    
    /**
     * Get all permission names from plugin.yml
     */
    fun getAllPermissions(): List<String> {
        return plugin.description.permissions.map { it.name }
    }
    
    /**
     * Check if a permission exists in plugin.yml
     */
    fun hasPermission(permission: String): Boolean {
        return plugin.description.permissions.any { it.name == permission }
    }
    
    /**
     * Get permission description from plugin.yml
     */
    fun getPermissionDescription(permission: String): String? {
        return plugin.description.permissions.find { it.name == permission }?.description
    }
    
    /**
     * Get permission default value from plugin.yml
     */
    fun getPermissionDefault(permission: String): String? {
        return plugin.description.permissions.find { it.name == permission }?.default?.toString()
    }
    
    /**
     * Print all configuration values for debugging
     */
    fun printDebugInfo() {
        plugin.logger.info("=== Plugin Configuration ===")
        plugin.logger.info("Name: $name")
        plugin.logger.info("Version: $version")
        plugin.logger.info("Description: $description")
        plugin.logger.info("Author: $author")
        plugin.logger.info("Website: $website")
        plugin.logger.info("API Version: $apiVersion")
        plugin.logger.info("Main Class: $mainClass")
        plugin.logger.info("Permissions: ${getAllPermissions()}")
    }
}