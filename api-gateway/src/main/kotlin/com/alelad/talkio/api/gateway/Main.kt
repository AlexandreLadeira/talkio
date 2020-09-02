package com.alelad.talkio.api.gateway

import com.alelad.talkio.api.gateway.configuration.Configuration.apiGateway
import org.slf4j.LoggerFactory

private val log = LoggerFactory.getLogger("ManagerApiServer")

fun main() {
    try {
        apiGateway.start()
    } catch (e: Exception) {
        log.error("Failed to run application: {}", e.message, e)
    }
}