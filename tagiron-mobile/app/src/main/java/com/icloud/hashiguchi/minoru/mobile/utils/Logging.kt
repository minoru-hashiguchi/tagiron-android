package com.icloud.hashiguchi.minoru.mobile.utils

import android.app.Application
import android.content.Context
import timber.log.Timber

class Logging : Application() {
    override fun onCreate() {
        super.onCreate()

        Timber.plant(LogTree(context = this))
    }

    class LogTree(private val context: Context) : Timber.DebugTree() {
        // 以下については後のステップで説明します。
        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            LogFile().postLog(context, message)
        }
    }
}