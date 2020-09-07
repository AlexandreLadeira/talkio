package com.alelad.talkio.user.service.grpc

import com.alelad.talkio.user.service.grpc.extensions.toUser
import com.alelad.talkio.user.service.model.User
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import java.io.Closeable
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class UserServiceGrpcClient(
    private val channel: ManagedChannel
) : Closeable {
    private val stub: UserGrpcKt.UserCoroutineStub = UserGrpcKt.UserCoroutineStub(channel)

    suspend fun createUser(email: String): User = coroutineScope {
        val request = CreateUserRequest
            .newBuilder()
            .setEmail(email)
            .build()
        val response = async { stub.create(request) }
        return@coroutineScope response.await().toUser()
    }

    override fun close() {
        channel.shutdown()
            .awaitTermination(5, TimeUnit.SECONDS)
    }
}

suspend fun main() {
    val port = 9201

    val client = UserServiceGrpcClient(
        ManagedChannelBuilder.forAddress("localhost", port)
            .usePlaintext()
            .executor(Dispatchers.Default.asExecutor())
            .build()
    )

    val user = client.createUser("alexandreladeira13@gmail.com")
    println(user)
}