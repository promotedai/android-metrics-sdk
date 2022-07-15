package ai.promoted.networking

import ai.promoted.PromotedApiRequest
import ai.promoted.http.RetrofitNetworkConnection
import ai.promoted.http.RetrofitPromotedApi
import ai.promoted.http.RetrofitProvider
import ai.promoted.mockkRelaxedUnit
import io.mockk.CapturingSlot
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import okhttp3.RequestBody
import okio.Buffer
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.create

class RetrofitNetworkConnectionTest {
    private val urlSlot = CapturingSlot<String>()
    private val headersSlot = CapturingSlot<Map<String, String>>()
    private val bodySlot = CapturingSlot<RequestBody>()
    private val api: RetrofitPromotedApi = mockkRelaxedUnit {
        coEvery { postData(capture(urlSlot), capture(headersSlot), capture(bodySlot)) } returns Unit
    }

    private val retrofit: Retrofit = mockkRelaxedUnit {
        every { create<RetrofitPromotedApi>() } returns api
    }

    private val retrofitProvider: RetrofitProvider = mockkRelaxedUnit {
        every { provide(any()) } returns retrofit
    }

    @Test
    fun `PromotedApiRequest is properly mapped over to Retrofit`() = runBlocking {
        // Given a Retrofit-based connection
        val connection = RetrofitNetworkConnection(retrofitProvider)

        // When we send the PromotedApiRequest
        connection.send(
            PromotedApiRequest(
                url = "https://test.com/?someParam=someValue",
                headers = mapOf("header1" to "value1"),
                bodyData = "test".toByteArray()
            )
        )

        // Then the request content (url, headers, body, etc.) all mirror what was provided
        // by the PromotedApiRequest
        coVerify(exactly = 1) {
            api.postData(
                url = "https://test.com/?someParam=someValue",
                headers = mapOf("header1" to "value1"),
                data = any()
            )
        }
        val buffer = Buffer()
        bodySlot.captured.writeTo(buffer)
        assertThat("test".toByteArray(), equalTo(buffer.readByteArray()))
    }

    @Test
    fun `New Retrofit or API objects are not unnecessarily generated`() = runBlocking {
        // Given a retrofit-based connection that has already sent a request once
        val connection = RetrofitNetworkConnection(retrofitProvider)
        connection.send(
            PromotedApiRequest(
                url = "https://test.com/",
                headers = mapOf("header1" to "value1"),
                bodyData = "test".toByteArray()
            )
        )

        // When we send a request again, with the same URL (though perhaps with diff headers/body)
        connection.send(
            PromotedApiRequest(
                url = "https://test.com/",
                headers = mapOf("header2" to "value2"),
                bodyData = "test2".toByteArray()
            )
        )

        // Then Retrofit was only provided once,
        // and the PromotedApi object was only created once
        verify(exactly = 1) { retrofitProvider.provide("https://test.com/") }
        verify(exactly = 1) { retrofit.create<RetrofitPromotedApi>() }
    }

    @Test
    fun `New API object is generated when URL changes`() = runBlocking {
        // Given a retrofit-based connection that has already sent a request once
        val connection = RetrofitNetworkConnection(retrofitProvider)
        connection.send(
            PromotedApiRequest(
                url = "https://test.com/",
                headers = mapOf("header1" to "value1"),
                bodyData = "test".toByteArray()
            )
        )

        // When we send a request again, with a different URL (though same headers/body)
        connection.send(
            PromotedApiRequest(
                url = "https://test2.com/",
                headers = mapOf("header1" to "value1"),
                bodyData = "test".toByteArray()
            )
        )

        // Then Retrofit is re-built for the new base URL (provide() is called twice)
        // And a new PromotedApi is created (create() is called twice)
        verify(exactly = 1) { retrofitProvider.provide("https://test.com/") }
        verify(exactly = 1) { retrofitProvider.provide("https://test2.com/") }
        verify(exactly = 2) { retrofit.create<RetrofitPromotedApi>() }
    }
}
