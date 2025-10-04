package kr.kro.chzzk.minecraft.database

import kr.kro.chzzk.minecraft.model.User
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

/**
 * 사용자 데이터 접근 클래스
 */
class UserRepository(private val databaseManager: DatabaseManager) {
    
    fun save(user: User): User {
        return transaction(databaseManager.getDatabase()) {
            val insertedId = UserTable.insert { row ->
                row[minecraftUuid] = user.minecraftUuid.toString()
                row[minecraftName] = user.minecraftName
                row[chzzkId] = user.chzzkId
                row[chzzkName] = user.chzzkName
                row[chzzkDevCode] = user.chzzkDevCode
                row[createdAt] = user.createdAt
                row[updatedAt] = user.updatedAt
            } get UserTable.id
            
            user.copy(id = insertedId)
        }
    }
    
    fun findByMinecraftUuid(uuid: UUID): User? {
        return transaction(databaseManager.getDatabase()) {
            UserTable.select { UserTable.minecraftUuid eq uuid.toString() }
                .singleOrNull()
                ?.toUser()
        }
    }
    
    fun findByChzzkId(chzzkId: String): User? {
        return transaction(databaseManager.getDatabase()) {
            UserTable.select { UserTable.chzzkId eq chzzkId }
                .singleOrNull()
                ?.toUser()
        }
    }
    
    fun deleteById(id: Int): Boolean {
        return transaction(databaseManager.getDatabase()) {
            UserTable.deleteWhere { UserTable.id eq id } > 0
        }
    }
    
    fun findAll(): List<User> {
        return transaction(databaseManager.getDatabase()) {
            UserTable.selectAll().map { it.toUser() }
        }
    }
    
    fun update(user: User): User? {
        return transaction(databaseManager.getDatabase()) {
            val updatedRows = UserTable.update({ UserTable.id eq user.id }) { row ->
                row[minecraftName] = user.minecraftName
                row[chzzkName] = user.chzzkName
                row[updatedAt] = System.currentTimeMillis()
            }
            
            if (updatedRows > 0) user else null
        }
    }
    
    private fun ResultRow.toUser(): User {
        return User(
            id = this[UserTable.id],
            minecraftUuid = UUID.fromString(this[UserTable.minecraftUuid]),
            minecraftName = this[UserTable.minecraftName],
            chzzkId = this[UserTable.chzzkId],
            chzzkName = this[UserTable.chzzkName],
            chzzkDevCode = this[UserTable.chzzkDevCode],
            createdAt = this[UserTable.createdAt],
            updatedAt = this[UserTable.updatedAt]
        )
    }
}