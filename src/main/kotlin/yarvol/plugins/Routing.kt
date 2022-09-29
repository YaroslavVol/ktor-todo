package yarvol.plugins

import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import kotlinx.css.h1
import kotlinx.css.title
import kotlinx.html.*
import yarvol.dao.dao
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

