package ai.promoted.metrics

import ai.promoted.NetworkConnection
import ai.promoted.PromotedApiRequest
import ai.promoted.metrics.usecases.FinalizeLogsUseCase
import com.google.protobuf.Message
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Allows you to log [Message]s in a batch fashion, making use of the provided flush interval to
 * determine when to flush the batch of logs.
 *
 * Logs will be sent using [PromotedApiRequest] + [NetworkConnection], and that request will be
 * executed using Kotlin coroutines, on the IO dispatcher.
 *
 * This class should be retained as a singleton to ensure all log messages are being placed onto
 * a single batch.
 */
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

    /**
     * Enqueue this message. If there is not a current batch scheduled to be sent, this will start
     * a new one. Otherwise, it will be added to the batch and be sent when the flush interval is
     * reached.
     */
    fun enqueueMessage(message: Message) {
        logMessages.add(message)
        scheduler.maybeSchedule()
    }

    /**
     * Cancel the batch that is scheduled to be sent and discard the log messages from that batch.
     */
    fun cancelAndDiscardPendingQueue() {
        scheduler.cancel()
        logMessages.clear()
    }

    /**
     * Cancel the scheduled send operation and instead send the batch right now.
     */
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
