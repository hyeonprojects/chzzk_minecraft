package kr.kro.chzzk.minecraft.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kr.kro.chzzk.minecraft.model.ChzzkChannelResponse
import kr.kro.chzzk.minecraft.model.ChzzkTokenResponse
import kr.kro.chzzk.minecraft.model.ChzzkErrorResponse
import java.io.IOException
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration

/**
 * 치지직 API 클라이언트
 * GET과 POST 요청 예제를 포함한 간단한 API 클라이언트 (JDK 기본 라이브러리 사용)
 */
class ChzzkApiClient {

    private val baseUrl = "https://api.chzzk.naver.com"
    
    private val httpClient = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(30))
        .build()

    /**
     * GET 요청 예제: 채널 정보 조회
     * @param channelId 채널 ID
     * @param accessToken 액세스 토큰 (선택적)
     * @return 채널 정보 또는 null
     */
    suspend fun getChannelInfo(channelId: String, accessToken: String? = null): ChzzkChannelResponse? {
        return withContext(Dispatchers.IO) {
            try {
                val requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create("$baseUrl/service/v1/channels/$channelId"))
                    .timeout(Duration.ofSeconds(30))
                    .GET()
                
                // 액세스 토큰이 있으면 헤더에 추가
                accessToken?.let { token ->
                    requestBuilder.header("Authorization", "Bearer $token")
                }
                
                val request = requestBuilder.build()
                val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())
                
                if (response.statusCode() == 200) {
                    val responseBody = response.body()
                    
                    // 간단한 JSON 파싱 (기본 라이브러리 사용)
                    parseChannelResponse(responseBody, channelId)
                } else {
                    println("채널 정보 조회 실패: ${response.statusCode()}")
                    null
                }
            } catch (e: IOException) {
                println("채널 정보 조회 중 네트워크 오류: ${e.message}")
                null
            } catch (e: Exception) {
                println("채널 정보 조회 중 오류: ${e.message}")
                null
            }
        }
    }

    /**
     * POST 요청 예제: OAuth 액세스 토큰 요청
     * @param authCode 인증 코드
     * @param clientId 클라이언트 ID
     * @param clientSecret 클라이언트 시크릿
     * @return 토큰 정보 또는 null
     */
    suspend fun requestAccessToken(
        authCode: String,
        clientId: String,
        clientSecret: String
    ): ChzzkTokenResponse? {
        return withContext(Dispatchers.IO) {
            try {
                // POST 요청 본문 데이터 (JSON 문자열로 직접 작성)
                val requestBody = """
                    {
                        "grant_type": "authorization_code",
                        "code": "$authCode",
                        "client_id": "$clientId",
                        "client_secret": "$clientSecret"
                    }
                """.trimIndent()
                
                val request = HttpRequest.newBuilder()
                    .uri(URI.create("$baseUrl/oauth/token"))
                    .timeout(Duration.ofSeconds(30))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build()
                
                val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())
                
                if (response.statusCode() == 200) {
                    val responseBody = response.body()
                    
                    // 간단한 JSON 파싱으로 토큰 응답 처리
                    parseTokenResponse(responseBody)
                } else {
                    println("토큰 요청 실패: ${response.statusCode()}")
                    null
                }
            } catch (e: IOException) {
                println("토큰 요청 중 네트워크 오류: ${e.message}")
                null
            } catch (e: Exception) {
                println("토큰 요청 중 오류: ${e.message}")
                null
            }
        }
    }
    
    /**
     * 채널 응답 JSON 파싱 (기본 라이브러리 사용)
     */
    private fun parseChannelResponse(json: String, channelId: String): ChzzkChannelResponse? {
        return try {
            // 간단한 정규식을 사용한 JSON 파싱
            val channelNameRegex = "\"channelName\"\\s*:\\s*\"([^\"]+)\"".toRegex()
            val channelImageRegex = "\"channelImageUrl\"\\s*:\\s*\"([^\"]+)\"".toRegex()
            val verifiedMarkRegex = "\"verifiedMark\"\\s*:\\s*(true|false)".toRegex()
            
            val channelName = channelNameRegex.find(json)?.groupValues?.get(1) ?: "Unknown"
            val channelImageUrl = channelImageRegex.find(json)?.groupValues?.get(1)
            val verifiedMark = verifiedMarkRegex.find(json)?.groupValues?.get(1)?.toBoolean() ?: false
            
            ChzzkChannelResponse(
                channelId = channelId,
                channelName = channelName,
                channelImageUrl = channelImageUrl,
                verifiedMark = verifiedMark
            )
        } catch (e: Exception) {
            println("채널 응답 파싱 오류: ${e.message}")
            null
        }
    }
    
    /**
     * 토큰 응답 JSON 파싱 (기본 라이브러리 사용)
     */
    private fun parseTokenResponse(json: String): ChzzkTokenResponse? {
        return try {
            // 간단한 정규식을 사용한 JSON 파싱
            val accessTokenRegex = "\"access_token\"\\s*:\\s*\"([^\"]+)\"".toRegex()
            val refreshTokenRegex = "\"refresh_token\"\\s*:\\s*\"([^\"]+)\"".toRegex()
            val tokenTypeRegex = "\"token_type\"\\s*:\\s*\"([^\"]+)\"".toRegex()
            val expiresInRegex = "\"expires_in\"\\s*:\\s*(\\d+)".toRegex()
            
            val accessToken = accessTokenRegex.find(json)?.groupValues?.get(1) ?: return null
            val refreshToken = refreshTokenRegex.find(json)?.groupValues?.get(1) ?: return null
            val tokenType = tokenTypeRegex.find(json)?.groupValues?.get(1) ?: "Bearer"
            val expiresIn = expiresInRegex.find(json)?.groupValues?.get(1)?.toLongOrNull() ?: 3600L
            
            ChzzkTokenResponse(
                accessToken = accessToken,
                refreshToken = refreshToken,
                tokenType = tokenType,
                expiresIn = expiresIn
            )
        } catch (e: Exception) {
            println("토큰 응답 파싱 오류: ${e.message}")
            null
        }
    }
}
