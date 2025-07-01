package com.example.podhub.RequestModel

data class Login(
    val uuid : String
)
data class FavouriteRequest(
    val artists: List<Long> = emptyList(),
    val category: List<Long> = emptyList()
)