//package com.example.podhub.viewmodels
//
//import androidx.compose.runtime.mutableStateListOf
//import androidx.lifecycle.ViewModel
//import com.example.podhub.models.RoomInfo
//import com.example.podhub.models.UserModel
//import dagger.hilt.android.lifecycle.HiltViewModel
//import javax.inject.Inject
//
//@HiltViewModel
//class RoomViewModel @Inject constructor() : ViewModel() {
//    val currentUserId: String
//        get() = "default_user"
//    val currentUser: UserModel
//        get() = UserModel(
//            uid = currentUserId,
//            displayName = "User 123"
//        )
//
//    val roomList = mutableStateListOf<RoomInfo>()
//
//    fun createRoom(topic: String, status: String): Result<String> {
//        return try {
//            val newRoom = RoomInfo(
//                id = "room${roomList.size + 1}",
//                topic = topic,
//                creator = currentUser,
//                status = status
//            )
//            roomList.add(newRoom)
//            Result.success(newRoom.id)
//        } catch (e: Exception) {
//            Result.failure(e)
//        }
//    }
//}