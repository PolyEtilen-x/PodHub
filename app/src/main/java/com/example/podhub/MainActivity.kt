package com.example.podhub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.fragment.app.FragmentActivity
import com.example.podhub.ui.theme.PodhubTheme
import androidx.navigation.compose.rememberNavController
import com.example.podhub.ui.navigation.AppRouter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PodhubTheme {
                val navController = rememberNavController()
                AppRouter(navController)
            }
        }
    }
}
