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

    private val pendingImplicitValue = advanceableId.currentValue

    var isOverridden = false
        private set

    var currentValue = ""
        private set

    val pendingOrCurrentValue
        get() = if (currentValue.isEmpty()) pendingImplicitValue
        else currentValue

    val currentValueOrNull
        get() = if (currentValue.isEmpty()) null
        else currentValue

    /**
     * Move to the next implicit / internally-generated ID value
     */
    fun advance() {
        if (isOverridden) return

        advanceableId.advance()
        this.currentValue = advanceableId.currentValue
    }

    /**
     * Manually set the ID's value
     */
    fun override(explicitValue: String) {
        this.currentValue = explicitValue
        this.isOverridden = true
    }
}
