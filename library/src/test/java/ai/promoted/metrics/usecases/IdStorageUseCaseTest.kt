package ai.promoted.metrics.usecases

import ai.promoted.metrics.storage.IdStorage
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class IdStorageUseCaseTest {
    private val dummyDiskStorage = object : IdStorage {
        override var userId: String = ""
        override var logUserId: String = ""
    }

    private val useCase = IdStorageUseCase(dummyDiskStorage)

    @Test
    fun `User ID is updated in memory & stored to disk`() {
        assertThat(useCase.currentUserId, equalTo(""))
        assertThat(dummyDiskStorage.userId, equalTo(""))

        useCase.updateUserId("test")

        assertThat(useCase.currentUserId, equalTo("test"))
        assertThat(dummyDiskStorage.userId, equalTo("test"))
    }

    @Test
    fun `Log user ID is updated in memory & stored to disk`() {
        assertThat(useCase.currentLogUserId, equalTo(""))
        assertThat(dummyDiskStorage.logUserId, equalTo(""))

        useCase.updateLogUserId("test")

        assertThat(useCase.currentLogUserId, equalTo("test"))
        assertThat(dummyDiskStorage.logUserId, equalTo("test"))
    }
}