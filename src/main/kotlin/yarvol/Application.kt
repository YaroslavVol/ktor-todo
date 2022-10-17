package yarvol

import io.ktor.server.application.*
import yarvol.dao.DatabaseFactory
import yarvol.plugins.*

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused")
fun Application.module() {
    DatabaseFactory.init()
    configureRouting()
    configureTemplating()
    configureSerialization()
}