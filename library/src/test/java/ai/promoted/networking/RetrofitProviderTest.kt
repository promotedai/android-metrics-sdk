package ai.promoted.networking

import ai.promoted.http.RetrofitProvider
import junit.framework.Assert.fail
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class RetrofitProviderTest {
    @Test
    fun `Fails when empty URL provided`() = runBlocking {
        try {
            RetrofitProvider().provide("")
            fail("Should have thrown an IllegalArgumentException")
        } catch (error: IllegalArgumentException) {
            error.printStackTrace()
        }
    }

    @Test
    fun `Fails when non-HTTP URL provided`() = runBlocking {
        try {
            RetrofitProvider().provide("ftp://test.com")
            fail("Should have thrown an IllegalArgumentException")
        } catch (error: IllegalArgumentException) {
            error.printStackTrace()
        }
    }

    @Test
    fun `Removes query params from base URL`() = runBlocking {
        val retrofit = RetrofitProvider().provide("http://test.com/?query1=value1")
        val baseUrl = retrofit.baseUrl()
        val baseUrlString = baseUrl.toString()
        assertThat(baseUrl.querySize(), equalTo(0))
        assertThat(baseUrlString.last(), equalTo('/'))
        assertThat(baseUrlString[baseUrlString.lastIndex - 1], not('/'))
    }

    @Test
    fun `Appends slash when URL does not end in slash`() = runBlocking {
        val retrofit = RetrofitProvider().provide("http://test.com")
        val baseUrl = retrofit.baseUrl()
        val baseUrlString = baseUrl.toString()
        assertThat(baseUrl.querySize(), equalTo(0))
        assertThat(baseUrlString.last(), equalTo('/'))
        assertThat(baseUrlString[baseUrlString.lastIndex - 1], not('/'))
    }
}
