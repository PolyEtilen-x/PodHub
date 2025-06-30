//package com.example.podhub.ui.feature.room
//
//import android.view.SurfaceView
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.viewinterop.AndroidView
//import com.example.podhub.zeGo.ZegoManager
//import im.zego.zegoexpress.entity.ZegoUser
//
//@Composable
//fun ListenScreen(roomId: String) {
//    val context = LocalContext.current
//
//    LaunchedEffect(Unit) {
//        ZegoManager.getEngine()?.loginRoom(roomId, ZegoUser("listener_${System.currentTimeMillis()}"))
//    }
//
//    AndroidView(factory = {
//        SurfaceView(it).apply {
//            ZegoManager.getEngine()?.startPlayingStream("stream_$roomId", this)
//        }
//    }, modifier = Modifier.fillMaxSize())
//}