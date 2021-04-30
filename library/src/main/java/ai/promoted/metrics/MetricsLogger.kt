package ai.promoted.metrics

import ai.promoted.ClientConfig
import ai.promoted.networking.NetworkConnection
import ai.promoted.networking.PromotedApiRequest
import com.google.protobuf.Message
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

internal class MetricsLogger(private val config: ClientConfig) {
    private val coroutineScope = CoroutineScope(context = Dispatchers.Main)

    private val scheduler = OperationScheduler(
        intervalMillis = TimeUnit.SECONDS.toMillis(config.loggingFlushInterval),
        operation = this::sendCurrentMessages
    )

    private var logMessages = mutableListOf<Message>()

    fun enqueueMessage(message: Message) {
        logMessages.add(message)
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
        val current = logMessages
        logMessages = mutableListOf()

        val request = buildApiRequest(current)
        val connection = config.networkConnectionProvider()

        connection.trySend(request)
    }

    private fun buildApiRequest(messages: List<Message>): PromotedApiRequest {
        TODO()
    }

    private fun NetworkConnection.trySend(request: PromotedApiRequest) {
        coroutineScope.launch {
            try {
                send(request)
            } catch (error: Throwable) {
                // TODO
                error.printStackTrace()
            }
        }
    }
}