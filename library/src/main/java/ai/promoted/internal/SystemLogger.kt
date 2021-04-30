package ai.promoted.internal

import android.util.Log

internal abstract class SystemLogger(
    var tag: String,
    var verbose: Boolean
) {
    protected abstract fun writeVerboseMessage(tag: String, message: String)
    protected abstract fun writeError(tag: String, error: Throwable)

    fun v(message: String) {
        if(verbose) writeVerboseMessage(tag, message)
    }

    fun e(error: Throwable) = writeError(tag, error)
}

internal class LogcatLogger(
    tag: String,
    verbose: Boolean
) : SystemLogger(tag, verbose) {
    override fun writeVerboseMessage(tag: String, message: String) {
        Log.v(tag, message)
    }

    override fun writeError(tag: String, error: Throwable) {
        Log.e(tag, error.message, error)
    }
}