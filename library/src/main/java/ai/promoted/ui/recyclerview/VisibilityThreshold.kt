package ai.promoted.ui.recyclerview

import ai.promoted.ImpressionThreshold

/**
 * Internal type-alias used to distinguish between the Android view layer semantics of when an RV
 * row is considered visible, versus the Promoted.Ai semantics of when an impression is considered
 * to have occurred.
 *
 * Currently, they mean the same thing (i.e., when the threshold has been met, a view is considered
 * visible, which means an impression is considered to have occurred). Thus, the
 * [ImpressionThreshold] could be directly referenced in the RV tracking layer.
 *
 * However, an "impression" is indeed a higher-level business concept that we would like to keep
 * clearly distinguished from the low-level mechanics of when an Android view is considered
 * visible to the user, as the two definitions could diverge at some point.
 *
 * That being said, it is not worth the memory overhead to create a new object (i.e. map from the
 * outer layer's [ImpressionThreshold] to a new object of type "VisibilityThreshold) purely for the
 * sake of semantics.  Hence, we are using a typealias for now. Should the function or definition
 * of an impression change in the future, this file could be updated to be a data class called
 * "VisibilityThreshold" that is specifically used for defining when an RV row is considered
 * visible.
 *
 * @see ImpressionThreshold
 */
internal typealias VisibilityThreshold = ImpressionThreshold
