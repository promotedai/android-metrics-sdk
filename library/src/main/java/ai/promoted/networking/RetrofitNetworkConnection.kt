package ai.promoted.networking

import ai.promoted.arch.Completable
import ai.promoted.arch.Failure
import ai.promoted.arch.Success
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.Body
import retrofit2.http.HeaderMap
import retrofit2.http.POST
import retrofit2.http.Url

internal interface PromotedApi {
    @POST
    suspend fun postData(
        @Url url: String,
        @HeaderMap headers: Map<String, String>,
        @Body data: ByteArray
    )
}

internal class RetrofitNetworkConnection: NetworkConnection {
    // TODO - add custom OkHttpClient w/ interceptors for telemetry
    private val api: PromotedApi =
        Retrofit.Builder()
            .build()
            .create()

    override suspend fun send(request: PromotedApiRequest): Completable {
        return try {
            api.postData(
                request.url,
                request.headers,
                request.bodyData
            )

            Success()
        } catch (e: Throwable) {
            Failure(e)
        }
    }
}