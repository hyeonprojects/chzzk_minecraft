package kr.kro.chzzk.server.presentation.controller

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * 인증 관련 컨트롤러
 * 치지직 로그인 토큰 확인, 회원가입 등
 */
@RequestMapping(value = ["v1/auth"])
@RestController
class ChzzkAuthController {
    val log: Logger = LoggerFactory.getLogger(this::class.java)

    /**
     * 치지직 소셜 로그인 리다이렉트 기능
     */
    @PostMapping(value = ["/redirect"])
    suspend fun redirect() {
        log.info("Auth redirect called")
    }

    @PostMapping(value = ["/register"])
    suspend fun register() {
        log.info("Auth register called")
    }
}