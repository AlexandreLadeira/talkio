package com.alelad.talkio.api.gateway.route

import io.ktor.http.cio.websocket.CloseReason
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.close
import io.ktor.http.cio.websocket.readText
import io.ktor.routing.Route
import io.ktor.websocket.webSocket

fun Route.chat() {
    webSocket("chat") {
        for (frame in incoming) {
            when (frame) {
                is Frame.Text -> {
                    val text = frame.readText()
                    println(text)
                    outgoing.send(Frame.Text("Hello from the server :D"))

                    if (text == "bye") {
                        close(CloseReason(CloseReason.Codes.NORMAL, "Client said BYE"))
                    }
                }

                else -> outgoing.send(Frame.Text("Invalid data type sent"))
            }
        }
    }

}