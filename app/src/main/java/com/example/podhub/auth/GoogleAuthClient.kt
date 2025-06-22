package com.example.podhub.auth

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.credentials.CredentialManager
import androidx.credentials.CredentialOption
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.example.podhub.R
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class GoogleAuthClient {
    companion object {
        fun doGoogleSignIn(
            context: Context,
            scope: CoroutineScope,
            launcher: ManagedActivityResultLauncher<Intent, ActivityResult>?,
            login: () -> Unit,
            onRequestCancelled: () -> Unit = {},
            setPendingLogin: (Boolean) -> Unit = {}
        ) {
            val credentialManager = CredentialManager.create(context)

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(getCredentialOptions(context))
                .build()

            scope.launch {
                try {
                    Log.d("AuthDebug", "Bắt đầu doGoogleSignIn")

                    val result = credentialManager.getCredential(context, request)
                    Log.d("AuthDebug", "Đã lấy credential: $result")

                    when (val credential = result.credential) {
                        is CustomCredential -> {
                            Log.d("AuthDebug", "Got CustomCredential")

                            if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                                val googleTokenId = googleIdTokenCredential.idToken
                                val authCredential = GoogleAuthProvider.getCredential(googleTokenId, null)
                                val user = Firebase.auth.signInWithCredential(authCredential).await().user

                                user?.let {
                                    Log.d("AuthDebug", "Firebase user signed in")
                                    login()
                                }
                            }
                        }
                        else -> {
                            Log.d("AuthDebug", "Credential không hợp lệ")
                            onRequestCancelled()
                        }
                    }
                } catch (e: NoCredentialException) {
                    Log.w("AuthDebug", "Không có tài khoản hợp lệ, mở Settings")
                    setPendingLogin(true)
                    launcher?.launch(getIntent())
                } catch (e: GetCredentialException) {
                    Log.e("AuthDebug", "Lỗi lấy Credential: ${e.message}")
                    onRequestCancelled()
                }
            }
        }

        private fun getIntent(): Intent {
            return Intent(Settings.ACTION_ADD_ACCOUNT).apply {
                putExtra(Settings.EXTRA_ACCOUNT_TYPES, arrayOf("com.google"))
            }
        }

        private fun getCredentialOptions(context: Context): CredentialOption {
            return GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setAutoSelectEnabled(false)
                .setServerClientId(context.getString(R.string.web_client_id))
                .build()
        }
    }
}
