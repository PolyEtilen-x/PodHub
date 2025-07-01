package com.example.podhub

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import com.cloudinary.android.MediaManager

@HiltAndroidApp
class PodhubApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        val config: HashMap<String, String> = HashMap()
        config["cloud_name"] = "drbfk0it9"
        config["api_key"] = "186443578522722"
        config["api_secret"] = "vuxXrro8h5VwdYCPFppAZUkB4oI"
        MediaManager.init(this, config)
    }
}
