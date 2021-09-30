package ai.promoted.metrics.usecases

import ai.promoted.metrics.id.IdGenerator

// TODO - remove class
internal class ImpressionIdGenerator(
    private val idGenerator: IdGenerator,
    private val trackUserUseCase: TrackUserUseCase
) {
    fun generateImpressionId(insertionId: String?, contentId: String?): String? = when {
        insertionId != null -> idGenerator.newId(basedOn = insertionId)
        contentId != null -> idGenerator.newId(
            basedOn = (contentId + trackUserUseCase.currentLogUserId)
        )
        else -> null
    }
}
