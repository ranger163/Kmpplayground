package me.inassar.kmp_playground

import android.app.Application
import android.content.Context

class KmpApp: Application() {
    override fun onCreate() {
        super.onCreate()
        AppContext.init(this)
    }
}

object AppContext {
private lateinit var appContext: Context

    fun init(context: Context) {
        appContext = context
    }

    fun get(): Context =appContext
}
