package ai.promoted.metrics.id

import java.util.*

internal class UuidGenerator : IdGenerator {
    override fun newId(): String = UUID.randomUUID().toString()
}