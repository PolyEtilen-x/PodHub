package com.example.podhub.ui.feature.room

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.fragment.app.FragmentActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.zegocloud.uikit.prebuilt.livestreaming.ZegoUIKitPrebuiltLiveStreamingFragment
import com.zegocloud.uikit.prebuilt.livestreaming.ZegoUIKitPrebuiltLiveStreamingConfig
import com.example.podhub.ui.theme.ZegoLiveTheme

class LiveActivity : FragmentActivity() {

    val userID = "aud"
    val userName = "aud"
    val liveId = "123"
    val isHost = true

    val appId = 2113018141L
    val appSign = "8d0ab927091ce85df2f9a2e83e6b9676a7bef3233b60219b96695a74b7116129"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ZegoLiveTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Log.d("ZegoRole", "Current role: ${if (isHost) "Host" else "Audience"}")

                    val config = if (isHost) {
                        ZegoUIKitPrebuiltLiveStreamingConfig.host(true).apply {
                            turnOnCameraWhenJoining = true
                            Log.d("ZegoCamera", "Host camera enabled: $turnOnCameraWhenJoining")
                        }
                    } else {
                        ZegoUIKitPrebuiltLiveStreamingConfig.audience(true).apply {
                            turnOnCameraWhenJoining = false
                            Log.d("ZegoCamera", "Audience camera enabled: $turnOnCameraWhenJoining")
                        }
                    }

                    val fragmentManager = remember {
                        this@LiveActivity.supportFragmentManager
                    }

                    val fragment = remember {
                        ZegoUIKitPrebuiltLiveStreamingFragment.newInstance(
                            appId, appSign, userID, userName, liveId, config
                        ).also {
                            Log.d("ZegoFragment", "Fragment initialized with liveId: $liveId")
                        }
                    }

                    AndroidView(
                        modifier = Modifier.fillMaxSize().padding(innerPadding),
                        factory = { context ->
                            FrameLayout(context).apply {
                                id = View.generateViewId()
                                setBackgroundColor(android.graphics.Color.TRANSPARENT)
                                layoutParams = ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT
                                )
                                Log.d("ZegoView", "FrameLayout created with id: $id")
                            }
                        },
                        update = {
                            fragmentManager.beginTransaction().replace(it.id, fragment).commitNow()
                            Log.d("ZegoView", "Fragment transaction committed for FrameLayout id: ${it.id}")
                        }
                    )
                }
            }
        }
    }
}