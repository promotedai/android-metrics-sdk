package ai.promoted.internal

import android.util.Log

/**
 * OS-logging interface to allow for dynamic enabling/disabling of verbose OS-level logging. Also
 * allows for you to avoid using [Log] in tests run inside the JVM.
 */
internal abstract class SystemLogger(
    var tag: String,
    var verbose: Boolean
) {
    protected abstract fun writeVerboseMessage(tag: String, message: String)
    protected abstract fun writeError(tag: String, error: Throwable)

    fun v(message: String) {
        if (verbose) writeVerboseMessage(tag, message)
    }

    fun e(error: Throwable) = writeError(tag, error)
}

/**
 * Default implementation of [SystemLogger] that uses Android's LogCat / [Log]
 */
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
