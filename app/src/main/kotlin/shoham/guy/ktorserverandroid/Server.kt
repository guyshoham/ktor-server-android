package shoham.guy.ktorserverandroid

import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

object Server {
    private val server = embeddedServer(factory = Netty, environment = Environment.getEnvironment())

    fun start() {
        server.start(wait = false)
    }
}
