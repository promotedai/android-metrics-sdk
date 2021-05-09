package ai.promoted.xray

import ai.promoted.platform.Clock
import ai.promoted.platform.SystemLogger

private const val TAG = "Xray"

/**
 * Default implementation of [Xray] that uses [SystemLogger] to log results from a
 * [FunctionMonitor].
 */
internal class DefaultXray(
    clock: Clock,
    private val systemLogger: SystemLogger
) : Xray {
    private val functionMonitor = FunctionMonitor(clock)

    private var _caughtThrowables = mutableListOf<Throwable>()
    val caughtThrowables: List<Throwable>
        get() {
            val copy = _caughtThrowables
            _caughtThrowables = mutableListOf()
            return copy
        }

    override fun <T : Any> monitored(block: () -> T): T =
        handleMonitorResult(
            functionMonitor.monitored(
                stackElementsToExclude = listOf(DefaultXray::class),
                block = block
            )
        )

    override suspend fun <T : Any> monitoredSuspend(block: suspend () -> T): T =
        handleMonitorResult(
            functionMonitor.monitoredSuspend(
                stackElementsToExclude = listOf(DefaultXray::class),
                block = block
            )
        )

    private fun <T : Any> handleMonitorResult(result: MonitorResult<T>): T =
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
        systemLogger.e(
            tag = TAG,
            errorMessage = "$blockTag threw an exception after executing for " +
                    "${elapsedTime}ms. Exception type: ${error::class.qualifiedName}"
        )
    }
}
