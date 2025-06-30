//package com.example.podhub.ui.feature.room
//
//import android.view.SurfaceView
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.viewinterop.AndroidView
//import com.example.podcastapp.zeGo.ZegoManager
//import im.zego.zegoexpress.entity.ZegoUser
//
//@Composable
//fun LivestreamScreen(roomId: String, userId: String) {
//    val context = LocalContext.current
//
//    LaunchedEffect(Unit) {
//        ZegoManager.init(context, 2113018141, "8d0ab927091ce85df2f9a2e83e6b9676a7bef3233b60219b96695a74b7116129")
//        ZegoManager.getEngine()?.loginRoom(roomId, ZegoUser(userId))
//    }
//
//    AndroidView(factory = {
//        SurfaceView(it).apply {
//            ZegoManager.getEngine()?.startPreview(this)
//            ZegoManager.getEngine()?.startPublishingStream("stream_$roomId")
//        }
//    }, modifier = Modifier.fillMaxSize())
//}