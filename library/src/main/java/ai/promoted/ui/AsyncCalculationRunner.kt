package ai.promoted.ui

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

internal class AsyncCalculationRunner<Input, Output>(
    private val calculationContext: CoroutineContext = Dispatchers.Default,
    private val notificationContext: CoroutineContext = calculationContext,
    private val calculation: (input: Input) -> Output,
    private val onResult: (result: Output) -> Unit
) {
    private val scope = CoroutineScope(Dispatchers.Default)
    private val mutex = Mutex()

    fun scheduleCalculation(input: Input) {
        scope.launch {
            mutex.withLock {
                val output = calculation.invoke(input)
                notifyResult(output)
            }
        }
    }

    private suspend fun notifyResult(output: Output) {
        withContext(notificationContext) {
            onResult.invoke(output)
        }
    }
}