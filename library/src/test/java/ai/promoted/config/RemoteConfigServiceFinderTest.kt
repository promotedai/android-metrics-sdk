package ai.promoted.config

import ai.promoted.runtime.ClassFinder
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class RemoteConfigServiceFinderTest {
    @Test
    fun `Should return no-op if FirebaseRemoteConfig not available`() {
        // Given
        val noFirebaseClassFinder = mockk<ClassFinder> {
            every { exists("com.google.firebase.remoteconfig.FirebaseRemoteConfig") } returns false
        }
        val serviceFinder = RemoteConfigServiceFinder(noFirebaseClassFinder)

        // When
        val service = serviceFinder.findAvailableService()

        // Then
        assertThat(service, instanceOf(NoOpRemoteConfigService::class.java))
    }

    @Test
    fun `Should return Firebase config service when FirebaseRemoteConfig is available`() {
        // Given (FirebaseRemoteConfig should be part of test dependencies to ensure class resolves
        // organically)
        val serviceFinder = RemoteConfigServiceFinder(ClassFinder())
        // Required to avoid issues with actually trying to obtain FirebaseRemoteConfig in a
        // non-Android environment
        mockkStatic(FirebaseRemoteConfig::class)
        every { FirebaseRemoteConfig.getInstance() } returns mockk()

        // When
        val service = serviceFinder.findAvailableService()

        // Then
        assertThat(
            service,
            instanceOf(FirebaseRemoteConfigService::class.java)
        )
    }
}