package com.todo.todos

import com.todo.database.DB
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID


@Serializable
data class ExposedTodos(
    val task: String,
)

class TodoRepository() {
    private val database = DB.getDB()

    object Todo: Table() {
        val id = uuid("id").default(UUID.randomUUID())
        val task = varchar("todo", length = 100)

        override val primaryKey = PrimaryKey(id)
    }

    init {
        transaction(database) {
            SchemaUtils.create(Todo)
        }
    }

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    suspend fun create(todo: ExposedTodos): UUID = dbQuery {
        Todo.insert {
            it[task] = todo.task
        } [Todo.id]
    }

    // The ? after ExposedTodos means that this function can return a nullable value
    suspend fun get(id: UUID): ExposedTodos? {
        return dbQuery {
            Todo.select { Todo.id eq id}
                .map { ExposedTodos(it[Todo.task]) }
                .singleOrNull()
        }
    }

    suspend fun update(id: UUID, todo: ExposedTodos) {}

    suspend fun delete(id: UUID) {}




}