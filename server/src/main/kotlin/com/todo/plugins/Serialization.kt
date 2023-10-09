package com.todo.plugins

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

// This is just a example on how to organize your application, in this case, I could have done this on the main Application.kt
fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json()
    }
}
