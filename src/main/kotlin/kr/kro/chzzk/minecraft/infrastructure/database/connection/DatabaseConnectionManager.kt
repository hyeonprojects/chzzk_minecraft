package kr.kro.chzzk.minecraft.infrastructure.database.connection

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import kr.kro.chzzk.minecraft.infrastructure.database.table.ChzzkUserTable
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

/**
 * 데이터베이스 연결 관리자
 * 
 * SQLite 데이터베이스와의 연결을 관리하고 테이블 스키마를 초기화합니다.
 * Exposed ORM을 사용하여 데이터베이스 연산을 추상화하고 타입 안전성을 제공합니다.
 * 
 * 주요 기능:
 * - 데이터베이스 연결 설정 및 해제
 * - 테이블 스키마 자동 생성
 * - 트랜잭션 관리 지원
 * - 데이터 폴더 자동 생성
 * 
 * @param plugin JavaPlugin 인스턴스 (데이터 폴더 경로 제공)
 * @author Hyeonprojects
 * @since 1.0
 */
class DatabaseConnectionManager(
    private val plugin: JavaPlugin
) {
    /** 현재 활성 데이터베이스 연결 */
    private var database: Database? = null
    
    /**
     * 데이터베이스에 연결하고 필요한 테이블을 생성합니다.
     * 
     * SQLite 데이터베이스 파일을 플러그인 데이터 폴더에 생성하고,
     * 필요한 테이블 스키마를 자동으로 생성합니다.
     * 
     * @return Database 연결된 데이터베이스 인스턴스
     * @throws Exception 데이터베이스 연결 또는 테이블 생성 실패 시
     */
    fun connect(): Database {
        if (database != null) {
            return database!!
        }
        
        val databaseFile = File(plugin.dataFolder, "chzzk_minecraft.db")
        
        // 데이터 폴더가 없으면 생성
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
            SchemaUtils.create(ChzzkUserTable)
        }
        
        plugin.logger.info("데이터베이스 연결 완료: ${databaseFile.absolutePath}")
        return database!!
    }
    
    /**
     * 데이터베이스 연결을 해제합니다.
     * 
     * 플러그인 종료 시 호출되어 리소스를 정리합니다.
     */
    fun disconnect() {
        database = null
        plugin.logger.info("데이터베이스 연결 해제 완료")
    }
    
    /**
     * 데이터베이스 연결 상태를 확인합니다.
     * 
     * @return Boolean 연결되어 있으면 true, 아니면 false
     */
    fun isConnected(): Boolean {
        return database != null
    }
    
    /**
     * 현재 데이터베이스 인스턴스를 반환합니다.
     * 
     * @return Database? 연결된 데이터베이스 인스턴스, 연결되지 않은 경우 null
     */
    fun getDatabase(): Database? {
        return database
    }
    
    /**
     * 트랜잭션 내에서 동기 작업을 실행합니다.
     * 
     * @param action 실행할 작업
     * @throws IllegalStateException 데이터베이스가 연결되지 않은 경우
     */
    fun executeInTransaction(action: () -> Unit) {
        database?.let { db ->
            transaction(db) {
                action()
            }
        } ?: throw IllegalStateException("데이터베이스가 연결되지 않았습니다.")
    }
    
    /**
     * 트랜잭션 내에서 비동기 작업을 실행합니다.
     * 
     * @param action 실행할 비동기 작업
     * @return T 작업 결과
     * @throws IllegalStateException 데이터베이스가 연결되지 않은 경우
     */
    suspend fun <T> executeInTransactionSuspend(action: suspend () -> T): T {
        return database?.let { db ->
            transaction(db) {
                runBlocking { action() }
            }
        } ?: throw IllegalStateException("데이터베이스가 연결되지 않았습니다.")
    }
}