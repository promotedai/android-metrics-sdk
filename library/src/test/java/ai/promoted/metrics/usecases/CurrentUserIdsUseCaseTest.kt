package ai.promoted.metrics.usecases

import ai.promoted.platform.KeyValueStorage
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

private const val KEY_USER_ID = "ai.promoted.user_id"
private const val KEY_LOG_USER_ID = "ai.promoted.log_user_id"

class CurrentUserIdsUseCaseTest {
    private val dummyDiskStorage = object : KeyValueStorage {
        private val storage = mutableMapOf<String, String>()

        override fun set(key: String, value: String) {
            storage[key] = value
        }

        override fun get(key: String, defaultValue: String): String {
            return storage[key] ?: defaultValue
        }
    }

    private val useCase = CurrentUserIdsUseCase(dummyDiskStorage)

    @Test
    fun `User ID is updated in memory & stored to disk`() {
        assertThat(useCase.currentUserId, equalTo(""))
        assertThat(dummyDiskStorage.get(KEY_USER_ID, ""), equalTo(""))

        useCase.updateUserId("test")

        assertThat(useCase.currentUserId, equalTo("test"))
        assertThat(dummyDiskStorage.get(KEY_USER_ID, ""), equalTo("test"))
    }

    @Test
    fun `Log user ID is updated in memory & stored to disk`() {
        assertThat(useCase.currentLogUserId, equalTo(""))
        assertThat(dummyDiskStorage.get(KEY_LOG_USER_ID, ""), equalTo(""))

        useCase.updateLogUserId("test")

        assertThat(useCase.currentLogUserId, equalTo("test"))
        assertThat(dummyDiskStorage.get(KEY_LOG_USER_ID, ""), equalTo("test"))
    }
}