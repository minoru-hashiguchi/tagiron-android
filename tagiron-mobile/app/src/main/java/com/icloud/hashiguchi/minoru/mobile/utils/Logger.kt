package com.icloud.hashiguchi.minoru.mobile.utils

import timber.log.Timber

class Logger {
    companion object {
        fun d(tag: String, message: String) {
            Timber.tag(tag).d(message)
        }

        fun i(tag: String, message: String) {
            Timber.tag(tag).i(message)
        }

        fun e(tag: String, message: String) {
            Timber.tag(tag).e(message)
        }

        fun e(tag: String, message: String, e: Throwable) {
            Timber.tag(tag).e(message, e)
        }
    }
}