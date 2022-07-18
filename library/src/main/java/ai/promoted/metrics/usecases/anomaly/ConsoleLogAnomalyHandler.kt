package ai.promoted.metrics.usecases.anomaly

import ai.promoted.platform.SystemLogger

internal class ConsoleLogAnomalyHandler(private val systemLogger: SystemLogger) : AnomalyHandler {
    override fun handle(anomalyType: AnomalyType) = systemLogger.e(anomalyType.debugDescription)
}