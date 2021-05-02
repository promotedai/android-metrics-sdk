package ai.promoted.networking

import okhttp3.HttpUrl
import okhttp3.RequestBody
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
        @Body data: RequestBody
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
                RequestBody.create(null, request.bodyData)
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
            .baseUrl(url.requireCorrectBaseUrl())
            .build()
            .create<PromotedApi>()

        urlApiPair = url to api

        return api
    }

    private fun String.requireCorrectBaseUrl(): String {
        when {
            this.isBlank() -> "No URL provided"
            !this.startsWith("http") -> "Non-HTTP URL provided: $this"
            else -> null
        }?.let { message -> throw IllegalArgumentException(message) }

        val httpUrl = HttpUrl.get(this)

        if(httpUrl.querySize() > 0) throw IllegalArgumentException("Logging URLs with queries not yet supported")

        // Retrofit requires that the URL end in "/"; rather than requiring this for our users,
        // we will attempt to add Retrofit's required "/" by doing the same check they do, but
        // appending the URL's path instead of throwing an exception
        return if(this.last() != '/') "$this/"
        else this
    }
}
