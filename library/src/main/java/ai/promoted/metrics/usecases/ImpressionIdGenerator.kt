package ai.promoted.metrics.usecases

import ai.promoted.metrics.id.IdGenerator

internal class ImpressionIdGenerator(
    private val idGenerator: IdGenerator,
    private val currentUserIdsUseCase: CurrentUserIdsUseCase
) {
    fun generateImpressionId(insertionId: String?, contentId: String?): String? = when {
        insertionId != null -> idGenerator.newId(basedOn = insertionId)
        contentId != null -> idGenerator.newId(
            basedOn = (contentId + currentUserIdsUseCase.currentLogUserId)
        )
        else -> null
    }
}
