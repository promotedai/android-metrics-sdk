package ai.promoted.networking

import ai.promoted.arch.Completable

interface NetworkConnection {
    suspend fun send(request: PromotedApiRequest): Completable
}