package ai.promoted

import ai.promoted.platform.AppRuntimeEnvironment
import android.content.Context

data class FakeAppRuntimeEnvironment(
    override val isNonProdBuild: Boolean = true,
    override val isRunningOnEmulator: Boolean = true,
    override val isDebuggerConnected: Boolean = true
) : AppRuntimeEnvironment {
    @Suppress("EmptyFunctionBlock")
    override fun initialize(context: Context) {

    }
}
