package com.alelad.talkio.user.service.grpc

import com.alelad.talkio.user.service.UserService
import com.alelad.talkio.user.service.grpc.extensions.toUserResponse
import com.alelad.talkio.user.service.model.User
import io.grpc.Server
import io.grpc.ServerBuilder
import java.util.logging.Logger

class UserServer constructor(
    private val port: Int,
    userService: UserService
) {
    private val server: Server = ServerBuilder
        .forPort(port)
        .addService(UserGrpcService(userService))
        .build()

    fun start() {
        server.start()
        logger.info("Server started, listening on $port")
        Runtime.getRuntime().addShutdownHook(
            Thread {
                logger.info("*** shutting down gRPC server since JVM is shutting down")
                this@UserServer.stop()
                logger.info("*** server shut down")
            }
        )
    }

    private fun stop() {
        server.shutdown()
    }

    fun blockUntilShutdown() {
        server.awaitTermination()
    }

    private class UserGrpcService(
        private val userService: UserService
    ) : UserGrpcKt.UserCoroutineImplBase() {

        override suspend fun create(request: CreateUserRequest): UserResponse {

            return userService.create(request.email)
                .toUserResponse()
        }
    }

    companion object {
        private val logger = Logger.getLogger(this::class.qualifiedName)
    }
}
