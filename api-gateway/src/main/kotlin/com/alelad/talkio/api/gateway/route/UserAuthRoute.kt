package com.alelad.talkio.api.gateway.route

import com.alelad.talkio.api.gateway.model.GoogleUserDataResponseDTO
import com.alelad.talkio.api.gateway.model.TalkioPrincipal
import com.alelad.talkio.commons.serialization.fromJson
import com.alelad.talkio.user.service.grpc.client.UserGrpcClient
import io.grpc.StatusException
import io.ktor.application.call
import io.ktor.auth.OAuthAccessTokenResponse
import io.ktor.auth.authenticate
import io.ktor.auth.authentication
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.response.respondRedirect
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.routing.route
import io.ktor.sessions.clear
import io.ktor.sessions.sessions
import io.ktor.sessions.set

fun Route.userAuth(
    googleUserInfoUrl: String,
    loginResponseRedirectUrl: String,
    userClient: UserGrpcClient
) {
    authenticate("google-oauth") {
        route("login/google") {
            handle {
                val googlePrincipal = call.authentication
                    .principal<OAuthAccessTokenResponse.OAuth2>() ?: error("Could not authenticate with google")

                val googleUserData = HttpClient(Apache)
                    .get<String>(googleUserInfoUrl) {
                        header("Authorization", "Bearer ${googlePrincipal.accessToken}")
                    }
                    .fromJson<GoogleUserDataResponseDTO>()


                val user = userClient.createUser(googleUserData.email)
                println(user.email) // TODO

                call.sessions.set(TalkioPrincipal())
                call.respondRedirect(loginResponseRedirectUrl)
            }
        }
    }

    route("logout") {
        handle {
            call.sessions.clear<TalkioPrincipal>()
            call.respondText("Logged out")
        }
    }

}