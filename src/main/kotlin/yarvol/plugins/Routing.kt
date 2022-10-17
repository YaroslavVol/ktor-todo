package yarvol.plugins

import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import yarvol.routes.todoRouting

fun Application.configureRouting() {

//     Starting point for a Ktor app:
    routing {
        get("/") {
            call.respondText("Hello, ToDo!")
        }
    }
    routing {
        todoRouting()
    }
    routing {
        static("/static") {
            resources("files")
        }
    }
}