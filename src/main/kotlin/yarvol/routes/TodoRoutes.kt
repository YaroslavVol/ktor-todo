package yarvol.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import yarvol.dao.dao
import yarvol.models.Todo

fun Route.todoRouting() {
    route("/todo") {

        get {
            call.respond(mapOf("todos" to dao.selectAll()))
        }

        get("{id?}") {
            val id = call.parameters["id"] ?: return@get call.respondText(
                "Missing id",
                status = HttpStatusCode.BadRequest
            )
            val todo = dao.select(id.toInt()) ?: return@get call.respondText(
                "No todo with id $id",
                status = HttpStatusCode.NotFound
            )
            call.respond(todo)
        }

        post {
            val todo = call.receive<Todo>()
            dao.insert(todo)
            call.respondText("Todo stored correctly", status = HttpStatusCode.Created)
        }

        put("{id?}") {
            val id = call.parameters["id"] ?: return@put call.respond(HttpStatusCode.BadRequest)
            val todo = call.receive<Todo>()
            if (dao.edit(id.toInt(), todo))  {
                call.respondText("Todo edit correctly", status = HttpStatusCode.Accepted)
            } else {
                call.respondText("Not found", status = HttpStatusCode.NotFound)
            }
        }

        delete("{id?}") {
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
            if (dao.delete(id.toInt())) {
                call.respondText("Todo removed correctly", status = HttpStatusCode.Accepted)
            } else {
                call.respondText("Not found", status = HttpStatusCode.NotFound)
            }
        }
    }
}