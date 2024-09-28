package me.ljpb.yosetsukenai

import android.app.Application
import me.ljpb.yosetsukenai.data.AppDbRepositoryContainer
import me.ljpb.yosetsukenai.data.DbRepositoryContainer

class MyApplication : Application() {
    lateinit var dbRepositoryContainer: DbRepositoryContainer
    
    override fun onCreate() {
        super.onCreate()
        dbRepositoryContainer = AppDbRepositoryContainer(this)
    }
}