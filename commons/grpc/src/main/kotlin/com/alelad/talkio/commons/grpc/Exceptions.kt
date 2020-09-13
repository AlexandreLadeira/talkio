package com.alelad.talkio.commons.grpc

import com.alelad.talkio.commons.exceptions.EntityNotFoundException
import io.grpc.Status
import io.grpc.Status.NOT_FOUND
import io.grpc.Status.UNKNOWN
import io.grpc.StatusException

inline fun <T> runMappingGrpcExceptions(block: () -> T): T =
    try {
        block()
    } catch (e: EntityNotFoundException) {
        throw StatusException(NOT_FOUND.exception(e.message,e))
    } catch (e: Exception) {
        throw StatusException(UNKNOWN.exception(e.message ?: "Unknown error", e))
    }

fun Status.exception(description: String, cause: Throwable): Status =
    withDescription(description).withCause(cause)


