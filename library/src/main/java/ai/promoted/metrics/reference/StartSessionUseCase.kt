//package ai.promoted.metrics
//
//internal class StartSessionUseCase(
//    private val idGenerator: IdGenerator,
//    private val logger: MetricsLogger
//) {
//
//    /**
//     * Given an (optional) user ID, execute all actions to start a session:
//     * - Generate a new session ID
//     * - Retrieve the last logUserId
//     * -
//     */
//    fun startSession(userId: String = ""): String {
//        /*
//            sessionIDProducer.nextValue()
//
//    // New session with same user should not regenerate logUserID.
//    if (self.userID != nil) && (self.userID == userID) { return }
//
//    self.userID = userID
//    store.userID = userID
//
//    // Reads logUserID from store for initial value, if available.
//    store.logUserID = logUserIDProducer.nextValue()
//         */
//
//        /*
//        /// Session ID for this session. Updated when
//  /// `startSession(userID:)` or `startSessionSignedOut()` is
//  /// called. If read before the first call to `startSession*`,
//  /// returns an ID that will be used for the first session.
//  public var sessionID: String { sessionIDProducer.currentValue }
//  private let sessionIDProducer: IDProducer
//         */
//
//        TODO()
//    }
//}