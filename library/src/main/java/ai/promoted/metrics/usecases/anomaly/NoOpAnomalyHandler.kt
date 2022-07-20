package ai.promoted.metrics.usecases.anomaly

internal class NoOpAnomalyHandler : AnomalyHandler {
    override fun handle(anomalyType: AnomalyType) {
        // no-op
    }
}
