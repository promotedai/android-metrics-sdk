package ai.promoted.metrics.usecases

import ai.promoted.SessionInfo

/**
 * Sources the various IDs in order to provide a current [SessionInfo]
 */
internal class CurrentSessionInfoUseCase(
    private val currentUserIdsUseCase: CurrentUserIdsUseCase,
    private val trackSessionUseCase: TrackSessionUseCase,
    private val trackViewUseCase: TrackViewUseCase
) {
    fun getCurrentSessionInfo() = SessionInfo(
        logUserId = currentUserIdsUseCase.currentLogUserId,
        sessionId = trackSessionUseCase.sessionId.pendingOrCurrentValue,
        viewId = trackViewUseCase.viewId
    )
}
