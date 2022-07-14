package ai.promoted.platform

import android.content.Context
import android.content.pm.ApplicationInfo
import java.lang.ref.WeakReference

interface AppRuntimeEnvironment {
    val isDebuggable: Boolean

    fun initialize(context: Context)

    companion object {
        val default: AppRuntimeEnvironment by lazy { Default }
    }
}

private object Default : AppRuntimeEnvironment {
    private var contextRef: WeakReference<Context?>? = WeakReference(null)
    private val context: Context? get() = contextRef?.get()

    @Suppress("TooGenericExceptionCaught", "SwallowedException")
    override val isDebuggable: Boolean
        get() = try {
            context!!.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
        } catch (throwable: Throwable) {
            false
        }

    override fun initialize(context: Context) {
        contextRef = WeakReference(context)
    }
}
