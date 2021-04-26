package ai.promoted

data class ClientConfig(
    val loggingEnabled: Boolean,
    val metricsLoggingUrl: String,
    val metricsLoggingAPIKey: String,
    val metricsLoggingWireFormat: MetricsLoggingWireFormat,
    val loggingFlushInterval: Long
) {
    data class Builder(
        var loggingEnabled: Boolean = true,
        var metricsLoggingUrl: String = "",
        var metricsLoggingAPIKey: String = "",
        var metricsLoggingWireFormat: MetricsLoggingWireFormat = MetricsLoggingWireFormat.Binary,
        var loggingFlushInterval: Long = 10
    ) {
        fun build() = ClientConfig(
            loggingEnabled,
            metricsLoggingUrl,
            metricsLoggingAPIKey,
            metricsLoggingWireFormat,
            loggingFlushInterval
        )
    }
    
    enum class MetricsLoggingWireFormat {
        Json,
        Binary
    }
}