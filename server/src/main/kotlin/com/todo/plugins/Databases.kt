package com.todo.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.*

fun Application.configureDatabases() {
    val database = Database.connect(
            url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
            //url = "jdbc:postgres:mem:test;DB_CLOSE_DELAY=-1",
            user = "root",
            driver = "org.h2.Driver",
            password = ""
        )

    val userService = UserService(database)
    val todoService = TodosService(database)

    routing {
        // Create user
        post("/users") {
            val user = call.receive<ExposedUser>()
            val id = userService.create(user)
            call.respond(HttpStatusCode.Created, id)
        }
        // Read user
        get("/users/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            val user = userService.read(id)
            if (user != null) {
                call.respond(HttpStatusCode.OK, user)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
        // Update user
        put("/users/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            val user = call.receive<ExposedUser>()
            userService.update(id, user)
            call.respond(HttpStatusCode.OK)
        }
        // Delete user
        delete("/users/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            userService.delete(id)
            call.respond(HttpStatusCode.OK)
        }

        // Create a todo
//        post("/todo") {
//            val todo = call.receive<ExposedTodos>()
//            val id = todoService.create(todo)
//            call.respond(HttpStatusCode.Created, id)
//        }

        // Get a todo
        get("/todo/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            val todo = todoService.read(id)
            if (todo != null) {
                call.respond(HttpStatusCode.OK, todo)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        // Update todo
        put("/todo/{id}") {
            val id  = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            val todo = call.receive<ExposedTodos>()
            todoService.update(id, todo)
            call.respond(HttpStatusCode.OK)
        }

        // Delete todo
        delete("/todo/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            todoService.delete(id)
            call.respond(HttpStatusCode.OK)
        }
    }
}
