package yarvol.dao

import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.*
import yarvol.dao.DatabaseFactory.dbQuery
import yarvol.models.Todo
import yarvol.models.Todos

class DAOTodoImpl : DAOFacade<Todo> {
    private fun resultRowToTodo(row: ResultRow) = Todo(
        id = row[Todos.id],
        body = row[Todos.body]
    )

    override suspend fun selectAll(): List<Todo> = dbQuery {
        Todos.selectAll().map(::resultRowToTodo)
    }

    override suspend fun select(id: Int): Todo? = dbQuery {
        Todos
            .select { Todos.id eq id }
            .map(::resultRowToTodo)
            .singleOrNull()
    }

    override suspend fun insert(type: Todo): Todo? = dbQuery {
        val insertStatement = Todos.insert {
            it[Todos.body] = type.body
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToTodo)
    }

    override suspend fun edit(id: Int, type: Todo): Boolean = dbQuery {
        Todos.update({ Todos.id eq id }) {
            it[Todos.body] = type.body
        } > 0
    }

    override suspend fun delete(id: Int): Boolean = dbQuery {
        Todos.deleteWhere { Todos.id eq id } > 0
    }
}

val dao: DAOFacade<Todo> = DAOTodoImpl().apply {
    runBlocking {
        if(selectAll().isEmpty()) {
            insert(Todo(body = "Empty todo"))
        }
    }
}