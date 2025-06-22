package com.example.podhub.models

data class UserModel(
    val uid: String = "",
    val displayName: String? = null,
    val email: String? = null,
    val photoUrl: String? = null
)
