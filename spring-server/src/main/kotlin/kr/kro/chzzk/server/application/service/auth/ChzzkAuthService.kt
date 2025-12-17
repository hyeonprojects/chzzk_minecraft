package kr.kro.chzzk.server.application.service.auth

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ChzzkAuthService {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    /**
     * 치지직 소셜 로그인 리다이렉트시 토큰 발급 및 저장하는 기능
     */
    fun handleSocialLoginRedirect(code: String, state: String) {
        logger.info("Handling social login redirect with code: $code and state: $state")

        // TODO: 치지직 API 호출 기능 구현

        // TODO: 토큰 발급 및 저장 기능 구현
    }
}