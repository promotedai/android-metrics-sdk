package ai.promoted.config

import ai.promoted.ClientConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigValue
import com.google.firebase.remoteconfig.ktx.get

private const val LOGGING_ENABLED = "ai_promoted_logging_enabled"
private const val API_URL = "ai_promoted_logging_url"
private const val API_KEY = "ai_promoted_logging_key"
private const val USE_JSON = "ai_promoted_wire_json"
private const val FLUSH_INTERVAL = "ai_promoted_flush_interval"
private const val XRAY_ENABLED = "ai_promoted_xray_enabled"

/**
 * Implementation of [RemoteConfigService] that uses [FirebaseRemoteConfig] to provide the
 * remote config functionality.
 */
internal class FirebaseRemoteConfigService(
    private val firebaseRemoteConfig: FirebaseRemoteConfig
) : RemoteConfigService {
    /**
     * The most recently cached [RemoteConfig].
     */
    override val latestRemoteConfig: RemoteConfig
        get() {
            val loggingEnabled =
                firebaseRemoteConfig[LOGGING_ENABLED].asBooleanOrNull()
            val apiUrl =
                firebaseRemoteConfig[API_URL].asNonEmptyStringOrNull()
            val apiKey =
                firebaseRemoteConfig[API_KEY].asNonEmptyStringOrNull()
            val useJson =
                firebaseRemoteConfig[USE_JSON].asBooleanOrNull()
            val wireFormat = when (useJson) {
                null -> null
                true -> ClientConfig.MetricsLoggingWireFormat.Json
                false -> ClientConfig.MetricsLoggingWireFormat.Binary
            }
            val flushInterval =
                firebaseRemoteConfig[FLUSH_INTERVAL].asLongOrNull()
            val xrayEnabled =
                firebaseRemoteConfig[XRAY_ENABLED].asBooleanOrNull()

            return RemoteConfig(
                loggingEnabled,
                apiUrl,
                apiKey,
                wireFormat,
                flushInterval,
                xrayEnabled
            )
        }

    /**
     * Asynchronously updates the [RemoteConfig] to reflect the latest values from the server.
     */
    override fun fetchLatestConfigValues() {
        firebaseRemoteConfig
            // Ensures the latest config is always fetched & stored immediately
            .fetch(0)
            .addOnCompleteListener {
                firebaseRemoteConfig.activate()
            }
    }

    /**
     * Firebase will intelligently try to convert strings to booleans based on their own logic,
     * which can result in false positives / false negatives on the true boolean value (i.e. if the
     * key doesn't exist, it might return a false).
     *
     * Instead, we want to be more strict and enforce that the strict, case-sensitive boolean
     * string must be present--otherwise, the value will be ignored and null will be returned.
     */
    private fun FirebaseRemoteConfigValue.asBooleanOrNull(): Boolean? =
        when (this.asString()) {
            "true" -> true
            "false" -> false
            else -> null
        }

    /**
     * Similar strategy to [FirebaseRemoteConfigValue.asBooleanOrNull]. Either a string with
     * content, or a null.
     */
    private fun FirebaseRemoteConfigValue.asNonEmptyStringOrNull(): String? {
        val string = this.asString()
        return if (string.isEmpty()) null
        else string
    }

    /**
     * Same strategy as [FirebaseRemoteConfigValue.asBooleanOrNull]; either a valid Long should be
     * present from the remote config string value, or null (i.e. no empty strings should be
     * converted to 0L).
     */
    private fun FirebaseRemoteConfigValue.asLongOrNull(): Long? = this.asString().toLongOrNull()
}
