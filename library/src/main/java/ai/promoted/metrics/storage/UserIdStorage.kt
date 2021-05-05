package ai.promoted.metrics.storage

/**
 * Represents a class that can read & persist user IDs to/from device storage via the abstract
 * vars [userId] and [logUserId].
 */
internal interface UserIdStorage {
    var userId: String
    var logUserId: String
}
