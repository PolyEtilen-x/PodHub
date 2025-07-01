package com.example.podhub.utils

import android.content.Context
import android.net.Uri
import android.util.Log
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback

object CloudinaryUploader {
    fun uploadImage(
        context: Context,
        imageUri: Uri,
        playlistName: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        val safeFolderName = playlistName.replace(" ", "_")
        val uploadFolder = "PodHub/$safeFolderName"

        MediaManager.get().upload(imageUri)
            .option("resource_type", "image")
            .option("folder", uploadFolder)
            .callback(object : UploadCallback {
                override fun onStart(requestId: String?) {}
                override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {}
                override fun onSuccess(requestId: String?, resultData: Map<*, *>?) {
                    val url = resultData?.get("secure_url") as? String
                    if (url != null) {
                        onSuccess(url)
                    } else {
                        onError("Không lấy được URL")
                    }
                }

                override fun onError(requestId: String?, error: ErrorInfo?) {
                    onError("Lỗi upload: ${error?.description}")
                    Log.e("Cloudinary", error?.description ?: "unknown")
                }

                override fun onReschedule(requestId: String?, error: ErrorInfo?) {}
            })
            .dispatch(context)
    }
}
