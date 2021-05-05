package ai.promoted.metrics.id

/**
 * Represents a class that can generate new unique IDs, and can also receive a "based on" string
 * that will result in a deterministic UUID.
 */
internal interface IdGenerator {
    fun newId(basedOn: String? = null): String
}
