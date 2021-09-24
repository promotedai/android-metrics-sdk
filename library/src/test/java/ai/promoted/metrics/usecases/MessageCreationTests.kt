package ai.promoted.metrics.usecases

import ai.promoted.ActionData
import ai.promoted.metrics.InternalActionData
import ai.promoted.platform.Clock
import ai.promoted.proto.event.ActionType
import io.mockk.every
import io.mockk.mockk
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

/**
 * The purpose of this class is *not* to unit test every single create* function, as most of it
 * is just passing data from an outside source into the protobuf message builders. Rather, this
 * class should be used to test specialized message creation logic that can vary based on the data
 * being passed in. For example, for an Action message, there should be an additional
 * navigateAction message appended to the top-level Action message if the top-level Action is of
 * type NAVIGATE.
 */
class MessageCreationTests {
    private val clock = mockk<Clock> { every { currentTimeMillis } returns 0L }

    @Test
    fun `Should add navigateAction if action type is NAVIGATE`() {
        // When
        val message = createActionMessage(
            clock = clock,
            internalActionData = InternalActionData(
                name = "TestNav",
                type = ActionType.NAVIGATE,
                actionId = "test",
                sessionId = "test",
                viewId = "test",
                impressionId = "test"
            ),
            actionData = ActionData.Builder(
                targetUrl = "http://google.com"
            ).build(null)
        )

        // Then
        assertThat(message.navigateAction, notNullValue())
        assertThat(message.navigateAction.targetUrl, equalTo("http://google.com"))
    }

    @Test
    fun `Should set element ID to action's name if element ID is null`() {
        // When
        val message = createActionMessage(
            clock = clock,
            internalActionData = InternalActionData(
                name = "TestNav",
                type = ActionType.CUSTOM_ACTION_TYPE,
                actionId = "test",
                sessionId = "test",
                viewId = "test",
                impressionId = "test"
            ),
            actionData = ActionData.Builder().build(null)
        )

        // Then
        assertThat(message.elementId, equalTo("TestNav"))
    }
}