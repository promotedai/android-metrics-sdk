package ai.promoted.http

import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.HeaderMap
import retrofit2.http.POST
import retrofit2.http.Url

/**
 * API interface definition for Retrofit.
 */
internal interface RetrofitPromotedApi {
    @POST
    suspend fun postData(
        @Url url: String,
        @HeaderMap headers: Map<String, String>,
        @Body data: RequestBody
    )
}
