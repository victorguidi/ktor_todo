package com.todo.plugins

import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

@Serializable
data class ExposedTodos(val todo: String)

class TodosService(private val database: Database) {
    object Todos: Table() {
        val id = integer("id").autoIncrement()
        val todo = varchar("todo", length = 255)
//        val todos = emptyArray<Todos>()

        override val primaryKey = PrimaryKey(id)
    }

    init { // This function when the class is initialized?
        transaction(database) {
            SchemaUtils.create(Todos)
        }
    }

    // Suspend means that the function can be blocking, it will lock the couritine that is runnign
    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    suspend fun create(todos: ExposedTodos): Int = dbQuery {
        Todos.insert {
            it[todo] = todos.todo
//            it[todos] = todos.todos
        }[Todos.id]
    }

    suspend fun read(id: Int): ExposedTodos? {
        return dbQuery {
            Todos.select { Todos.id eq id}
                .map { ExposedTodos(it[Todos.todo]) }
                .singleOrNull()
        }
    }

    suspend fun update(id: Int, todos: ExposedTodos) {
        dbQuery {
            Todos.update({ Todos.id eq id}) {
                it[todo] = todos.todo
            }
        }
    }

    suspend fun delete(id: Int) {
        dbQuery {
            Todos.deleteWhere { Todos.id.eq(id) }
        }
    }

}