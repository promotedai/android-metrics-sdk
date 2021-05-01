package ai.promoted.metrics.usecases

import ai.promoted.internal.Clock
import ai.promoted.proto.common.Timing
import ai.promoted.proto.common.UserInfo
import ai.promoted.proto.event.User

internal fun createTimingMessage(clock: Clock) =
    Timing
        .newBuilder()
        .setClientLogTimestamp(clock.currentTimeMillis)
        .build()

internal fun createUserMessage(clock: Clock) =
    User
        .newBuilder()
        .setTiming(createTimingMessage(clock))
        .build()

// TODO - when Kotlin 1.5 comes out, use inline/value classes to ensure type-safety
internal fun createUserInfoMessage(userId: String, logUserId: String) =
    UserInfo
        .newBuilder()
        .setUserId(userId)
        .setLogUserId(logUserId)
        .build()