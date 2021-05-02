package ai.promoted.networking

import junit.framework.Assert.fail
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
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
    fun `Fails when URL has query params`() = runBlocking {
        try {
            RetrofitProvider().provide("http://test.com/?query1=value1")
            fail("Should have thrown an IllegalArgumentException")
        } catch (error: IllegalArgumentException) {
            error.printStackTrace()
        }
    }

    @Test
    fun `Appends slash when URL does not end in slash`() = runBlocking {
        val retrofit = RetrofitProvider().provide("http://test.com")
        assertThat(retrofit.baseUrl().toString().last(), equalTo('/'))
    }
}