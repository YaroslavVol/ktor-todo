package yarvol.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import yarvol.dao.daoTodo
import yarvol.models.Todo

fun Route.todoRouting() {
    // http route
    route("/todo") {
        get {
            call.respond(mapOf("todos" to daoTodo.selectAll()))
        }
        get("{id?}") {
            val id = call.parameters["id"] ?: return@get call.respondText(
                "Missing id",
                status = HttpStatusCode.BadRequest
            )
            val todo = daoTodo.select(id.toInt()) ?: return@get call.respondText(
                "No todo with id $id",
                status = HttpStatusCode.NotFound
            )
            call.respond(todo)
        }
        post {
            val todo = call.receive<Todo>()
            daoTodo.insert(todo)
            call.respondText("Todo stored correctly", status = HttpStatusCode.Created)
        }
        put("{id?}") {
            val id = call.parameters["id"] ?: return@put call.respond(HttpStatusCode.BadRequest)
            val todo = call.receive<Todo>()
            if (daoTodo.edit(id.toInt(), todo))  {
                call.respondText("Todo edit correctly", status = HttpStatusCode.Accepted)
            } else {
                call.respondText("Not found", status = HttpStatusCode.NotFound)
            }
        }
        delete("{id?}") {
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
            if (daoTodo.delete(id.toInt())) {
                call.respondText("Todo removed correctly", status = HttpStatusCode.Accepted)
            } else {
                call.respondText("Not found", status = HttpStatusCode.NotFound)
            }
        }
    }
    // html template rotue
    route("/todos") {
        get {
            call.respond(FreeMarkerContent("index.ftl", mapOf("todos" to daoTodo.selectAll())))
        }
        get("new") {
            call.respond(FreeMarkerContent("new.ftl", model = null))
        }
        get("{id}") {
            val id = call.parameters.getOrFail<Int>("id").toInt()
            call.respond(FreeMarkerContent("show.ftl", mapOf("todo" to daoTodo.select(id))))
        }
        get("{id}/edit") {
            val id = call.parameters.getOrFail<Int>("id").toInt()
            call.respond(FreeMarkerContent("edit.ftl", mapOf("todo" to daoTodo.select(id))))
        }
        post {
            val formParameters = call.receiveParameters()
            val title = formParameters.getOrFail("title")
            val body = formParameters.getOrFail("body")
            val newTodo = daoTodo.insert(Todo(title = title, body = body))
            call.respondRedirect("/todos/${newTodo?.id}")
        }
        post("{id}") {
            val id = call.parameters.getOrFail<Int>("id").toInt()
            val formParameters = call.receiveParameters()
            when (formParameters.getOrFail("_action")) {
                "update" -> {
                    val title = formParameters.getOrFail("title")
                    val body = formParameters.getOrFail("body")
                    daoTodo.edit(id, Todo(id, title = title, body = body))
                    call.respondRedirect("/todos/$id")
                }
                "delete" -> {
                    daoTodo.delete(id)
                    call.respondRedirect("/todos")
                }
            }
        }
    }
}