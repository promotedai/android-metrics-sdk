package ai.promoted.metrics

import ai.promoted.metrics.usecases.FinalizeLogsUseCase
import ai.promoted.networking.NetworkConnection
import ai.promoted.networking.PromotedApiRequest
import com.google.protobuf.Message
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

internal class MetricsLogger(
    flushIntervalMillis: Long,
    private val networkConnection: NetworkConnection,
    private val finalizeLogsUseCase: FinalizeLogsUseCase
) {
    private val networkConnectionScope = CoroutineScope(context = Dispatchers.IO)

    private val scheduler = OperationScheduler(
        intervalMillis = flushIntervalMillis,
        operation = this::sendCurrentMessages
    )

    private var logMessages = mutableListOf<Message>()

    fun enqueueMessage(message: Message) {
        logMessages.add(message)
        scheduler.maybeSchedule()
    }

    fun cancelAndDiscardPendingQueue() {
        scheduler.cancel()
        logMessages.clear()
    }

    fun cancelAndSendPendingQueue() {
        scheduler.cancel()
        sendCurrentMessages()
    }

    private fun sendCurrentMessages() {
        val logMessagesCopy = logMessages
        logMessages = mutableListOf()

        val request = finalizeLogsUseCase.finalizeLogs(logMessagesCopy)

        trySend(request)
    }

    // TODO - handle error
    @Suppress("TooGenericExceptionCaught")
    private fun trySend(request: PromotedApiRequest) {
        networkConnectionScope.launch {
            try {
                networkConnection.send(request)
            } catch (error: Throwable) {
                // TODO
                error.printStackTrace()
            }
        }
    }
}
