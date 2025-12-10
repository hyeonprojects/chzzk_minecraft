package kr.kro.chzzk.minecraft.webhook

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import com.sun.net.httpserver.HttpServer
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json
import org.bukkit.plugin.Plugin
import java.net.InetSocketAddress
import java.util.concurrent.Executors

/**
 * 내장 HTTP 서버 - Paper 플러그인 내에서 웹훅을 처리하는 경량 서버
 */
class WebhookServer(
    private val plugin: Plugin,
    private val port: Int = 8080,
    private val host: String = "0.0.0.0"
) {
    private var server: HttpServer? = null
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val json = Json { ignoreUnknownKeys = true }
    
    /**
     * HTTP 서버 시작
     */
    fun start() {
        try {
            server = HttpServer.create(InetSocketAddress(host, port), 0).apply {
                // 스레드풀 설정 (Paper 서버와 분리)
                executor = Executors.newFixedThreadPool(4)
                
                // 라우트 등록
                createContext("/health", HealthHandler())
                createContext("/chzzk/auth", ChzzkAuthHandler(plugin, json))
                createContext("/chzzk/notification", ChzzkNotificationHandler(plugin, json))
                
                start()
            }
            
            plugin.logger.info("✅ 웹훅 서버가 시작되었습니다: http://$host:$port")
            plugin.logger.info("📋 등록된 엔드포인트:")
            plugin.logger.info("   - GET  /health - 서버 상태 확인")
            plugin.logger.info("   - POST /chzzk/auth - 치지직 인증 웹훅")
            plugin.logger.info("   - POST /chzzk/notification - 치지직 알림 웹훅")
            
        } catch (e: Exception) {
            plugin.logger.severe("❌ 웹훅 서버 시작 실패: ${e.message}")
            throw e
        }
    }
    
    /**
     * HTTP 서버 종료
     */
    fun stop() {
        try {
            server?.stop(3) // 3초 대기 후 강제 종료
            scope.cancel()
            plugin.logger.info("🛑 웹훅 서버가 종료되었습니다")
        } catch (e: Exception) {
            plugin.logger.warning("⚠️ 웹훅 서버 종료 중 오류: ${e.message}")
        }
    }
}

/**
 * 기본 HTTP 핸들러 추상 클래스
 */
abstract class BaseWebhookHandler : HttpHandler {
    protected fun sendResponse(exchange: HttpExchange, statusCode: Int, response: String) {
        exchange.responseHeaders.add("Content-Type", "application/json; charset=utf-8")
        exchange.responseHeaders.add("Access-Control-Allow-Origin", "*")
        exchange.sendResponseHeaders(statusCode, response.toByteArray().size.toLong())
        exchange.responseBody.use { it.write(response.toByteArray()) }
    }
    
    protected fun readRequestBody(exchange: HttpExchange): String {
        return exchange.requestBody.use { it.readBytes().toString(Charsets.UTF_8) }
    }
}

/**
 * 헬스 체크 핸들러
 */
class HealthHandler : BaseWebhookHandler() {
    override fun handle(exchange: HttpExchange) {
        val response = """
            {
                "status": "healthy",
                "service": "chzzk-minecraft-webhook",
                "timestamp": "${System.currentTimeMillis()}"
            }
        """.trimIndent()
        
        sendResponse(exchange, 200, response)
    }
}