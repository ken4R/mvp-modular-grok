package com.mvpmodular.core.common

class ConsoleAppLogger : AppLogger {
    override fun debug(tag: String, message: String) = log("DEBUG", tag, message)

    override fun info(tag: String, message: String) = log("INFO", tag, message)

    override fun warn(tag: String, message: String) = log("WARN", tag, message)

    override fun error(tag: String, message: String, throwable: Throwable?) {
        log("ERROR", tag, message)
        throwable?.printStackTrace()
    }

    private fun log(level: String, tag: String, message: String) {
        println("[$level] $tag: $message")
    }
}
