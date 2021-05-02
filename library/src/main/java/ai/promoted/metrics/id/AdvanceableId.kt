package ai.promoted.metrics.id

/**
 * Represents any given business-specific ID which should be regenerated at certain points in the
 * application / user lifecycle (i.e. represents the overall concept of a "session ID" while
 * allowing for regeneration of a new value each time a session is started).
 *
 * The unique part about this class is not just generating new values, but that it allows you to
 * skip actual regeneration of the value on the first time [advance] is called. In other words, it
 * allows you to say, "Give me a new value every time I call [advance], except for the first time."
 *
 * This is useful in the given example of tracking sessions. Typically, one would expect to generate
 * a new session ID every time startSession is called; however, there are cases where metrics need
 * to be logged prior to a true session starting, but those metrics still need to be associated
 * to the session that *will* start in the future. For this case, we allow the initial value of
 * the session ID to be retained, so that metrics during both the pre-session and the session can be
 * properly associated under one session ID.
 *
 * Note: The behavior described above must be explicitly defined via the "skipFirstAdvancement"
 * boolean when constructing this class. Otherwise, the first call to [advance] will actually
 * advance the ID.
 */
internal class AdvanceableId(
    skipFirstAdvancement: Boolean,
    private val idGenerator: IdGenerator
) {
    private var shouldAdvance = !skipFirstAdvancement

    var currentValue = idGenerator.newId()
        private set

    fun advance(): String {
        if (!shouldAdvance) shouldAdvance = true
        else currentValue = idGenerator.newId()

        return currentValue
    }
}
