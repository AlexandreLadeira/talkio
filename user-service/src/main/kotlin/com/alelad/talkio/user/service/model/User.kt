package com.alelad.talkio.user.service.model

import com.alelad.talkio.commons.model.UserId

data class User(
    val id: UserId,
    val nickname: String,
    val email: String,
    val name: String,
    val lastname: String,
    val profilePicture: String? = null
)