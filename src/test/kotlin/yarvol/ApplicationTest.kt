package yarvol

import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import org.junit.After
import yarvol.dao.dao
import yarvol.models.Todo
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {

//    @After
//    fun clear() = dao

    @Test
    fun testGetRoot() = testApplication {
        val response = client.get("/")
        assertEquals("Hello, ToDo!", response.bodyAsText())
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun testGetAllTodo() = testApplication {
        val response = client.get("/todo")
        assertEquals(
            "No todo list",
            response.bodyAsText()
        )
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun testGetTodo() = testApplication {
        val response = client.get("/todo/1")
        assertEquals(
            "No todo with id 1",
            response.bodyAsText()
        )
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun testPostTodo() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val response = client.post("/todo") {
            contentType(ContentType.Application.Json)
            setBody("body")
        }
        assertEquals("Todo stored correctly", response.bodyAsText())
        assertEquals(HttpStatusCode.Created, response.status)
    }

    @Test
    fun testDeleteTodo() = testApplication {
        val response = client.delete("/todo/1")
        assertEquals(
            "Not found", response.bodyAsText()
        )
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

}