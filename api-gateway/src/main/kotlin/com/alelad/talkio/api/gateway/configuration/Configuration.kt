package com.alelad.talkio.api.gateway.configuration

import com.alelad.talkio.user.service.grpc.client.UserGrpcClient
import com.typesafe.config.ConfigFactory
import io.ktor.auth.OAuthServerSettings
import io.ktor.http.HttpMethod

object Configuration {
    private val config = ConfigFactory.load()

    val googleOauthProvider = OAuthServerSettings.OAuth2ServerSettings(
        name = "google",
        authorizeUrl = config.getString("google.authorize-url"),
        accessTokenUrl = config.getString("google.access-token-url"),
        requestMethod = HttpMethod.Post,
        clientId = config.getString("google.client-id"),
        clientSecret = config.getString("google.client-secret"),
        defaultScopes = listOf("profile", "email")
    )

    val googleUserInfoUrl = config.getString("google.user-info-url")!!
    val googleOAuthRedirectUrl = config.getString("google.oauth-redirect-url")!!
    val loginResponseRedirectUrl = config.getString("google.login-response-redirect-url")!!

    val cookieDuration = config.getDuration("auth.cookie-duration")
    val cookieEncryptKey = config.getString("auth.cookie-encrypt-key")!!
    val cookieAuthKey = config.getString("auth.cookie-auth-key")!!

    val userClient = UserGrpcClient.createClient()

}