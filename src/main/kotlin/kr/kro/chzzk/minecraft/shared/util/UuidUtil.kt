package kr.kro.chzzk.minecraft.shared.util

import java.util.*

object UuidUtil {
    
    fun fromString(uuidString: String): UUID? {
        return try {
            UUID.fromString(uuidString)
        } catch (e: IllegalArgumentException) {
            null
        }
    }
    
    fun fromStringOrThrow(uuidString: String): UUID {
        return try {
            UUID.fromString(uuidString)
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("유효하지 않은 UUID 형식: $uuidString", e)
        }
    }
    
    fun isValidUuid(uuidString: String): Boolean {
        return try {
            UUID.fromString(uuidString)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }
    
    fun addDashes(uuidString: String): String {
        if (uuidString.length != 32) {
            throw IllegalArgumentException("UUID string must be exactly 32 characters long")
        }
        
        return "${uuidString.substring(0, 8)}-${uuidString.substring(8, 12)}-${uuidString.substring(12, 16)}-${uuidString.substring(16, 20)}-${uuidString.substring(20)}"
    }
    
    fun removeDashes(uuidString: String): String {
        return uuidString.replace("-", "")
    }
    
    fun toMojangUuid(uuid: UUID): String {
        return removeDashes(uuid.toString())
    }
    
    fun fromMojangUuid(mojangUuid: String): UUID {
        return UUID.fromString(addDashes(mojangUuid))
    }
}