package com.alelad.talkio.api.gateway.model

import com.alelad.talkio.commons.model.GoogleUserId

data class GoogleUserDataResponseDTO(
    val id: GoogleUserId,
    val email: String,
    val name: String,
    val picture: String
)