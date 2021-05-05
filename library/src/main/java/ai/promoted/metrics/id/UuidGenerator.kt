package ai.promoted.metrics.id

import java.util.*

/**
 * Implementation of [IdGenerator] using [UUID.randomUUID] and [UUID.nameUUIDFromBytes]
 */
internal class UuidGenerator : IdGenerator {
    override fun newId(basedOn: String?): String{
        return if(basedOn == null) UUID.randomUUID().toString()
        else UUID.nameUUIDFromBytes(basedOn.encodeToByteArray()).toString()
    }
}
