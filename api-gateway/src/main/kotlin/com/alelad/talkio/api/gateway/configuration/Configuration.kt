package com.alelad.talkio.api.gateway.configuration

import com.alelad.talkio.api.gateway.ApiGateway
import com.typesafe.config.ConfigFactory

object Configuration {
    private const val APPLICATION = "talkio.api-gateway"
    private val config = ConfigFactory.load().getConfig(APPLICATION)

    private val port = config.getInt("port")

    val apiGateway = ApiGateway(port)
}