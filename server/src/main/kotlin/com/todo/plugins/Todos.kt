package com.todo.plugins

import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

@Serializable
data class ExposedTodos(val todo: String, val todos: ArrayList<String>)

class TodosService(private val database: Database) {
    object Todos: Table() {
        val id = integer("id").autoIncrement()
        val todo = varchar("todo", length = 255)
        val todos = emptyArray<Todos>()

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
}