package com.alelad.talkio.api.gateway

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.afterburner.AfterburnerModule
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallId
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.features.callIdMdc
import io.ktor.http.HttpStatusCode
import io.ktor.jackson.jackson
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.routing
import java.util.*
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
//        replyToHeader(HttpHeaders.XRequestId)
    }
//    install(ConditionalHeaders.Feature)
//    install(Compression.Feature) { default() }
    install(ContentNegotiation) {
        jackson {
            disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            enable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
            disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            setSerializationInclusion(JsonInclude.Include.NON_NULL)

            registerModule(Jdk8Module())
            registerModule(JavaTimeModule())
            registerModule(ParameterNamesModule())
            registerModule(AfterburnerModule())
        }
    }
//    install(XForwardedHeaderSupport)
    install(StatusPages) {
        // TODO
    }

    routing {
        get("health") {
            call.respond(HttpStatusCode.OK, "Talkio Api Gateway is Online!")
        }
    }
}