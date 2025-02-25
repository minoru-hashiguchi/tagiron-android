package com.icloud.hashiguchi.minoru.mobile.utils

import android.util.Log
import com.icloud.hashiguchi.minoru.tagiron.constants.Constant
import timber.log.Timber

class Logger {
    companion object {
        @JvmStatic
        fun d(message: String) {
            Log.d(Constant.LOG_TAG, message)
            Timber.tag(Constant.LOG_TAG).d(message)
        }

        @JvmStatic
        fun i(message: String) {
            Log.i(Constant.LOG_TAG, message)
            Timber.tag(Constant.LOG_TAG).i(message)
        }

        @JvmStatic
        fun w(message: String) {
            Log.w(Constant.LOG_TAG, message)
            Timber.tag(Constant.LOG_TAG).w(message)
        }

        @JvmStatic
        fun e(message: String) {
            Log.e(Constant.LOG_TAG, message)
            Timber.tag(Constant.LOG_TAG).e(message)
        }

        @JvmStatic
        fun e(message: String, e: Throwable) {
            Log.e(Constant.LOG_TAG, message, e)
            Timber.tag(Constant.LOG_TAG).e(message, e)
        }
    }
}