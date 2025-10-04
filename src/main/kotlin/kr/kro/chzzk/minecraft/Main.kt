package kr.kro.chzzk.minecraft

import org.bukkit.plugin.java.JavaPlugin
import kr.kro.chzzk.minecraft.shared.di.DIContainer
import kotlinx.coroutines.runBlocking

/**
 * 치지직 마인크래프트 연동 플러그인의 메인 클래스
 * 
 * 이 플러그인은 치지직 스트리밍 플랫폼과 마인크래프트 서버를 연동하여
 * 플레이어들이 자신의 치지직 계정을 마인크래프트 계정과 연결할 수 있도록 합니다.
 * 
 * 주요 기능:
 * - 치지직 계정과 마인크래프트 계정 연결/해제
 * - 연결 상태 확인 및 관리
 * - 관리자용 통계 및 관리 기능
 * - 다국어 메시지 지원
 * 
 * 아키텍처:
 * - Clean Architecture 패턴 적용
 * - 의존성 주입을 통한 느슨한 결합
 * - Paper API 1.21.8 기반
 * - Kotlin Coroutines를 활용한 비동기 처리
 * 
 * @author Hyeonprojects
 * @version 1.0-SNAPSHOT
 * @since 1.0
 */
class Main : JavaPlugin() {
    
    /**
     * 의존성 주입 컨테이너
     * 모든 서비스와 컴포넌트의 의존성을 관리합니다.
     */
    private lateinit var container: DIContainer
    
    /**
     * 플러그인 활성화 시 호출되는 메서드
     * 
     * 다음 작업들을 순차적으로 수행합니다:
     * 1. 의존성 주입 컨테이너 초기화
     * 2. 데이터베이스 연결 설정
     * 3. 명령어 등록
     * 4. 이벤트 리스너 등록
     * 5. 설정 정보 로딩 및 검증
     * 
     * @throws Exception 초기화 과정에서 오류 발생 시 플러그인이 비활성화됩니다
     */
    override fun onEnable() {
        try {
            // 의존성 주입 컨테이너 초기화
            container = DIContainer(this)
            container.initialize()
            
            // 명령어 등록
            getCommand("chzzk")?.setExecutor(container.authCommand)
            getCommand("chzzkadmin")?.setExecutor(container.adminCommand)
            
            // 이벤트 리스너 등록
            server.pluginManager.registerEvents(container.playerJoinListener, this)
            server.pluginManager.registerEvents(container.playerQuitListener, this)
            
            logger.info("치지직 마인크래프트 연동 플러그인이 활성화되었습니다.")
            logger.info("플러그인 정보: ${container.pluginConfig.name} v${container.pluginConfig.version} by ${container.pluginConfig.author}")
            
            // 설정 디버그 정보 출력
            if (logger.isLoggable(java.util.logging.Level.INFO)) {
                container.pluginConfig.printDebugInfo()
                container.databaseConfig.printDebugInfo()
            }
            
        } catch (e: Exception) {
            logger.severe("플러그인 초기화 중 오류가 발생했습니다: ${e.message}")
            e.printStackTrace()
            server.pluginManager.disablePlugin(this)
        }
    }

    /**
     * 플러그인 비활성화 시 호출되는 메서드
     * 
     * 다음 정리 작업들을 수행합니다:
     * 1. 데이터베이스 연결 해제
     * 2. 활성 작업들 정리
     * 3. 리소스 해제
     * 
     * 오류가 발생하더라도 최대한 정리 작업을 완료하려고 시도합니다.
     */
    override fun onDisable() {
        try {
            if (::container.isInitialized) {
                container.shutdown()
            }
            
            logger.info("치지직 마인크래프트 연동 플러그인이 비활성화되었습니다.")
        } catch (e: Exception) {
            logger.severe("플러그인 종료 중 오류가 발생했습니다: ${e.message}")
            e.printStackTrace()
        }
    }
    
    /**
     * 의존성 주입 컨테이너를 반환합니다.
     * 
     * 다른 컴포넌트에서 서비스에 접근해야 할 때 사용됩니다.
     * 주로 테스트나 확장 플러그인에서 활용됩니다.
     * 
     * @return DIContainer 초기화된 의존성 주입 컨테이너
     * @throws UninitializedPropertyAccessException 플러그인이 아직 초기화되지 않은 경우
     */
    fun getContainer(): DIContainer {
        return container
    }
}