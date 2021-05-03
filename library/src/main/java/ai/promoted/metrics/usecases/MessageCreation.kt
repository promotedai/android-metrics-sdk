/*
    This file purely contains utility functions that map various data into corresponding protobuf
    Message objects. The purpose is to keep classes interested in reporting these messages free
    from the builder-pattern clutter, and only focus on providing the data necessary to generate
    the message.
 */

package ai.promoted.metrics.usecases

import ai.promoted.internal.Clock
import ai.promoted.internal.DeviceInfoProvider
import ai.promoted.internal.PromotedAiLocale
import ai.promoted.metrics.ActionData
import ai.promoted.metrics.InternalActionData
import ai.promoted.proto.common.Timing
import ai.promoted.proto.common.UserInfo
import ai.promoted.proto.event.Action
import ai.promoted.proto.event.ActionType
import ai.promoted.proto.event.AppScreenView
import ai.promoted.proto.event.Device
import ai.promoted.proto.event.NavigateAction
import ai.promoted.proto.event.Screen
import ai.promoted.proto.event.Session
import ai.promoted.proto.event.Size
import ai.promoted.proto.event.User
import ai.promoted.proto.event.View

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

internal fun createSessionMessage(clock: Clock) =
    Session
        .newBuilder()
        .setTiming(createTimingMessage(clock))
        .setStartEpochMillis(clock.currentTimeMillis)
        .build()

internal fun createDeviceMessage(deviceInfoProvider: DeviceInfoProvider): Device {
    val localeMessage =
        PromotedAiLocale
            .newBuilder()
            .setLanguageCode(deviceInfoProvider.languageCode)
            .setRegionCode(deviceInfoProvider.countryCode)
            .build()

    val screenSizeMessage =
        Size
            .newBuilder()
            .setWidth(deviceInfoProvider.screenWidth)
            .setHeight(deviceInfoProvider.screenHeight)
            .build()

    val screenMessage =
        Screen
            .newBuilder()
            .setSize(screenSizeMessage)
            .setScale(deviceInfoProvider.screenDensity)
            .build()

    return Device
        .newBuilder()
        .setBrand(deviceInfoProvider.brand)
        .setManufacturer(deviceInfoProvider.manufacturer)
        .setIdentifier(deviceInfoProvider.model)
        .setOsVersion(deviceInfoProvider.sdkRelease)
        .setLocale(localeMessage)
        .setScreen(screenMessage)
        .build()
}

internal fun createViewMessage(
    clock: Clock,
    viewId: String,
    sessionId: String,
    name: String,
    // The Device message should be cached in memory elsewhere, so we will not construct it
    // ourselves as part of this function, but rather it should be passed in.
    deviceMessage: Device
) = View
    .newBuilder()
    .setTiming(createTimingMessage(clock))
    .setViewId(viewId)
    .setSessionId(sessionId)
    .setName(name)
    .setDevice(deviceMessage)
    // TODO - Fill out AppScreenView.
    .setAppScreenView(AppScreenView.getDefaultInstance())
    .build()

@Suppress("LongParameterList")
internal fun createActionMessage(
    clock: Clock,
    internalActionData: InternalActionData,
    actionData: ActionData
) =
    Action
        .newBuilder()
        .setTiming(createTimingMessage(clock))
        .setActionId(internalActionData.actionId)
        .setName(actionData.name)
        .setSessionId(internalActionData.sessionId)
        .setViewId(internalActionData.viewId)
        .setActionType(actionData.type)
        .apply {
            internalActionData.impressionId?.let { setImpressionId(it) }
            actionData.insertionId?.let { setInsertionId(it) }
            actionData.requestId?.let { setRequestId(it) }
            elementId = actionData.elementId ?: name
            if (actionData.type == ActionType.NAVIGATE) {
                navigateAction = createNavigationMessage(actionData.targetUrl)
            }
        }
        .build()

private fun createNavigationMessage(targetUrl: String?) =
    NavigateAction
        .newBuilder()
        .apply {
            targetUrl?.let { setTargetUrl(it) }
        }
        .build()

// TODO - when Kotlin 1.5 comes out, use inline/value classes to ensure type-safety
internal fun createUserInfoMessage(userId: String, logUserId: String) =
    UserInfo
        .newBuilder()
        .setUserId(userId)
        .setLogUserId(logUserId)
        .build()
