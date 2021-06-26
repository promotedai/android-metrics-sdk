package ai.promoted.telemetry

/**
 * Simple wrapper around [Class.forName] for safety and testability
 */
internal class ClassFinder {
    // Suppressing this Detekt check because if we run into *any* issues accessing a given class,
    // we cannot safely assume it is available.
    @Suppress("TooGenericExceptionCaught", "SwallowedException")
    fun exists(name: String): Boolean = try {
        Class.forName(name)
        true
    } catch (error: Throwable) {
        false
    }
}
