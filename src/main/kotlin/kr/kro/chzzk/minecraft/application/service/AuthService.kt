package kr.kro.chzzk.minecraft.application.service

import kr.kro.chzzk.minecraft.application.dto.User
import kr.kro.chzzk.minecraft.application.dto.ChzzkProfile
import kr.kro.chzzk.minecraft.domain.repository.UserRepository
import kr.kro.chzzk.minecraft.domain.repository.ChzzkApiRepository
import kr.kro.chzzk.minecraft.domain.exception.UserNotFoundException
import kr.kro.chzzk.minecraft.domain.exception.LinkageException
import java.util.*

/**
 * 인증 애플리케이션 서비스
 * 
 * 마인크래프트 계정과 치지직 계정 간의 연결/해제에 대한 애플리케이션 로직을 담당합니다.
 * 도메인 서비스와 인프라스트럭처 레이어를 조합하여 복잡한 비즈니스 유스케이스를 구현합니다.
 * 
 * 주요 기능:
 * - 계정 연결 및 해제
 * - 연결 상태 확인
 * - 치지직 프로필 정보 갱신
 * - 중복 연결 방지
 * 
 * @param userRepository 사용자 데이터 접근 레포지토리
 * @param chzzkApiRepository 치지직 API 접근 레포지토리
 * @author Hyeonprojects
 * @since 1.0
 */
class AuthService(
    private val userRepository: UserRepository,
    private val chzzkApiRepository: ChzzkApiRepository
) {
    
    /**
     * 마인크래프트 계정과 치지직 계정을 연결합니다.
     * 
     * @param minecraftUuid 마인크래프트 플레이어 UUID
     * @param minecraftName 마인크래프트 플레이어 이름
     * @param authCode 치지직 인증 코드
     * @return User 생성된 사용자 정보
     * @throws LinkageException 연결 과정에서 오류 발생 시
     */
    suspend fun linkAccount(minecraftUuid: UUID, minecraftName: String, authCode: String): User {
        // 기존 연결 확인
        userRepository.findByMinecraftUuid(minecraftUuid)?.let {
            throw LinkageException("이미 연결된 계정입니다.")
        }
        
        // 치지직 API로 프로필 조회
        val chzzkProfile = chzzkApiRepository.authenticateWithCode(authCode)
            ?: throw LinkageException("유효하지 않은 인증 코드입니다.")
        
        // 이미 다른 마인크래프트 계정과 연결된 치지직 계정인지 확인
        userRepository.findByChzzkId(chzzkProfile.chzzkId)?.let {
            throw LinkageException("이미 다른 마인크래프트 계정과 연결된 치지직 계정입니다.")
        }
        
        // 새 사용자 생성
        val newUser = User(
            id = 0, // Auto-increment
            minecraftUuid = minecraftUuid,
            minecraftName = minecraftName,
            chzzkId = chzzkProfile.chzzkId,
            chzzkName = chzzkProfile.chzzkName,
            chzzkDevCode = authCode,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        
        return userRepository.save(newUser)
    }
    
    /**
     * 마인크래프트 계정의 치지직 연결을 해제합니다.
     * 
     * @param minecraftUuid 마인크래프트 플레이어 UUID
     * @return Boolean 성공 시 true, 실패 시 false
     * @throws UserNotFoundException 연결된 계정을 찾을 수 없는 경우
     */
    suspend fun unlinkAccount(minecraftUuid: UUID): Boolean {
        val user = userRepository.findByMinecraftUuid(minecraftUuid)
            ?: throw UserNotFoundException("연결된 계정을 찾을 수 없습니다.")
        
        return userRepository.delete(user.id)
    }
    
    /**
     * 연결된 계정 정보를 조회합니다.
     * 
     * @param minecraftUuid 마인크래프트 플레이어 UUID
     * @return User? 연결된 사용자 정보, 없으면 null
     */
    suspend fun getLinkedAccount(minecraftUuid: UUID): User? {
        return userRepository.findByMinecraftUuid(minecraftUuid)
    }
    
    /**
     * 계정이 연결되어 있는지 확인합니다.
     * 
     * @param minecraftUuid 마인크래프트 플레이어 UUID
     * @return Boolean 연결되어 있으면 true, 아니면 false
     */
    suspend fun isLinked(minecraftUuid: UUID): Boolean {
        return userRepository.findByMinecraftUuid(minecraftUuid) != null
    }
    
    /**
     * 치지직 프로필 정보를 최신 상태로 갱신합니다.
     * 
     * @param minecraftUuid 마인크래프트 플레이어 UUID
     * @return User? 갱신된 사용자 정보, 갱신 실패 시 기존 정보
     */
    suspend fun refreshChzzkProfile(minecraftUuid: UUID): User? {
        val user = userRepository.findByMinecraftUuid(minecraftUuid)
            ?: return null
        
        val updatedProfile = chzzkApiRepository.getProfile(user.chzzkId)
            ?: return user
        
        val updatedUser = user.copy(
            chzzkName = updatedProfile.chzzkName,
            updatedAt = System.currentTimeMillis()
        )
        
        return userRepository.update(updatedUser)
    }
}