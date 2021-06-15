package ai.promoted.metrics.id

/**
 * Represents any of the top-level "ancestor" IDs (i.e. session ID, view ID, logUserID) which
 * can either be explicitly set by the library user, or implicitly set by calling [advance].
 */
internal class AncestorId(
    idGenerator: IdGenerator
) {
    private val advanceableId = AdvanceableId(
        skipFirstAdvancement = true,
        idGenerator
    )

    val pendingImplicitValue = advanceableId.currentValue

    var currentValue = ""
        private set

    /**
     * Move to the next implicit / internally-generated ID value
     */
    fun advance() {
        advanceableId.advance()
        this.currentValue = advanceableId.currentValue
    }

    /**
     * Manually set the ID's value
     */
    fun override(explicitValue: String) {
        this.currentValue = explicitValue
    }
}