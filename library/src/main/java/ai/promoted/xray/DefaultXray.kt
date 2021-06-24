package ai.promoted.xray

import ai.promoted.platform.Clock
import ai.promoted.platform.SystemLogger
import ai.promoted.telemetry.Telemetry

private const val TAG = "Xray"

/**
 * Default implementation of [Xray] that uses [SystemLogger] to log results from a
 * [FunctionMonitor].
 */
internal class DefaultXray(
    clock: Clock,
    private val systemLogger: SystemLogger,
    private val telemetry: Telemetry
) : Xray {
    private val functionMonitor = FunctionMonitor(clock)

    private var _caughtThrowables = mutableListOf<Throwable>()
    override val caughtThrowables: List<Throwable>
        get() {
            val copy = _caughtThrowables
            _caughtThrowables = mutableListOf()
            return copy
        }

    override fun <T : Any> monitored(block: () -> T): T {
        val monitorResult = functionMonitor.monitored(
            stackElementsToExclude = listOf(DefaultXray::class),
            block = block
        )

        return logMonitorResult(monitorResult)
    }

    override suspend fun <T : Any> monitoredSuspend(block: suspend () -> T): T {
        val monitorResult = functionMonitor.monitoredSuspend(
            stackElementsToExclude = listOf(DefaultXray::class),
            block = block
        )

        return logMonitorResult(monitorResult)
    }

    private fun <T : Any> logMonitorResult(result: MonitorResult<T>): T =
        when (result.functionReturn) {
            is FunctionReturn.Failure -> {
                _caughtThrowables.add(result.functionReturn.throwable)
                logMonitoredBlockError(
                    result.tag,
                    result.elapsedTimeMillis,
                    result.functionReturn.throwable
                )
                throw result.functionReturn.throwable
            }
            is FunctionReturn.Success -> {
                logMonitoredBlockEnd(result.tag, result.elapsedTimeMillis)
                result.functionReturn.data
            }
        }

    private fun logMonitoredBlockEnd(blockTag: String, elapsedTime: Long) {
        systemLogger.i(
            tag = TAG,
            message = "$blockTag completed after ${elapsedTime}ms"
        )
    }

    private fun logMonitoredBlockError(blockTag: String, elapsedTime: Long, error: Throwable) {
        val errorMessage = "$blockTag threw an exception after executing for " +
                "${elapsedTime}ms. Exception type: ${error::class.qualifiedName}"
        systemLogger.e(TAG, errorMessage)
        telemetry.onError(errorMessage, error)
    }
}
