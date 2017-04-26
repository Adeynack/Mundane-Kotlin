package com.moneydance.modules.kotlinmd

interface Logger {

    fun info(message: String): Unit

    fun error(message: String, error: Throwable? = null): Unit

}

class SystemErrLogger : Logger {

    override fun info(message: String) {
        System.err.println("[INFO] $message")
    }

    override fun error(message: String, error: Throwable?) {
        System.err.println("[ERROR] $message")
        error?.let { System.err.println(it.toString()) }
    }

}