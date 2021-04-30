package ai.promoted

import ai.promoted.networking.NetworkConnection
import ai.promoted.networking.RetrofitNetworkConnection

data class ClientConfig(
    val loggingEnabled: Boolean,
    val metricsLoggingUrl: String,
    val devMetricsLoggingUrl: String,
    val metricsLoggingApiKey: String,
    val metricsLoggingWireFormat: MetricsLoggingWireFormat,
    val loggingFlushIntervalSeconds: Long,
    val networkConnectionProvider: () -> NetworkConnection,
) {
    data class Builder(
        var loggingEnabled: Boolean = true,
        var metricsLoggingUrl: String = "",
        var devMetricsLoggingUrl: String = "",
        var metricsLoggingAPIKey: String = "",
        var metricsLoggingWireFormat: MetricsLoggingWireFormat = MetricsLoggingWireFormat.Binary,
        var loggingFlushInterval: Long = 10,
        var networkConnectionProvider: () -> NetworkConnection = { RetrofitNetworkConnection() }
    ) {
        fun build() = ClientConfig(
            loggingEnabled,
            metricsLoggingUrl,
            devMetricsLoggingUrl,
            metricsLoggingAPIKey,
            metricsLoggingWireFormat,
            loggingFlushInterval,
            networkConnectionProvider
        )
    }

    enum class MetricsLoggingWireFormat {
        Json,
        Binary
    }
}