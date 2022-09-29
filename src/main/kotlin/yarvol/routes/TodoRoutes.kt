package yarvol.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.html.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import kotlinx.html.body
import kotlinx.html.h1
import kotlinx.html.head
import kotlinx.html.title
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import yarvol.dao.dao
import yarvol.models.Todo
import yarvol.models.Todos

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

    route("/todos") {

        get {
            call.respond(FreeMarkerContent("index.ftl", mapOf("todos" to dao.selectAll())))
        }

        get("new") {
            call.respond(FreeMarkerContent("new.ftl", model = null))
        }

        get("{id}") {
            val id = call.parameters.getOrFail<Int>("id").toInt()
            call.respond(FreeMarkerContent("show.ftl", mapOf("todo" to dao.select(id))))
        }

        get("{id}/edit") {
            val id = call.parameters.getOrFail<Int>("id").toInt()
            call.respond(FreeMarkerContent("edit.ftl", mapOf("todo" to dao.select(id))))
        }

        post {
            val formParameters = call.receiveParameters()
            val body = formParameters.getOrFail("body")
            val newTodo = dao.insert(Todo(body = body))
            call.respondRedirect("/todos/${newTodo?.id}")
        }

        post("{id}") {
            val id = call.parameters.getOrFail<Int>("id").toInt()
            val formParameters = call.receiveParameters()
            when (formParameters.getOrFail("_action")) {
                "update" -> {
                    val body = formParameters.getOrFail("body")
//                    dao.edit(, Todo(body = body))
                    dao.edit(id, Todo(id, body = body))
                    call.respondRedirect("/todos/$id")
                }
                "delete" -> {
                    dao.delete(id)
                    call.respondRedirect("/todos")
                }
            }
        }
    }
}