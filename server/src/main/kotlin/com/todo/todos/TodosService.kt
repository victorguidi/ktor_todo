package com.todo.todos

import java.lang.Exception
import java.util.UUID

class TodoService() {
    private val repository = TodoRepository()

    suspend fun create(todo: ExposedTodos): Result<UUID> {
        val result: Result<UUID> = try {
            val uuid = this.repository.create(todo)
            Result.success(uuid)
        } catch (e: Exception) {
            throw IllegalArgumentException("Error occured")
        }
        return result
    }
}