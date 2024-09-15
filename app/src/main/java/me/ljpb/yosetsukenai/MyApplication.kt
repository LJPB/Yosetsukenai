package me.ljpb.yosetsukenai

import android.app.Application

class MyApplication : Application() {
    lateinit var dbRepositoryContainer: DbRepositoryContainer
    
    override fun onCreate() {
        super.onCreate()
        dbRepositoryContainer = AppDbRepositoryContainer(this)
    }
}