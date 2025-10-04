package kr.kro.chzzk.minecraft.application.service

import kr.kro.chzzk.minecraft.application.dto.User
import kr.kro.chzzk.minecraft.domain.repository.UserRepository
import java.util.*

class UserLinkService(
    private val userRepository: UserRepository
) {
    
    suspend fun getAllLinkedUsers(): List<User> {
        return userRepository.findAll()
    }
    
    suspend fun getUsersByChzzkName(chzzkName: String): List<User> {
        return userRepository.findByChzzkNameContaining(chzzkName)
    }
    
    suspend fun getUsersByMinecraftName(minecraftName: String): List<User> {
        return userRepository.findByMinecraftNameContaining(minecraftName)
    }
    
    suspend fun getLinkageStats(): Map<String, Int> {
        val allUsers = userRepository.findAll()
        val now = System.currentTimeMillis()
        val oneWeekAgo = now - (7 * 24 * 60 * 60 * 1000)
        val oneMonthAgo = now - (30 * 24 * 60 * 60 * 1000)
        
        return mapOf(
            "total" to allUsers.size,
            "thisWeek" to allUsers.count { it.createdAt >= oneWeekAgo },
            "thisMonth" to allUsers.count { it.createdAt >= oneMonthAgo },
            "active" to allUsers.count { it.updatedAt >= oneWeekAgo }
        )
    }
    
    suspend fun updateMinecraftName(minecraftUuid: UUID, newName: String): User? {
        val user = userRepository.findByMinecraftUuid(minecraftUuid)
            ?: return null
        
        val updatedUser = user.copy(
            minecraftName = newName,
            updatedAt = System.currentTimeMillis()
        )
        
        return userRepository.update(updatedUser)
    }
}