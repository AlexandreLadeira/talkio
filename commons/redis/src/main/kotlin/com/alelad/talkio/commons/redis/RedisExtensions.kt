package com.alelad.talkio.commons.redis

import io.lettuce.core.RedisClient
import io.lettuce.core.api.async.RedisAsyncCommands

fun createRedisAsyncCommands(uri: String): RedisAsyncCommands<String, String> =
    RedisClient.create(uri)
        .connect()
        .async()
