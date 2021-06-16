package ai.promoted

import ai.promoted.platform.SystemLogger

internal class SystemOutLogger : SystemLogger("test", true) {
    override fun writeVerboseMessage(tag: String, message: String) {
        println("$tag - $message")
    }

    override fun writeInfoMessage(tag: String, message: String) {
        println("$tag - $message")
    }

    override fun writeError(tag: String, error: Throwable) {
        println(tag)
        error.printStackTrace()
    }

    override fun writeError(tag: String, errorMessage: String) {
        println(tag)
        Exception(errorMessage).printStackTrace()
    }
}