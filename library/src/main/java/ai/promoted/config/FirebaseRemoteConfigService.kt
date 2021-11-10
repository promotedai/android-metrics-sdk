package ai.promoted.config

import ai.promoted.ClientConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigValue

private const val LOGGING_ENABLED = "ai_promoted_logging_enabled"
private const val API_URL = "ai_promoted_metrics_logging_url"
private const val API_KEY = "ai_promoted_metrics_logging_key"
private const val WIRE_FORMAT = "ai_promoted_metrics_logging_wire_format"
private const val FLUSH_INTERVAL = "ai_promoted_logging_flush_interval"
private const val XRAY_LEVEL = "ai_promoted_xray_level"

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
                firebaseRemoteConfig.getValue(LOGGING_ENABLED).asBooleanOrNull()
            val apiUrl =
                firebaseRemoteConfig.getValue(API_URL).asNonEmptyStringOrNull()
            val apiKey =
                firebaseRemoteConfig.getValue(API_KEY).asNonEmptyStringOrNull()
            val wireFormat = firebaseRemoteConfig.getWireFormatOrNull()
            val flushInterval =
                firebaseRemoteConfig.getValue(FLUSH_INTERVAL).asLongOrNull()
            val xrayEnabled = firebaseRemoteConfig.getXrayEnabledOrNull()

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
     *
     * This will honor the default fetch interval for Firebase remote config, so changes may not
     * be received immediately if it is sooner than the default (12 hour) interval.
     */
    override fun fetchLatestConfigValues() {
        firebaseRemoteConfig
            .fetch()
            .addOnCompleteListener {
                firebaseRemoteConfig.activate()
            }
    }

    /**
     * Attempt to parse the binary or JSON enums. Any other value (null or an unsupported value)
     * will fall back to null so that the compiled [ClientConfig.metricsLoggingWireFormat] is used.
     */
    private fun FirebaseRemoteConfig.getWireFormatOrNull(): ClientConfig.MetricsLoggingWireFormat? =
        when (this.getValue(WIRE_FORMAT).asNonEmptyStringOrNull()) {
            "json" -> ClientConfig.MetricsLoggingWireFormat.Json
            "binary" -> ClientConfig.MetricsLoggingWireFormat.Binary
            else -> null
        }

    /**
     * Attempt to parse the Xray level enums. Currently, all enum values beside "none" will return
     * a true (Android only supports on or off, rather than levels). Any other string value outside
     * of the enums (or an empty, or a null string) will return null.
     */
    private fun FirebaseRemoteConfig.getXrayEnabledOrNull(): Boolean? =
        when (this.getValue(XRAY_LEVEL).asNonEmptyStringOrNull()) {
            "none" -> false
            "error",
            "warning",
            "info",
            "debug" -> true
            else -> null
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
