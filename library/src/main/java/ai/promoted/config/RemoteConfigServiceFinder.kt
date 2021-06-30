package ai.promoted.config

import ai.promoted.runtime.ClassFinder
import android.annotation.SuppressLint
import com.google.firebase.remoteconfig.FirebaseRemoteConfig

/**
 * Executes a run-time check of whether usable remote-config-service classes are available on the
 * classpath. If not, then we cannot do remote configuration, so a no-op must be used.
 */
internal class RemoteConfigServiceFinder(private val classFinder: ClassFinder) {
    // Suppressing this Lint message because it will be up to library users to have pulled in
    // Firebase and declared the necessary permissions
    @SuppressLint("MissingPermission")
    fun findAvailableService(): RemoteConfigService = when {
        hasFirebaseRemoteConfigService() ->
            FirebaseRemoteConfigService(FirebaseRemoteConfig.getInstance())
        else -> NoOpRemoteConfigService()
    }

    private fun hasFirebaseRemoteConfigService(): Boolean =
        classFinder.exists("com.google.firebase.remoteconfig.FirebaseRemoteConfig")
}
