/*
    This file purely contains utility functions that map various data into corresponding protobuf
    Message objects. The purpose is to keep classes interested in reporting these messages free
    from the builder-pattern clutter, and only focus on providing the data necessary to generate
    the message.
 */
@file:Suppress("TooManyFunctions")

package ai.promoted.metrics.usecases

import ai.promoted.ActionData
import ai.promoted.ImpressionData
import ai.promoted.metrics.InternalActionData
import ai.promoted.metrics.InternalImpressionData
import ai.promoted.platform.Clock
import ai.promoted.platform.DeviceInfoProvider
import ai.promoted.platform.PromotedAiLocale
import ai.promoted.proto.common.Device
import ai.promoted.proto.common.Properties
import ai.promoted.proto.common.Screen
import ai.promoted.proto.common.Size
import ai.promoted.proto.common.Timing
import ai.promoted.proto.common.UserInfo
import ai.promoted.proto.event.Action
import ai.promoted.proto.event.ActionType
import ai.promoted.proto.event.AppScreenView
import ai.promoted.proto.event.AutoView
import ai.promoted.proto.event.Impression
import ai.promoted.proto.event.NavigateAction
import ai.promoted.proto.event.User
import ai.promoted.proto.event.View
import com.google.protobuf.Message

internal fun createTimingMessage(clock: Clock) =
    Timing
        .newBuilder()
        .setClientLogTimestamp(clock.currentTimeMillis)
        .build()

internal fun createTimingMessage(time: Long) =
    Timing
        .newBuilder()
        .setClientLogTimestamp(time)
        .build()

internal fun createUserMessage(clock: Clock, userId: String?, logUserId: String?) =
    User
        .newBuilder()
        .setTiming(createTimingMessage(clock))
        .setUserInfo(createUserInfoMessage(userId, logUserId))
        .build()

internal fun createDeviceMessage(deviceInfoProvider: DeviceInfoProvider): Device {
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
        .setScreen(screenMessage)
        .build()
}

internal fun createViewMessage(
    clock: Clock,
    deviceInfoProvider: DeviceInfoProvider,
    viewId: String?,
    sessionId: String?,
    name: String,
) = View
    .newBuilder()
    .setTiming(createTimingMessage(clock))
    .setLocale(createLocaleMessage(deviceInfoProvider))
    .apply {
        sessionId?.let { setSessionId(it) }
        viewId?.let { setViewId(it) }
    }
    .setName(name)
    .setAppScreenView(AppScreenView.getDefaultInstance())
    .build()

@Suppress("LongParameterList")
internal fun createAutoViewMessage(
    clock: Clock,
    deviceInfoProvider: DeviceInfoProvider,
    autoViewId: String?,
    sessionId: String?,
    name: String
) = AutoView
    .newBuilder()
    .setTiming(createTimingMessage(clock))
    .setLocale(createLocaleMessage(deviceInfoProvider))
    .apply {
        sessionId?.let { setSessionId(it) }
        autoViewId?.let { setAutoViewId(autoViewId) }
    }
    .setName(name)
    .setAppScreenView(AppScreenView.getDefaultInstance())
    .build()

private fun createLocaleMessage(deviceInfoProvider: DeviceInfoProvider) =
    PromotedAiLocale
        .newBuilder()
        .setLanguageCode(deviceInfoProvider.languageCode)
        .setRegionCode(deviceInfoProvider.countryCode)
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
        .setName(internalActionData.name)
        .setActionType(internalActionData.type)
        .apply {
            internalActionData.sessionId?.let { setSessionId(it) }
            internalActionData.autoViewId?.let { setAutoViewId(it) }
            internalActionData.hasSuperImposedViews?.let { setHasSuperimposedViews(it) }
            actionData.impressionId?.let { setImpressionId(it) }
            actionData.insertionId?.let { setInsertionId(it) }
            actionData.requestId?.let { setRequestId(it) }
            elementId = actionData.elementId ?: name
            if (internalActionData.type == ActionType.NAVIGATE) {
                navigateAction = createNavigationMessage(actionData.targetUrl)
            }

            createPropertiesMessage(actionData.customProperties)?.let {
                this.properties = it
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

internal fun createImpressionMessage(
    impressionData: ImpressionData,
    internalImpressionData: InternalImpressionData,
) =
    Impression
        .newBuilder()
        .setTiming(createTimingMessage(internalImpressionData.time))
        .setImpressionId(internalImpressionData.impressionId)
        .apply {
            internalImpressionData.sessionId?.let { setSessionId(it) }
            internalImpressionData.autoViewId?.let { setAutoViewId(it) }
            internalImpressionData.hasSuperImposedViews?.let { setHasSuperimposedViews(it) }
            impressionData.insertionId?.let { setInsertionId(it) }
            impressionData.requestId?.let { setRequestId(it) }
            impressionData.contentId?.let { setContentId(it) }
            createPropertiesMessage(impressionData.customProperties)?.let {
                this.properties = it
            }
        }
        .build()

@Suppress("TooGenericExceptionCaught", "PrintStackTrace")
internal fun createPropertiesMessage(properties: Message?): Properties? {
    if (properties == null) return null
    return try {
        Properties
            .newBuilder()
            .setStructBytes(properties.toByteString())
            .build()
    } catch (e: Throwable) {
        // Currently catching any type of Throwable here, because we want the library to be
        // particularly safe at runtime, even if the custom properties message was malformed
        e.printStackTrace()
        null
    }
}

// TODO - when Kotlin 1.5 comes out, use inline/value classes to ensure type-safety
internal fun createUserInfoMessage(userId: String?, logUserId: String?) =
    UserInfo
        .newBuilder()
        .apply {
            userId?.let { setUserId(it) }
            logUserId?.let { setLogUserId(it) }
        }
        .build()
