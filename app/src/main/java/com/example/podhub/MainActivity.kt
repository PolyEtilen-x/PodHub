package com.example.podhub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import com.example.podhub.ui.theme.PodhubTheme
import androidx.navigation.compose.rememberNavController
import com.example.podhub.ui.navigation.AppRouter

class MainActivity : ComponentActivity() {
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
