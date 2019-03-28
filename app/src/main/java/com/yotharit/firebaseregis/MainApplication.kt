package com.yotharit.firebaseregis

import android.app.Application
import android.content.Context
import com.google.firebase.FirebaseApp

class MainApplication : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }

    override fun onTerminate() {
        super.onTerminate()
    }

}