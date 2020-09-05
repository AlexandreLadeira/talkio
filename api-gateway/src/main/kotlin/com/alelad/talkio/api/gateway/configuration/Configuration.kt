package com.alelad.talkio.api.gateway.configuration

import com.typesafe.config.ConfigFactory
import io.ktor.auth.OAuthServerSettings
import io.ktor.http.HttpMethod

object Configuration {
    private val config = ConfigFactory.load()

    val googleOauthProvider = OAuthServerSettings.OAuth2ServerSettings(
        name = "google",
        authorizeUrl = "https://accounts.google.com/o/oauth2/auth",
        accessTokenUrl = "https://www.googleapis.com/oauth2/v3/token",
        requestMethod = HttpMethod.Post,

        clientId = config.getString("google.client-id"),
        clientSecret = config.getString("google.client-secret"),
        defaultScopes = listOf("email, profile")
    )

    private val port = config.getInt("port")

//    val apiGateway = ApiGateway(port)
}