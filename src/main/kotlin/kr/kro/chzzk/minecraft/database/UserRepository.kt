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
        // TODO: Exposed 라이브러리 설정 후 구현
        // return transaction(databaseManager.getDatabase()) {
        //     val insertedId = UserTable.insert { row ->
        //         row[minecraftUuid] = user.minecraftUuid.toString()
        //         row[minecraftName] = user.minecraftName
        //         row[chzzkId] = user.chzzkId
        //         row[chzzkName] = user.chzzkName
        //         row[chzzkDevCode] = user.chzzkDevCode
        //         row[createdAt] = user.createdAt
        //         row[updatedAt] = user.updatedAt
        //     } get UserTable.id
        //     
        //     user.copy(id = insertedId)
        // }
        return user
    }
    
    fun findByMinecraftUuid(uuid: UUID): User? {
        // TODO: Exposed 라이브러리 설정 후 구현
        // return transaction(databaseManager.getDatabase()) {
        //     UserTable.select { UserTable.minecraftUuid eq uuid.toString() }
        //         .singleOrNull()
        //         ?.toUser()
        // }
        return null
    }
    
    fun findByChzzkId(chzzkId: String): User? {
        // TODO: Exposed 라이브러리 설정 후 구현
        // return transaction(databaseManager.getDatabase()) {
        //     UserTable.select { UserTable.chzzkId eq chzzkId }
        //         .singleOrNull()
        //         ?.toUser()
        // }
        return null
    }
    
    fun deleteById(id: Int): Boolean {
        // TODO: Exposed 라이브러리 설정 후 구현
        // return transaction(databaseManager.getDatabase()) {
        //     UserTable.deleteWhere { UserTable.id eq id } > 0
        // }
        return false
    }
    
    fun findAll(): List<User> {
        // TODO: Exposed 라이브러리 설정 후 구현
        // return transaction(databaseManager.getDatabase()) {
        //     UserTable.selectAll().map { it.toUser() }
        // }
        return emptyList()
    }
    
    fun update(user: User): User? {
        // TODO: Exposed 라이브러리 설정 후 구현
        // return transaction(databaseManager.getDatabase()) {
        //     val updatedRows = UserTable.update({ UserTable.id eq user.id }) { row ->
        //         row[minecraftName] = user.minecraftName
        //         row[chzzkName] = user.chzzkName
        //         row[updatedAt] = System.currentTimeMillis()
        //     }
        //     
        //     if (updatedRows > 0) user else null
        // }
        return null
    }
    
    // TODO: Exposed 라이브러리 설정 후 구현
    // private fun ResultRow.toUser(): User {
    //     return User(
    //         id = this[UserTable.id],
    //         minecraftUuid = UUID.fromString(this[UserTable.minecraftUuid]),
    //         minecraftName = this[UserTable.minecraftName],
    //         chzzkId = this[UserTable.chzzkId],
    //         chzzkName = this[UserTable.chzzkName],
    //         chzzkDevCode = this[UserTable.chzzkDevCode],
    //         createdAt = this[UserTable.createdAt],
    //         updatedAt = this[UserTable.updatedAt]
    //     )
    // }
}