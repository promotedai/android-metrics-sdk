package ai.promoted.networking

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
    private var urlApiPair: Pair<String, PromotedApi>? = null

    override suspend fun send(request: PromotedApiRequest) =
        getApiForUrl(request.url)
            .postData(
                request.url,
                request.headers,
                request.bodyData
            )

    private fun getApiForUrl(url: String): PromotedApi {
        val lastUrlAndApi = urlApiPair
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

        urlApiPair = url to api

        return api
    }
}