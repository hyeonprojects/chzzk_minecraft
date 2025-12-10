package kr.kro.chzzk.minecraft.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kr.kro.chzzk.minecraft.dto.*
import kr.kro.chzzk.minecraft.enum.ChzzkAuthGrandType
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

    private val baseUrl = "https://chzzk.naver.com"
    
    private val httpClient = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(30))
        .build()


    /**
     * 치지직 Access Token 발급 요청
     * @param requestBody 요청 본문 데이터
     * @return 토큰 응답 또는 null
     * 필요한 토큰이 발급됨
     */
    suspend fun getChzzkAccessToken(requestBody: ChzzkTokenRequest): ChzzkTokenResponse? {
        if (requestBody.grantType === ChzzkAuthGrandType.AUTH ||
            requestBody.clientId.isBlank() ||
            requestBody.clientSecret.isBlank() ||
            requestBody.code?.isBlank() == true ||
            requestBody.state?.isBlank() == true
        ) {
            throw IllegalArgumentException("필수 파라미터가 누락되었습니다.")
        }

        return withContext(Dispatchers.IO) {
            try {
                val url = "${baseUrl}/auth/v1/token"
                val request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(30))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(Json.encodeToString(requestBody)))
                    .build()

                val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())

                // 상태 코드가 200인 경우에만 처리
                if (response.statusCode() == 200) {
                    val responseBody = Json.decodeFromString<ChzzkTokenResponse>(response.body())
                    responseBody
                } else {
                    println("Access Token 발급 요청 실패: ${response.statusCode()}")
                    null
                }
            } catch (e: IOException) {
                println("Access Token 발급 요청 중 네트워크 오류: ${e.message}")
                null
            } catch (e: Exception) {
                println("Access Token 발급 요청 중 오류: ${e.message}")
                null
            }
        }
    }

    /**
     *
     */
    suspend fun getRefreshChzzkAccessToken(requsetBody: ChzzkTokenRequest): ChzzkTokenResponse? {
        if (requsetBody.grantType === ChzzkAuthGrandType.REFRESH ||
            requsetBody.clientId.isBlank() ||
            requsetBody.clientSecret.isBlank() ||
            requsetBody.refreshToken?.isBlank() == true
        ) {
            throw IllegalArgumentException("필수 파라미터가 누락되었습니다.")
        }

        return withContext(Dispatchers.IO) {
            try {
                val url = "${baseUrl}/auth/v1/token"
                val request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(30))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(Json.encodeToString(requsetBody)))
                    .build()

                val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())

                // 상태 코드가 200인 경우에만 처리
                if (response.statusCode() == 200) {
                    val responseBody = Json.decodeFromString<ChzzkTokenResponse>(response.body())
                    responseBody
                } else {
                    println("Access Token 발급 요청 실패: ${response.statusCode()}")
                    null
                }
            } catch (e: IOException) {
                println("Access Token 발급 요청 중 네트워크 오류: ${e.message}")
                null
            } catch (e: Exception) {
                println("Access Token 발급 요청 중 오류: ${e.message}")
                null
            }
        }
    }

    /**
     * 치지직 토큰 폐기 요청
     */
    suspend fun revokeChzzkToken(requestBody: RevokeTokenRequest): Boolean? {
        if (requestBody.clientId.isBlank() ||
            requestBody.clientSecret.isBlank() ||
            requestBody.token.isBlank()
        ) {
            throw IllegalArgumentException("필수 파라미터가 누락되었습니다.")
        }

        return withContext(Dispatchers.IO) {
            try {
                val url = "${baseUrl}/auth/v1/token/revoke"
                val request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(30))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(Json.encodeToString(requestBody)))
                    .build()

                val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())

                // 상태 코드가 200인 경우에만 처리
                if (response.statusCode() == 200) {
                    true
                } else {
                    println("토큰 폐기 요청 실패: ${response.statusCode()}")
                    false
                }
            } catch (e: IOException) {
                println("토큰 폐기 요청 중 네트워크 오류: ${e.message}")
                null
            } catch (e: Exception) {
                println("토큰 폐기 요청 중 오류: ${e.message}")
                null
            }
        }
    }
}
