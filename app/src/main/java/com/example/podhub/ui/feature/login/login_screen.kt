package com.example.podhub.ui.feature.login

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.podhub.models.UserModel
import com.example.podhub.ui.navigation.Routes
import com.example.podhub.viewmodels.UserViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth


@Composable
fun LoginScreen(navController: NavHostController, userViewModel: UserViewModel = viewModel()) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val launcher  = rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()){
        GoogleAuthClient.doGoogleSignIn(
            context = context,
            scope = scope,
            launcher = null,
            login = {
                Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
            }
        )
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
            // Logo PodHub
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "PodHub Logo",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth()
            )

            // Subtitle
            Text(
                text = "Listen, Relax and Escape",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            // Sign in button
            OutlinedButton(
                onClick = {
                    GoogleAuthClient.doGoogleSignIn(
                        context = context,
                        scope = scope,
                        launcher = null,
                        login = {
                            val user = Firebase.auth.currentUser
                            if (user != null) {
                                val userModel = UserModel(
                                    uid = user.uid,
                                    displayName = user.displayName,
                                    email = user.email,
                                    photoUrl = user.photoUrl.toString(),
                                )
                                userViewModel.currentUser = userModel
                                Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
                                navController.navigate(Routes.FAVORITE_ARTIST)
                            } else {
                                Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show()
                            }
                        }
                    )


                },
                border = BorderStroke(2.dp, Color(0xFFFFC107)),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color(0xFFFFC107)
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "Sign in with Google", fontSize = 16.sp)
            }
        }
    }
}
