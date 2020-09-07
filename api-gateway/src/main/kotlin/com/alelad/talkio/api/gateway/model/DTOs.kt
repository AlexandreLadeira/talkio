package com.alelad.talkio.api.gateway.model

import com.alelad.talkio.commons.model.UserId
import java.time.Instant

data class MessageDTO(
    val to: UserId,
    val from: UserId,
    val text: String,
    val sentAt: Instant
)