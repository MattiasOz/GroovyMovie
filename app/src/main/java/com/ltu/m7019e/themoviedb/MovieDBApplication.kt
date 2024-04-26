package com.ltu.m7019e.themoviedb

import android.app.Application
import com.ltu.m7019e.themoviedb.database.AppContainer
import com.ltu.m7019e.themoviedb.database.DefaultAppContainer

class MovieDBApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}