package ai.promoted.metrics.id

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.not
import org.junit.Assert.assertThat
import org.junit.Test

class AdvanceableIdTest {
    @Test
    fun `Should retain initial ID after advance when skipFirstAdvancement is true`() {
        // Given
        val advanceableId = AdvanceableId(true, UuidGenerator())
        val initialValue = advanceableId.currentValue

        // When
        val advancedValue = advanceableId.advance()

        // Then
        assertThat(advancedValue, equalTo(initialValue))
        assertThat(advanceableId.currentValue, equalTo(initialValue))
    }

    @Test
    fun `Should advance from initial ID after second advance, when skipFirstAdvancement is true`() {
        // Given
        val advanceableId = AdvanceableId(true, UuidGenerator())
        val initialValue = advanceableId.currentValue
        val firstAdvancedValue = advanceableId.advance()
        assertThat(firstAdvancedValue, equalTo(initialValue))

        // When
        val secondAdvancedValue = advanceableId.advance()

        // Then
        assertThat(secondAdvancedValue, not(initialValue))
    }

    @Test
    fun `Should advance from initial ID after first advance, when skipFirstAdvancement is false`() {
        // Given
        val advanceableId = AdvanceableId(skipFirstAdvancement = false, UuidGenerator())
        val initialValue = advanceableId.currentValue

        // When
        val firstAdvancedValue = advanceableId.advance()

        // Then
        assertThat(firstAdvancedValue, not(initialValue))
    }
}
