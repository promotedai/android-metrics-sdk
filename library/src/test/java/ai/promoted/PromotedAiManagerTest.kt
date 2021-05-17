package ai.promoted

import ai.promoted.di.ConfigurableKoinComponent
import ai.promoted.sdk.NoOpSdk
import ai.promoted.sdk.PromotedAiSdk
import ai.promoted.sdk.SdkManager
import android.app.Application
import io.mockk.called
import io.mockk.mockk
import io.mockk.verify
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Test
import org.koin.core.context.stopKoin
import org.koin.core.module.Module
import org.koin.dsl.module

class PromotedAiManagerTest {
    private val application: Application = mockk()

    // Using this to ensure no Koin instances are living longer than each test
    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `Uses no-op if initialize() or configure() not called`() {
        // Given a default PromotedAiManager (using prod/runtime Koin and everything)
        val manager = object : SdkManager(){}

        // When we access the instance
        // or try to call some function on the default PromotedAi interface
        // Then the PromotedAi instance is a NoOp
        assertThat(manager.sdkInstance, instanceOf(NoOpSdk::class.java))
        PromotedAi.startSession()
        assertThat(manager.sdkInstance, instanceOf(NoOpSdk::class.java))
    }

    @Test
    fun `PromotedAi is no longer called after logging is disabled, and a no-op instance is being used`() {
        // Given a PromotedAiManager that is configured to return one PromotedAi initially,
        // but will always return a second one on subsequent injects,
        // and that the PromotedAiManager has been configured once
        var creationCount = 0
        val firstPromotedAi: PromotedAiSdk = mockk(relaxUnitFun = true)
        val secondPromotedAi: PromotedAiSdk = mockk(relaxUnitFun = true)
        val manager = object : SdkManager(object : ConfigurableKoinComponent() {
            override fun buildModules(config: ClientConfig): List<Module> = listOf(
                module {
                    factory<PromotedAiSdk> {
                        creationCount++
                        if (creationCount == 1) firstPromotedAi
                        else secondPromotedAi
                    }
                }
            )
        }) {}
        manager.configure(application) { loggingEnabled = true }

        // When it is re-configured to disable logging
        manager.configure(application) { loggingEnabled = false }

        // Then the second promoted ai is never called
        // and the actual instance is a NoOp (it's not even the second PromotedAi that DI provided)
        verify {
            secondPromotedAi wasNot called
        }
        assertThat(manager.sdkInstance, instanceOf(NoOpSdk::class.java))
    }

    @Test
    fun `Existing PromtoedAi is shut down upon reconfigure`() {
        // Given a PromotedAiManager that is configured to return one PromotedAi initially,
        // but will always return a second one on subsequent injects,
        // and that the PromotedAiManager has been configured once
        var creationCount = 0
        val firstPromotedAi: PromotedAiSdk = mockk(relaxUnitFun = true)
        val secondPromotedAi: PromotedAiSdk = mockk(relaxUnitFun = true)
        val manager = object : SdkManager(object : ConfigurableKoinComponent() {
            override fun buildModules(config: ClientConfig): List<Module> = listOf(
                module {
                    factory<PromotedAiSdk> {
                        creationCount++
                        if (creationCount == 1) firstPromotedAi
                        else secondPromotedAi
                    }
                }
            )
        }) {}
        manager.configure(application) { loggingEnabled = true }

        // When it is re-configured
        manager.configure(application) { loggingEnabled = false }

        // Then the initial PromotedAi was shutdown
        verify(exactly = 1) { firstPromotedAi.shutdown() }
    }

    @Test
    fun `All components shut down when call shutdown()`() {
        // Given a PromotedAiManager that is configured to return one PromotedAi initially,
        // but will always return a second one on subsequent injects,
        // and that the PromotedAiManager has been configured once
        val promotedAi: PromotedAiSdk = mockk(relaxUnitFun = true)
        val koin = object : ConfigurableKoinComponent() {
            fun startedKoinApp() = super.startedKoinApplication
            override fun buildModules(config: ClientConfig): List<Module> = listOf(
                module {
                    single { config }
                    factory<PromotedAiSdk> { promotedAi }
                }
            )
        }
        val manager = object : SdkManager(koin) {}
        manager.configure(application) { loggingEnabled = true }

        // When it is shut down
        manager.shutdown()

        // Then all components are shut down
        verify(exactly = 1) { promotedAi.shutdown() }
        assertThat(koin.startedKoinApp(), nullValue())
    }
}