package kr.kro.chzzk.minecraft.database

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

/**
 * 데이터베이스 연결 및 초기화 관리
 */
class DatabaseManager(private val plugin: JavaPlugin) {
    
    private var database: Database? = null
    
    fun connect(): Database {
        if (database != null) {
            return database!!
        }
        
        val databaseFile = File(plugin.dataFolder, "chzzk_minecraft.db")
        
        if (!plugin.dataFolder.exists()) {
            plugin.dataFolder.mkdirs()
        }
        
        val jdbcUrl = "jdbc:sqlite:${databaseFile.absolutePath}"
        
        database = Database.connect(
            url = jdbcUrl,
            driver = "org.sqlite.JDBC"
        )
        
        // 테이블 생성
        transaction(database) {
            SchemaUtils.create(UserTable)
        }
        
        plugin.logger.info("데이터베이스 연결 완료: ${databaseFile.absolutePath}")
        return database!!
    }
    
    fun disconnect() {
        database = null
        plugin.logger.info("데이터베이스 연결 해제 완료")
    }
    
    fun getDatabase(): Database? = database
}