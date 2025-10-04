package kr.kro.chzzk.minecraft

import kr.kro.chzzk.minecraft.command.ChzzkCommand
import kr.kro.chzzk.minecraft.command.ChzzkAdminCommand
import kr.kro.chzzk.minecraft.database.DatabaseManager
import kr.kro.chzzk.minecraft.database.UserRepository
import kr.kro.chzzk.minecraft.api.ChzzkApiClient
import kr.kro.chzzk.minecraft.listener.PlayerJoinListener
import kr.kro.chzzk.minecraft.listener.PlayerQuitListener
import kr.kro.chzzk.minecraft.util.MessageUtil
import kr.kro.chzzk.minecraft.util.ConfigUtil
import org.bukkit.plugin.java.JavaPlugin

/**
 * 치지직 마인크래프트 연동 플러그인 메인 클래스
 */
class Main : JavaPlugin() {
    
    private lateinit var databaseManager: DatabaseManager
    private lateinit var userRepository: UserRepository
    private lateinit var chzzkApiClient: ChzzkApiClient
    private lateinit var messageUtil: MessageUtil
    private lateinit var configUtil: ConfigUtil
    
    override fun onEnable() {
        try {
            // 유틸리티 초기화
            configUtil = ConfigUtil(this)
            messageUtil = MessageUtil(this)
            
            // 데이터베이스 초기화
            databaseManager = DatabaseManager(this)
            databaseManager.connect()
            userRepository = UserRepository(databaseManager)
            
            // API 클라이언트 초기화
            chzzkApiClient = ChzzkApiClient()
            
            // 명령어 등록
            getCommand("chzzk")?.setExecutor(ChzzkCommand(userRepository, chzzkApiClient, messageUtil))
            getCommand("chzzkadmin")?.setExecutor(ChzzkAdminCommand(userRepository, messageUtil))
            
            // 이벤트 리스너 등록
            server.pluginManager.registerEvents(PlayerJoinListener(userRepository, messageUtil), this)
            server.pluginManager.registerEvents(PlayerQuitListener(userRepository), this)
            
            logger.info("치지직 마인크래프트 연동 플러그인이 활성화되었습니다.")
            
        } catch (e: Exception) {
            logger.severe("플러그인 초기화 중 오류가 발생했습니다: ${e.message}")
            e.printStackTrace()
            server.pluginManager.disablePlugin(this)
        }
    }
    
    override fun onDisable() {
        try {
            if (::databaseManager.isInitialized) {
                databaseManager.disconnect()
            }
            
            logger.info("치지직 마인크래프트 연동 플러그인이 비활성화되었습니다.")
        } catch (e: Exception) {
            logger.severe("플러그인 종료 중 오류가 발생했습니다: ${e.message}")
            e.printStackTrace()
        }
    }
}