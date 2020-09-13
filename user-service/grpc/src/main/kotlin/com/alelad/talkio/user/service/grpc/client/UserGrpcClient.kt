package com.alelad.talkio.user.service.grpc.client

import com.alelad.talkio.user.service.grpc.CreateUserRequest
import com.alelad.talkio.user.service.grpc.UserGrpcKt
import com.alelad.talkio.user.service.grpc.UserResponse
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import java.io.Closeable
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class UserGrpcClient(
    private val channel: ManagedChannel
) : Closeable {
    private val stub: UserGrpcKt.UserCoroutineStub = UserGrpcKt.UserCoroutineStub(channel)

    suspend fun createUser(email: String): UserResponse = coroutineScope {
        val request = CreateUserRequest.newBuilder()
            .setEmail(email)
            .build()
        val response = async { stub.create(request) }
        return@coroutineScope response.await()
    }

    override fun close() {
        channel.shutdown()
            .awaitTermination(5, TimeUnit.SECONDS)
    }

    companion object {
        fun createClient() = UserGrpcClient(
            ManagedChannelBuilder.forAddress("localhost", 9201)
                .usePlaintext()
                .executor(Dispatchers.Default.asExecutor())
                .build()
        )
    }
}