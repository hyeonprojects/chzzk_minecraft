package kr.kro.chzzk.minecraft.webhook

import com.sun.net.httpserver.HttpExchange
import kotlinx.coroutines.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin

/**
 * 치지직 인증 웹훅 핸들러
 */
class ChzzkAuthHandler(
    private val plugin: Plugin,
    private val json: Json
) : BaseWebhookHandler() {
    
    override fun handle(exchange: HttpExchange) {
        when (exchange.requestMethod) {
            "POST" -> handleAuthWebhook(exchange)
            "OPTIONS" -> handleCors(exchange)
            else -> sendResponse(exchange, 405, """{"error": "Method not allowed"}""")
        }
    }
    
    private fun handleAuthWebhook(exchange: HttpExchange) {
        try {
            val requestBody = readRequestBody(exchange)
            plugin.logger.info("📨 치지직 인증 웹훅 수신: $requestBody")
            
            // JSON 파싱
            val authData = json.decodeFromString<ChzzkAuthWebhook>(requestBody)
            
            // 메인 스레드에서 플레이어 처리 (Paper 플러그인 스레드 안전성)
            Bukkit.getScheduler().runTask(plugin, Runnable {
                processAuthWebhook(authData)
            })
            
            val response = """
                {
                    "success": true,
                    "message": "인증 웹훅이 처리되었습니다",
                    "userId": "${authData.userId}",
                    "timestamp": "${System.currentTimeMillis()}"
                }
            """.trimIndent()
            
            sendResponse(exchange, 200, response)
            
        } catch (e: Exception) {
            plugin.logger.severe("❌ 인증 웹훅 처리 오류: ${e.message}")
            sendResponse(exchange, 400, """{"error": "Invalid request: ${e.message}"}""")
        }
    }
    
    private fun processAuthWebhook(authData: ChzzkAuthWebhook) {
        // 실제 인증 처리 로직 (예제)
        plugin.logger.info("🔐 사용자 인증 처리: ${authData.userId} (${authData.username})")
        
        // TODO: 데이터베이스에 저장
        // TODO: 플레이어에게 권한 부여
        // TODO: 알림 메시지 전송
        
        val player = Bukkit.getPlayer(authData.minecraftUuid)
        if (player != null && player.isOnline) {
            player.sendMessage("§a[치지직] 인증이 완료되었습니다! 환영합니다, ${authData.username}님!")
        }
    }
    
    private fun handleCors(exchange: HttpExchange) {
        exchange.responseHeaders.add("Access-Control-Allow-Origin", "*")
        exchange.responseHeaders.add("Access-Control-Allow-Methods", "POST, OPTIONS")
        exchange.responseHeaders.add("Access-Control-Allow-Headers", "Content-Type")
        sendResponse(exchange, 200, "")
    }
}

/**
 * 치지직 알림 웹훅 핸들러
 */
class ChzzkNotificationHandler(
    private val plugin: Plugin,
    private val json: Json
) : BaseWebhookHandler() {
    
    override fun handle(exchange: HttpExchange) {
        when (exchange.requestMethod) {
            "POST" -> handleNotificationWebhook(exchange)
            "OPTIONS" -> handleCors(exchange)
            else -> sendResponse(exchange, 405, """{"error": "Method not allowed"}""")
        }
    }
    
    private fun handleNotificationWebhook(exchange: HttpExchange) {
        try {
            val requestBody = readRequestBody(exchange)
            plugin.logger.info("📢 치지직 알림 웹훅 수신: $requestBody")
            
            // JSON 파싱
            val notification = json.decodeFromString<ChzzkNotificationWebhook>(requestBody)
            
            // 메인 스레드에서 알림 처리
            Bukkit.getScheduler().runTask(plugin, Runnable {
                processNotification(notification)
            })
            
            val response = """
                {
                    "success": true,
                    "message": "알림 웹훅이 처리되었습니다",
                    "type": "${notification.type}",
                    "timestamp": "${System.currentTimeMillis()}"
                }
            """.trimIndent()
            
            sendResponse(exchange, 200, response)
            
        } catch (e: Exception) {
            plugin.logger.severe("❌ 알림 웹훅 처리 오류: ${e.message}")
            sendResponse(exchange, 400, """{"error": "Invalid request: ${e.message}"}""")
        }
    }
    
    private fun processNotification(notification: ChzzkNotificationWebhook) {
        when (notification.type) {
            "STREAM_START" -> {
                plugin.logger.info("🎥 스트림 시작 알림: ${notification.streamerName}")
                broadcastMessage("§6[치지직] §e${notification.streamerName}님이 방송을 시작했습니다!")
            }
            "DONATION" -> {
                plugin.logger.info("💰 후원 알림: ${notification.amount}원 by ${notification.donorName}")
                broadcastMessage("§6[치지직] §a${notification.donorName}님이 ${notification.amount}원을 후원했습니다! 감사합니다!")
            }
            "FOLLOW" -> {
                plugin.logger.info("👥 팔로우 알림: ${notification.followerName}")
                broadcastMessage("§6[치지직] §b${notification.followerName}님이 팔로우했습니다!")
            }
            else -> {
                plugin.logger.info("📨 알 수 없는 알림 타입: ${notification.type}")
            }
        }
    }
    
    private fun broadcastMessage(message: String) {
        Bukkit.getOnlinePlayers().forEach { player ->
            player.sendMessage(message)
        }
    }
    
    private fun handleCors(exchange: HttpExchange) {
        exchange.responseHeaders.add("Access-Control-Allow-Origin", "*")
        exchange.responseHeaders.add("Access-Control-Allow-Methods", "POST, OPTIONS")
        exchange.responseHeaders.add("Access-Control-Allow-Headers", "Content-Type")
        sendResponse(exchange, 200, "")
    }
}

// 데이터 클래스들
@Serializable
data class ChzzkAuthWebhook(
    val userId: String,
    val username: String,
    val minecraftUuid: String,
    val authToken: String,
    val timestamp: Long
)

@Serializable
data class ChzzkNotificationWebhook(
    val type: String, // STREAM_START, DONATION, FOLLOW, etc.
    val streamerName: String? = null,
    val donorName: String? = null,
    val followerName: String? = null,
    val amount: Int? = null,
    val message: String? = null,
    val timestamp: Long
)