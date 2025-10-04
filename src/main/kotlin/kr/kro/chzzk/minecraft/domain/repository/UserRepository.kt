package kr.kro.chzzk.minecraft.domain.repository

import kr.kro.chzzk.minecraft.application.dto.User
import java.util.*

interface UserRepository {
    
    suspend fun save(user: User): User
    
    suspend fun update(user: User): User?
    
    suspend fun delete(id: Int): Boolean
    
    suspend fun findById(id: Int): User?
    
    suspend fun findByMinecraftUuid(uuid: UUID): User?
    
    suspend fun findByMinecraftName(name: String): User?
    
    suspend fun findByChzzkId(chzzkId: String): User?
    
    suspend fun findByChzzkName(chzzkName: String): List<User>
    
    suspend fun findByMinecraftNameContaining(name: String): List<User>
    
    suspend fun findByChzzkNameContaining(name: String): List<User>
    
    suspend fun findAll(): List<User>
    
    suspend fun count(): Long
    
    suspend fun existsByMinecraftUuid(uuid: UUID): Boolean
    
    suspend fun existsByChzzkId(chzzkId: String): Boolean
}