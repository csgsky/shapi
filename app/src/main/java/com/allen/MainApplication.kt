package com.allen

import android.app.Application
import android.util.Log

class MainApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        Log.i(MainApplication::class.simpleName, "test")
        Log.i(MainApplication::class.simpleName, "test1")
    }
}