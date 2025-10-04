package kr.kro.chzzk.minecraft.infrastructure.database.repository

import kr.kro.chzzk.minecraft.application.dto.User
import kr.kro.chzzk.minecraft.domain.repository.UserRepository
import kr.kro.chzzk.minecraft.infrastructure.database.table.ChzzkUserTable
import kr.kro.chzzk.minecraft.infrastructure.database.connection.DatabaseConnectionManager
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class UserRepositoryImpl(
    private val databaseManager: DatabaseConnectionManager
) : UserRepository {
    
    override suspend fun save(user: User): User {
        return transaction(databaseManager.getDatabase()) {
            val insertedId = ChzzkUserTable.insert { row ->
                row[minecraftUuid] = user.minecraftUuid.toString()
                row[minecraftName] = user.minecraftName
                row[chzzkId] = user.chzzkId
                row[chzzkName] = user.chzzkName
                row[chzzkDevCode] = user.chzzkDevCode
                row[createdAt] = user.createdAt
                row[updatedAt] = user.updatedAt
            } get ChzzkUserTable.id
            
            user.copy(id = insertedId)
        }
    }
    
    override suspend fun update(user: User): User? {
        return transaction(databaseManager.getDatabase()) {
            val updatedRows = ChzzkUserTable.update({ ChzzkUserTable.id eq user.id }) { row ->
                row[minecraftName] = user.minecraftName
                row[chzzkName] = user.chzzkName
                row[chzzkDevCode] = user.chzzkDevCode
                row[updatedAt] = System.currentTimeMillis()
            }
            
            if (updatedRows > 0) user else null
        }
    }
    
    override suspend fun delete(id: Int): Boolean {
        return transaction(databaseManager.getDatabase()) {
            ChzzkUserTable.deleteWhere { ChzzkUserTable.id eq id } > 0
        }
    }
    
    override suspend fun findById(id: Int): User? {
        return transaction(databaseManager.getDatabase()) {
            ChzzkUserTable.select { ChzzkUserTable.id eq id }
                .singleOrNull()
                ?.toUser()
        }
    }
    
    override suspend fun findByMinecraftUuid(uuid: UUID): User? {
        return transaction(databaseManager.getDatabase()) {
            ChzzkUserTable.select { ChzzkUserTable.minecraftUuid eq uuid.toString() }
                .singleOrNull()
                ?.toUser()
        }
    }
    
    override suspend fun findByMinecraftName(name: String): User? {
        return transaction(databaseManager.getDatabase()) {
            ChzzkUserTable.select { ChzzkUserTable.minecraftName eq name }
                .singleOrNull()
                ?.toUser()
        }
    }
    
    override suspend fun findByChzzkId(chzzkId: String): User? {
        return transaction(databaseManager.getDatabase()) {
            ChzzkUserTable.select { ChzzkUserTable.chzzkId eq chzzkId }
                .singleOrNull()
                ?.toUser()
        }
    }
    
    override suspend fun findByChzzkName(chzzkName: String): List<User> {
        return transaction(databaseManager.getDatabase()) {
            ChzzkUserTable.select { ChzzkUserTable.chzzkName eq chzzkName }
                .map { it.toUser() }
        }
    }
    
    override suspend fun findByMinecraftNameContaining(name: String): List<User> {
        return transaction(databaseManager.getDatabase()) {
            ChzzkUserTable.select { ChzzkUserTable.minecraftName like "%$name%" }
                .map { it.toUser() }
        }
    }
    
    override suspend fun findByChzzkNameContaining(name: String): List<User> {
        return transaction(databaseManager.getDatabase()) {
            ChzzkUserTable.select { ChzzkUserTable.chzzkName like "%$name%" }
                .map { it.toUser() }
        }
    }
    
    override suspend fun findAll(): List<User> {
        return transaction(databaseManager.getDatabase()) {
            ChzzkUserTable.selectAll().map { it.toUser() }
        }
    }
    
    override suspend fun count(): Long {
        return transaction(databaseManager.getDatabase()) {
            ChzzkUserTable.selectAll().count()
        }
    }
    
    override suspend fun existsByMinecraftUuid(uuid: UUID): Boolean {
        return transaction(databaseManager.getDatabase()) {
            ChzzkUserTable.select { ChzzkUserTable.minecraftUuid eq uuid.toString() }
                .count() > 0
        }
    }
    
    override suspend fun existsByChzzkId(chzzkId: String): Boolean {
        return transaction(databaseManager.getDatabase()) {
            ChzzkUserTable.select { ChzzkUserTable.chzzkId eq chzzkId }
                .count() > 0
        }
    }
    
    private fun ResultRow.toUser(): User {
        return User(
            id = this[ChzzkUserTable.id],
            minecraftUuid = UUID.fromString(this[ChzzkUserTable.minecraftUuid]),
            minecraftName = this[ChzzkUserTable.minecraftName],
            chzzkId = this[ChzzkUserTable.chzzkId],
            chzzkName = this[ChzzkUserTable.chzzkName],
            chzzkDevCode = this[ChzzkUserTable.chzzkDevCode],
            createdAt = this[ChzzkUserTable.createdAt],
            updatedAt = this[ChzzkUserTable.updatedAt]
        )
    }
}