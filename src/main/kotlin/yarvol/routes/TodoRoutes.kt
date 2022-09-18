package yarvol.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import yarvol.models.Todo
import yarvol.models.todoStorage

fun Route.todoRouting() {
    route("/todo") {
        get {
            if (todoStorage.isNotEmpty()) {
                call.respond(todoStorage)
            } else {
                call.respondText("No todo list", status = HttpStatusCode.OK)
            }
        }
        get("{id?}") {
            val id = call.parameters["id"] ?: return@get call.respondText(
                "Missing id",
                status = HttpStatusCode.BadRequest
            )
            val todo =
                todoStorage.find { it.id == id } ?: return@get call.respondText(
                    "No todo with id $id",
                    status = HttpStatusCode.NotFound
                )
            call.respond(todoStorage)
        }
        post {
            val todo = call.receive<Todo>()
            todoStorage.add(todo)
            call.respondText("Todo stored correctly", status = HttpStatusCode.Created)

        }
        delete("{id?}") {
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
            if (todoStorage.removeIf { it.id == id }) {
                call.respondText("Customer removed correctly", status = HttpStatusCode.Accepted)
            } else {
                call.respondText("Not found", status = HttpStatusCode.NotFound)
            }
        }
    }
}