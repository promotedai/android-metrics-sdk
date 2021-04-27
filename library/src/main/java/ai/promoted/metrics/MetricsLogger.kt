package ai.promoted.metrics

interface MetricsLogger {
    // TODO - define metrics logger API
    fun log()

    fun shutdown()
}