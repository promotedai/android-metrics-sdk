package ai.promoted.metrics.id

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

/**
 * These tests are just as much for learning/demonstrating the behavior of random UUID vs.
 * UUID (v3) nameFromBytes(byte{]) as they are for verifying behavior of the UuidGenerator class.
 */
class UuidGeneratorTest {
    private val generator = UuidGenerator()

    @Test
    fun `UUID is random every time when basedOn is null`() {
        /* Small-ish test of 1000 iterations on random */

        // When we generate 1000 new UUIDs with basedOn equaling null
        val uuids = mutableListOf<String>()
        repeat(1000) {
            uuids.add(generator.newId(basedOn = null))
        }

        // Then they're all distinct
        assertThat(uuids.size, equalTo(1000))
        assertThat(uuids.distinct().size, equalTo(1000))
    }

    @Test
    fun `UUID is deterministic every time when basedOn is provided`() {
        /* Small-ish test of 1000 iterations on nameFromUUIDBytes */

        // When we generate 1000 new UUIDs with basedOn equaling "test-insertion-id"
        val basedOn = "test-insertion-id"
        val uuids = mutableListOf<String>()
        repeat(1000) {
            uuids.add(generator.newId(basedOn = basedOn))
        }

        // Then there is only one distinct UUID
        assertThat(uuids.size, equalTo(1000))
        assertThat(uuids.distinct().size, equalTo(1))
    }

    @Test
    fun `UUID is deterministic every time when basedOn is an empty string`() {
        /* Small-ish test of 1000 iterations on nameFromUUIDBytes */

        // When we generate 1000 new UUIDs with basedOn equaling empty
        val basedOn = ""
        val uuids = mutableListOf<String>()
        repeat(1000) {
            uuids.add(generator.newId(basedOn = basedOn))
        }

        // Then there is only one distinct UUID
        assertThat(uuids.size, equalTo(1000))
        assertThat(uuids.distinct().size, equalTo(1))
    }

    /**
     * This is just to verify that the default argument / no argument passed for 
     * UuidGenerator.newId() results in a random UUID.
     */
    @Test
    fun `UUID is random every time when basedOn is not provided (default null argument validation)`() {
        /* Small-ish test of 1000 iterations on random */

        // When we generate 1000 new UUIDs with basedOn not being provided
        val uuids = mutableListOf<String>()
        repeat(1000) {
            uuids.add(generator.newId())
        }

        // Then they're all distinct
        assertThat(uuids.size, equalTo(1000))
        assertThat(uuids.distinct().size, equalTo(1000))
    }
}