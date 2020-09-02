package com.alelad.talkio.api.gateway

import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.DefaultHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

class ApiGateway(port: Int) {
    private val server = embeddedServer(Netty, port) {
        install(DefaultHeaders)
        install(CallLogging)

        routing {
            get("health") {
                call.respond(HttpStatusCode.OK, "Talkio Api Gateway is Online!")
            }
        }
    }

    fun start() {
        server.start(wait = true)
    }
}