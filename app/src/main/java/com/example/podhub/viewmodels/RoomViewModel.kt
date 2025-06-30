package com.example.podhub.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.compose.runtime.mutableStateListOf
import com.example.podhub.models.RoomInfo

@HiltViewModel
class RoomViewModel @Inject constructor() : ViewModel() {
    val currentUserId = "user123"
    val roomList = mutableStateListOf<RoomInfo>()

    init {
        roomList.addAll(
            listOf(
                RoomInfo("room1", "Góc Chill", "user123", "Đang phát"),
                RoomInfo("room2", "K-pop Talk", "user456", "Đang phát")
            )
        )
    }
}