package com.example.podhub.models

data class RoomInfo(
    val id: String,
    val topic: String,
    val creator: UserModel,
    val participants: List<String> = emptyList()
)
