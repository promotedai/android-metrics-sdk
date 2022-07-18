package ai.promoted.metrics.usecases.anomaly

/**
 * Represents an object that can handle logging anomalies that are detected a runtime.
 *
 * @see [ClientConfig.LoggingAnomalyHandlingType]
 */
internal interface AnomalyHandler {
    fun handle(anomalyType: AnomalyType)
}