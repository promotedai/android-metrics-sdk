package ai.promoted.metrics.id

import java.util.*

internal class UuidGenerator : IdGenerator {
    override fun newId(basedOn: String?): String{
        return if(basedOn == null) UUID.randomUUID().toString()
        else UUID.nameUUIDFromBytes(basedOn.encodeToByteArray()).toString()
    }
}
