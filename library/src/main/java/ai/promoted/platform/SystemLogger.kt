package ai.promoted.platform

import android.util.Log

/**
 * OS-logging interface to allow for dynamic enabling/disabling of verbose OS-level logging. Also
 * allows for you to avoid using [Log] in tests run inside the JVM.
 */
@Suppress("TooManyFunctions")
internal abstract class SystemLogger(
    var tag: String,
    var verbose: Boolean
) {
    protected abstract fun writeVerboseMessage(tag: String, message: String)
    protected abstract fun writeInfoMessage(tag: String, message: String)
    protected abstract fun writeError(tag: String, error: Throwable)
    protected abstract fun writeError(tag: String, errorMessage: String)

    fun v(tag: String, message: String) {
        if (verbose) writeVerboseMessage(tag, message)
    }
    fun v(message: String) = v(this.tag, message)

    fun i(tag: String, message: String) = writeInfoMessage(tag, message)
    fun i(message: String) = i(this.tag, message)

    fun e(tag: String, errorMessage: String) = writeError(tag, errorMessage)
    fun e(errorMessage: String) = e(this.tag, errorMessage)

    fun e(tag: String, error: Throwable) = writeError(tag, error)
    fun e(error: Throwable) = e(this.tag, error)
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

    override fun writeInfoMessage(tag: String, message: String) {
        Log.i(tag, message)
    }

    override fun writeError(tag: String, error: Throwable) {
        Log.e(tag, error.message, error)
    }

    override fun writeError(tag: String, errorMessage: String) {
        Log.e(tag, errorMessage)
    }
}
