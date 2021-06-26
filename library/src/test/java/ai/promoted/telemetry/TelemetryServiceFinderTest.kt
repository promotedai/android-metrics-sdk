package ai.promoted.telemetry

import com.google.firebase.analytics.FirebaseAnalytics
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class TelemetryServiceFinderTest {
    @Test
    fun `Should return no-op if FirebaseAnalytics not available`() {
        // Given
        val noFirebaseClassFinder = mockk<ClassFinder> {
            every { exists("com.google.firebase.analytics.FirebaseAnalytics") } returns false
        }
        val serviceFinder = TelemetryServiceFinder(noFirebaseClassFinder)

        // When
        val service = serviceFinder.findAvailableService(mockk())

        // Then
        assertThat(service, instanceOf(NoOpTelemetryService::class.java))
    }

    @Test
    // We cannot obtain an instance of FirebaseAnalytics if we don't have a Context object
    fun `Should return no-op if Context not available, even though Firebase is`() {
        // Given (FirebaseAnalytics should be part of test dependencies to ensure class resolves
        // organically)
        val serviceFinder = TelemetryServiceFinder(ClassFinder())

        // When
        val service = serviceFinder.findAvailableService(null)

        // Then
        assertThat(service, instanceOf(NoOpTelemetryService::class.java))
    }

    @Test
    fun `Should return Firebase telemetry when FirebaseAnalytics & Context available`() {
        // Given (FirebaseAnalytics should be part of test dependencies to ensure class resolves
        // organically)
        val serviceFinder = TelemetryServiceFinder(ClassFinder())
        // Required to avoid issues with actually trying to obtain FirebaseAnalytics in a
        // non-Android environment
        mockkStatic(FirebaseAnalytics::class)
        every { FirebaseAnalytics.getInstance(any()) } returns mockk()

        // When
        val service = serviceFinder.findAvailableService(mockk())

        // Then
        assertThat(service, instanceOf(FirebaseTelemetryService::class.java))
    }
}