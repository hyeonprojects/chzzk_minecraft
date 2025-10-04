package kr.kro.chzzk.minecraft.api

import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.net.URI
import java.time.Duration

/**
 * 치지직 API 클라이언트
 */
class ChzzkApiClient {
    
    private val baseUrl = "https://api.chzzk.naver.com"
    private val httpClient = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(30))
        .build()
    
    /**
     * 인증 코드로 치지직 프로필 정보 조회
     */
    fun getProfileByAuthCode(authCode: String): ChzzkProfile? {
        return try {
            val request = HttpRequest.newBuilder()
                .uri(URI.create("$baseUrl/service/v1/channels/user"))
                .header("Authorization", "Bearer $authCode")
                .header("Content-Type", "application/json")
                .GET()
                .timeout(Duration.ofSeconds(30))
                .build()
            
            val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())
            
            if (response.statusCode() == 200) {
                parseChzzkProfile(response.body())
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
    
    /**
     * 치지직 ID로 프로필 정보 조회
     */
    fun getProfile(chzzkId: String): ChzzkProfile? {
        return try {
            val request = HttpRequest.newBuilder()
                .uri(URI.create("$baseUrl/service/v1/channels/$chzzkId"))
                .header("Content-Type", "application/json")
                .GET()
                .timeout(Duration.ofSeconds(30))
                .build()
            
            val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())
            
            if (response.statusCode() == 200) {
                parseChzzkProfile(response.body())
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
    
    private fun parseChzzkProfile(responseBody: String): ChzzkProfile? {
        return try {
            // TODO: JSON 파싱 라이브러리를 사용하여 실제 파싱 구현
            // 현재는 임시 구현
            ChzzkProfile(
                chzzkId = "temp_id",
                chzzkName = "temp_name",
                profileImageUrl = null,
                isVerified = false,
                followerCount = 0
            )
        } catch (e: Exception) {
            null
        }
    }
}

/**
 * 치지직 프로필 데이터 클래스
 */
data class ChzzkProfile(
    val chzzkId: String,
    val chzzkName: String,
    val profileImageUrl: String?,
    val isVerified: Boolean,
    val followerCount: Int
)