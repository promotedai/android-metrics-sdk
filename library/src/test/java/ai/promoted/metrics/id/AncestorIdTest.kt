package ai.promoted.metrics.id

import ai.promoted.mockkRelaxedUnit
import io.mockk.every
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.nullValue
import org.junit.Assert.assertThat
import org.junit.Test

class AncestorIdTest {
    private val generatedId = "test-id"
    private val idGenerator: IdGenerator = mockkRelaxedUnit {
        every { newId(any()) } returns generatedId
    }
    private val ancestorId = AncestorId(idGenerator)

    @Test
    fun `Check current value before override or advance`() {
        // Given a new ancestor ID that has not been overridden or advanced
        // When we get the current value
        // Then it is empty/null
        assertThat(ancestorId.currentValue, equalTo(""))
        assertThat(ancestorId.currentValueOrNull, nullValue())
    }

    @Test
    fun `Check pending value before override or advance`() {
        // Given a new ancestor ID that has not been overridden or advanced
        // When we get the current or pending value
        // Then the current value is empty, but the pendingOrCurrentValue is a valid ID
        assertThat(ancestorId.currentValue, equalTo(""))
        assertThat(ancestorId.currentOrPendingValue, equalTo(generatedId))
    }

    @Test
    fun `Current value reflects override`() {
        // When
        ancestorId.override("overridden-id")
        // Then
        assertThat(ancestorId.isOverridden, equalTo(true))
        assertThat(ancestorId.currentValue, equalTo("overridden-id"))
    }

    @Test
    fun `Advance is ignored if overridden`() {
        // Given
        ancestorId.override("overridden-id")

        // When
        ancestorId.advance()

        // Then
        assertThat(ancestorId.isOverridden, equalTo(true))
        assertThat(ancestorId.currentValue, equalTo("overridden-id"))
    }
}