package com.alelad.talkio.api.gateway

import com.alelad.talkio.api.gateway.configuration.Configuration.cookieAuthKey
import com.alelad.talkio.api.gateway.configuration.Configuration.cookieDuration
import com.alelad.talkio.api.gateway.configuration.Configuration.cookieEncryptKey
import com.alelad.talkio.api.gateway.configuration.Configuration.googleOAuthRedirectUrl
import com.alelad.talkio.api.gateway.configuration.Configuration.googleOauthProvider
import com.alelad.talkio.api.gateway.configuration.Configuration.googleUserInfoUrl
import com.alelad.talkio.api.gateway.configuration.Configuration.loginResponseRedirectUrl
import com.alelad.talkio.api.gateway.model.TalkioPrincipal
import com.alelad.talkio.api.gateway.route.userAuth
import com.alelad.talkio.commons.serialization.JsonExtensions
import com.alelad.talkio.user.service.grpc.client.UserGrpcClient
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.authenticate
import io.ktor.auth.oauth
import io.ktor.auth.session
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.features.CallId
import io.ktor.features.CallLogging
import io.ktor.features.Compression
import io.ktor.features.ConditionalHeaders
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.features.XForwardedHeaderSupport
import io.ktor.features.callIdMdc
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.jackson.jackson
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.sessions.SessionTransportTransformerEncrypt
import io.ktor.sessions.Sessions
import io.ktor.sessions.cookie
import java.util.*
import org.apache.commons.codec.binary.Hex
import org.koin.ktor.ext.Koin


fun Application.main() {
    install(Koin) {
        modules()
    }
    install(CallLogging) {
        callIdMdc("request.id")
    }
    install(CallId) {
        generate { UUID.randomUUID().toString() }
        replyToHeader(HttpHeaders.XRequestId)
    }
    install(ConditionalHeaders.Feature)
    install(Compression.Feature) { default() }
    install(ContentNegotiation) {
        jackson {
            JsonExtensions.DEFAULT_OBJECT_MAPPER
        }
    }
    install(XForwardedHeaderSupport)
    install(StatusPages) {
        // TODO
    }

    install(Sessions) {
        cookie<TalkioPrincipal>("TalkioPrincipal") {
//            cookie.secure = apiEnvironment != "dev"
            cookie.maxAgeInSeconds = cookieDuration.seconds
            cookie.path = "/"

            val secretEncryptKey = Hex.decodeHex(cookieEncryptKey.toCharArray())
            val secretAuthKey = Hex.decodeHex(cookieAuthKey.toCharArray())

            transform(
                SessionTransportTransformerEncrypt(
                    encryptionKey = secretEncryptKey,
                    signKey = secretAuthKey
                )
            )
        }
    }

    install(Authentication) {
        oauth("google-oauth") {
            client = HttpClient(Apache)
            providerLookup = { googleOauthProvider }
            urlProvider = { googleOAuthRedirectUrl }
        }

        session<TalkioPrincipal> {
            validate {
                TODO()
            }

            challenge {
                call.respond(HttpStatusCode.Unauthorized)
            }
        }
    }

    routing {
        get("health") {
            call.respond(HttpStatusCode.OK, "Talkio Api Gateway is Online!")
        }

        route("v1") {
            userAuth(
                googleUserInfoUrl = googleUserInfoUrl,
                loginResponseRedirectUrl = loginResponseRedirectUrl,
                userClient = UserGrpcClient.createClient()
            )

            authenticate {

            }
        }
    }
}