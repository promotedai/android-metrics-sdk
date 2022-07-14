package ai.promoted

import ai.promoted.platform.AppRuntimeEnvironment
import android.content.Context

data class FakeAppRuntimeEnvironment(
    override val isDebuggable: Boolean
) : AppRuntimeEnvironment {
    @Suppress("EmptyFunctionBlock")
    override fun initialize(context: Context) {

    }
}
