package com.alelad.talkio.user.service.grpc.extensions

import com.alelad.talkio.commons.model.UserId
import com.alelad.talkio.user.service.grpc.UserResponse
import com.alelad.talkio.user.service.model.User

fun User.toUserResponse(): UserResponse {
    return UserResponse.newBuilder()
        .setId(id.toString())
        .setNickname(nickname)
        .setEmail(email)
        .setName(name)
        .setLastname(lastname)
        .build()
}

fun UserResponse.toUser() = User(
    id = UserId.fromString(id),
    nickname = nickname,
    email = email,
    name = name,
    lastname = lastname

)