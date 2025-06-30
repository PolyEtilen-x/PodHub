package com.example.podhub.ui.feature.login

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.podhub.R
import com.example.podhub.auth.GoogleAuthClient
import com.example.podhub.storage.DataStoreManager
import com.example.podhub.ui.navigation.Routes
import com.example.podhub.viewmodels.UserViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

@Composable
fun LoginScreen(navController: NavHostController, userViewModel: UserViewModel = viewModel()) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val dataStore = remember { DataStoreManager(context) }

    var pendingLogin by remember { mutableStateOf(false) }
    var relaunchSignIn by remember { mutableStateOf(false) }


    val loginViewModel: LoginViewModel = viewModel(factory = viewModelFactory {
        initializer {
            val dataStoreManager = DataStoreManager(context)
            LoginViewModel(dataStoreManager)
        }
    })

    val loginCallback = {
        val user = Firebase.auth.currentUser
        if (user != null) {
            val uid = user.uid
            val name = user.displayName ?: ""
            val email = user.email ?: ""
            val photoUrl = user.photoUrl?.toString() ?: ""

            scope.launch {
                dataStore.saveUser(uid, name, email, photoUrl)
                loginViewModel.login(uuid = uid)
                navController.navigate(Routes.FAVORITE_ARTIST)
            }
            Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
        }
    }

    val activityResultHandler: (ActivityResult) -> Unit = rememberUpdatedState(newValue = { result: ActivityResult ->
        if (pendingLogin && (result.resultCode == RESULT_OK || result.resultCode == RESULT_CANCELED)) {
            pendingLogin = false
            relaunchSignIn = true  // Trigger sign-in retry
        }
    }).value

    val launcher = rememberLauncherForActivityResult(
        contract = androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult(),
        onResult = activityResultHandler
    )

    if (relaunchSignIn) {
        LaunchedEffect(Unit) {
            relaunchSignIn = false
            GoogleAuthClient.doGoogleSignIn(
                context = context,
                scope = scope,
                launcher = launcher,
                login = loginCallback,
                onRequestCancelled = {
                    Toast.makeText(context, "Request cancelled by PodHub", Toast.LENGTH_SHORT).show()
                },
                setPendingLogin = { pendingLogin = it }
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAF6F5)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
                .background(Color(0xFFF9F7F6), shape = RoundedCornerShape(32.dp))
                .padding(horizontal = 24.dp, vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "PodHub Logo",
                contentScale = ContentScale.Fit,
                modifier = Modifier.height(100.dp).fillMaxWidth()
            )

            Text(
                text = "Listen, Relax and Escape",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            OutlinedButton(
                onClick = {
                    GoogleAuthClient.doGoogleSignIn(
                        context = context,
                        scope = scope,
                        launcher = launcher,
                        login = loginCallback,
                        onRequestCancelled = {
                            Toast.makeText(context, "Request cancelled by PodHub", Toast.LENGTH_SHORT).show()
                        },
                        setPendingLogin = { pendingLogin = it }
                    )
                },
                border = BorderStroke(2.dp, Color(0xFFFFC107)),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color(0xFFFFC107)
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text(text = "Sign in with Google", fontSize = 16.sp)
            }
        }
    }
}

