package kr.kro.chzzk.minecraft.domain.repository

import kr.kro.chzzk.minecraft.application.dto.ChzzkProfile

interface ChzzkApiRepository {
    
    suspend fun authenticateWithCode(authCode: String): ChzzkProfile?
    
    suspend fun getProfile(chzzkId: String): ChzzkProfile?
    
    suspend fun validateAuthCode(authCode: String): Boolean
    
    suspend fun refreshToken(refreshToken: String): String?
    
    suspend fun revokeAccess(chzzkId: String): Boolean
}