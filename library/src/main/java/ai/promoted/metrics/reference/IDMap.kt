package ai.promoted.metrics.reference//package ai.promoted.metrics
//
//import java.util.*
//
//
//// MARK: -
////* Maps client-side IDs to server-side IDs.
//interface IDMap {
//    fun deterministicUUIDString(value/// Produces a deterministic UUID string given an input value.
//                                /// Collision of returned values when given different input should
//                                /// be the same as generating new UUIDs. The generated UUIDs are
//                                /// not necessarily cryptographically secure.
//                                : String?) : String
//    fun logUserID() : String
//    fun sessionID() : String
//    fun impressionIDOrNil(insertionID/// Given a client-side user ID, generates a log user ID which
//                          /// is used to track the the current session without exposing
//                          /// the underlying user ID.
//                          /// Generates a new session ID.
//                          /// Given possible input sources, generate a server-side impression ID.
//                          /// If `insertionID` is not `nil`, then generates an impression ID based
//                          /// on `insertionID`. If `contentID` and `logUserID` are both not `nil`,
//                          /// then generates an impression ID based on a combination of those two
//                          /// IDs. Otherwise, returns `nil`.
//                          : String?, contentID: String?, logUserID: String?) : String?
//    fun contentID(clientID/// Given a client's content ID, generates a content ID to log.
//                  : String) : String
//    fun actionID() : String
//    fun viewID() : String
//}
//
///// Generates a new click ID.
///// Generates a new view ID.
///// Returns the null UUID string when passed `nil`.
//interface IDMapSource {
//    val idMap: IDMap
//}
//
//// MARK: -
///**
//DO NOT INSTANTIATE. Base class for IDMap implementation.
//The `impressionID(clientID:)` and `logUserID(userID:)` methods would
//ideally be in the protocol extension, but doing so prevents
//FakeIDMap from overriding them for tests.
// */
//abstract class AbstractIDMap: IDMap {
//    override fun logUserID() : String =
//        UUID.randomUUID().toString()
//
//    override fun sessionID() : String =
//        UUID.randomUUID().toString()
//
//    override fun impressionIDOrNil(insertionID: String?, contentID: String?, logUserID: String?) : String? {
//        val insertionID = insertionID
//        if (insertionID != null) {
//            return deterministicUUIDString(value = insertionID)
//        }
//        val contentID = contentID
//        val logUserID = logUserID
//        if (contentID != null && logUserID != null) {
//            val combined = contentID + logUserID
//            return deterministicUUIDString(value = combined)
//        }
//        return null
//    }
//
//    override fun contentID(clientID: String) : String =
//        clientID
//
//    override fun actionID() : String =
//        UUID.randomUUID().toString()
//
//    override fun viewID() : String =
//        UUID.randomUUID().toString()
//}
//
//// MARK: -
////* SHA1-based deterministic UUID generation.
//final class SHA1IDMap: AbstractIDMap() {
//    companion object {
//
//        fun sha1(value: String) : String {
//            var context = CC_SHA1_CTX()
//            CC_SHA1_Init(&context)
//            value.withCString { cString  ->
//                CC_SHA1_Update(&context, cString, CC_LONG(strlen(cString)))
//            }
//            var array = listOf<UInt8>(repeating = 0, count = Int(CC_SHA1_DIGEST_LENGTH))
//            CC_SHA1_Final(&array, &context)
//            array[6] = (array[6] & 0x0F) | 0x50
//            // set version number nibble to 5
//            array[8] = (array[8] & 0x3F) | 0x80
//            // reset clock nibbles
//            val uuid = UUID(uuid = (array[0], array[1], array[2], array[3], array[4], array[5], array[6], array[7], array[8], array[9], array[10], array[11], array[12], array[13], array[14], array[15]))
//            return uuid.uuidString
//        }
//    }
//
//
//    override fun deterministicUUIDString(value: String?) : String {
//        val s = value
//        if (s != null) {
//            return SHA1IDMap.sha1(s)
//        }
//        return "00000000-0000-0000-0000-000000000000"
//    }
//}
