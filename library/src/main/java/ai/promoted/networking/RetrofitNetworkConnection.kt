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

// TODO - add custom OkHttpClient w/ interceptors for telemetry
internal class RetrofitNetworkConnection : NetworkConnection {
    private var apiUrlPair: Pair<String, PromotedApi>? = null

    override suspend fun send(request: PromotedApiRequest): Completable {
        return try {
            getApiForUrl(request.url)
                .postData(
                    request.url,
                    request.headers,
                    request.bodyData
                )

            Success()
        } catch (e: Throwable) {
            Failure(e)
        }
    }

    private fun getApiForUrl(url: String): PromotedApi {
        val lastUrlAndApi = apiUrlPair
        return when {
            lastUrlAndApi == null -> buildAndSetNewApi(url)
            lastUrlAndApi.first != url -> buildAndSetNewApi(url)
            else -> lastUrlAndApi.second
        }
    }

    private fun buildAndSetNewApi(url: String): PromotedApi {
        val api = Retrofit.Builder()
            .baseUrl(url)
            .build()
            .create<PromotedApi>()

        apiUrlPair = url to api

        return api
    }
}