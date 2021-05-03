package ai.promoted.metrics.id

internal interface IdGenerator {
    fun newId(basedOn: String? = null): String
}
