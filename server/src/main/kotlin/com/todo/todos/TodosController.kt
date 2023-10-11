package com.todo.todos

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureTodoController() {
    val service = TodoService()

    routing {
        post("/todo") {
            val todo = call.receive<ExposedTodos>()
            val id = service.create(todo)
            call.respond(HttpStatusCode.Created, id)
        }
    }
}