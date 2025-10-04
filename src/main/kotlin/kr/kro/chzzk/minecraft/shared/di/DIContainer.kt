package kr.kro.chzzk.minecraft.shared.di

import kr.kro.chzzk.minecraft.application.service.AuthService
import kr.kro.chzzk.minecraft.application.service.UserLinkService
import kr.kro.chzzk.minecraft.application.usecase.LinkAccountUseCase
import kr.kro.chzzk.minecraft.application.usecase.UnlinkAccountUseCase
import kr.kro.chzzk.minecraft.domain.repository.UserRepository
import kr.kro.chzzk.minecraft.domain.repository.ChzzkApiRepository
import kr.kro.chzzk.minecraft.domain.service.AuthenticationDomainService
import kr.kro.chzzk.minecraft.infrastructure.chzzk.ChzzkApiClient
import kr.kro.chzzk.minecraft.infrastructure.chzzk.ChzzkAuthenticator
import kr.kro.chzzk.minecraft.infrastructure.config.PluginConfig
import kr.kro.chzzk.minecraft.infrastructure.config.DatabaseConfig
import kr.kro.chzzk.minecraft.infrastructure.database.connection.DatabaseConnectionManager
import kr.kro.chzzk.minecraft.infrastructure.database.repository.UserRepositoryImpl
import kr.kro.chzzk.minecraft.presentation.command.AuthCommand
import kr.kro.chzzk.minecraft.presentation.command.AdminCommand
import kr.kro.chzzk.minecraft.presentation.listener.PlayerJoinListener
import kr.kro.chzzk.minecraft.presentation.listener.PlayerQuitListener
import kr.kro.chzzk.minecraft.presentation.message.MessageManager
import org.bukkit.plugin.java.JavaPlugin

/**
 * 의존성 주입 컨테이너
 * 
 * 플러그인의 모든 서비스, 리포지토리, 컨트롤러들의 의존성을 관리하고
 * 생성과 생명주기를 담당합니다. Singleton 패턴으로 구현되어 있으며,
 * 지연 초기화(lazy initialization)를 통해 필요할 때 인스턴스를 생성합니다.
 * 
 * 컨테이너 구조:
 * - Configuration Layer: 설정 관리
 * - Infrastructure Layer: 외부 시스템 연동 및 데이터 액세스
 * - Domain Layer: 비즈니스 로직과 도메인 서비스
 * - Application Layer: 애플리케이션 서비스와 유스케이스
 * - Presentation Layer: 사용자 인터페이스 (명령어, 이벤트)
 * 
 * @param plugin JavaPlugin 인스턴스
 * @author Hyeonprojects
 * @since 1.0
 */
class DIContainer(private val plugin: JavaPlugin) {
    
    // ======== Configuration Layer ========
    /** 플러그인 기본 설정 관리자 */
    val pluginConfig by lazy { PluginConfig(plugin) }
    
    /** 데이터베이스 설정 관리자 */
    val databaseConfig by lazy { DatabaseConfig(plugin) }
    
    /** 다국어 메시지 관리자 */
    val messageManager by lazy { MessageManager(plugin) }
    
    // ======== Infrastructure Layer ========
    /** 데이터베이스 연결 관리자 */
    val databaseConnectionManager by lazy { DatabaseConnectionManager(plugin) }
    
    /** 치지직 API 클라이언트 */
    val chzzkApiClient by lazy { ChzzkApiClient() }
    
    /** 치지직 인증 처리기 */
    val chzzkAuthenticator by lazy { ChzzkAuthenticator(chzzkApiClient) }
    
    // ======== Repository Layer ========
    /** 사용자 데이터 접근 레포지토리 */
    val userRepository: UserRepository by lazy { UserRepositoryImpl(databaseConnectionManager) }
    
    /** 치지직 API 접근 레포지토리 */
    val chzzkApiRepository: ChzzkApiRepository by lazy { chzzkApiClient }
    
    // ======== Domain Layer ========
    /** 인증 도메인 서비스 */
    val authenticationDomainService by lazy { AuthenticationDomainService() }
    
    // ======== Application Layer ========
    /** 인증 관련 애플리케이션 서비스 */
    val authService by lazy { AuthService(userRepository, chzzkApiRepository) }
    
    /** 사용자 연결 관리 애플리케이션 서비스 */
    val userLinkService by lazy { UserLinkService(userRepository) }
    
    // ======== Use Cases ========
    /** 계정 연결 유스케이스 */
    val linkAccountUseCase by lazy { LinkAccountUseCase(authService) }
    
    /** 계정 연결 해제 유스케이스 */
    val unlinkAccountUseCase by lazy { UnlinkAccountUseCase(authService) }
    
    // ======== Presentation Layer ========
    /** 일반 사용자 명령어 처리기 */
    val authCommand by lazy { AuthCommand(linkAccountUseCase, unlinkAccountUseCase, authService, messageManager) }
    
    /** 관리자 명령어 처리기 */
    val adminCommand by lazy { AdminCommand(userLinkService, messageManager) }
    
    /** 플레이어 접속 이벤트 리스너 */
    val playerJoinListener by lazy { PlayerJoinListener(authService, userLinkService, messageManager) }
    
    /** 플레이어 퇴장 이벤트 리스너 */
    val playerQuitListener by lazy { PlayerQuitListener(userLinkService) }
    
    /**
     * 컨테이너 초기화
     * 
     * 플러그인 활성화 시 호출되어 다음 작업들을 수행합니다:
     * 1. 데이터베이스 설정 로드 및 기본값 설정
     * 2. 데이터베이스 연결 초기화
     * 3. 필요한 테이블 생성
     * 
     * @throws Exception 초기화 과정에서 오류 발생 시
     */
    fun initialize() {
        // 데이터베이스 설정 로드
        databaseConfig.loadDefaults()
        
        // 데이터베이스 연결
        databaseConnectionManager.connect()
        
        plugin.logger.info("의존성 주입 컨테이너 초기화 완료")
    }
    
    /**
     * 컨테이너 종료
     * 
     * 플러그인 비활성화 시 호출되어 다음 정리 작업들을 수행합니다:
     * 1. 데이터베이스 연결 해제
     * 2. 활성 작업들 정리
     * 3. 리소스 해제
     * 
     * 오류가 발생하더라도 최대한 정리를 완료하려고 시도합니다.
     */
    fun shutdown() {
        // 데이터베이스 연결 해제
        databaseConnectionManager.disconnect()
        
        plugin.logger.info("의존성 주입 컨테이너 종료 완료")
    }
}