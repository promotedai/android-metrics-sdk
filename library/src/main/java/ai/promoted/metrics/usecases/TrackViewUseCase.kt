package ai.promoted.metrics.usecases

import ai.promoted.internal.Clock
import ai.promoted.metrics.MetricsLogger
import ai.promoted.metrics.id.AdvanceableId
import ai.promoted.metrics.id.IdGenerator

internal class TrackViewUseCase(
    private val logger: MetricsLogger,
    private val clock: Clock,
    private val idGenerator: IdGenerator,
    private val sessionUseCase: TrackSessionUseCase
) {
    private val advanceableViewId = AdvanceableId(
        skipFirstAdvancement = true,
        idGenerator = idGenerator
    )

    val viewId: String
        get() = advanceableViewId.currentValue

    fun onViewVisible(key: String) {

    }
}