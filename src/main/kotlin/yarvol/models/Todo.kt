package yarvol.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
data class Todo(
    val id: Int = 0,
    val body: String
)

object Todos : Table() {
    val id = integer("id").autoIncrement()
    val body = varchar("title", 1024)

    override val primaryKey = PrimaryKey(id)
}