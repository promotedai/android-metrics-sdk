package ai.promoted.networking

import okhttp3.HttpUrl
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.Body
import retrofit2.http.HeaderMap
import retrofit2.http.POST
import retrofit2.http.Url

/**
 * API interface definition for Retrofit.
 */
internal interface PromotedApi {
    @POST
    suspend fun postData(
        @Url url: String,
        @HeaderMap headers: Map<String, String>,
        @Body data: RequestBody
    )
}

/**
 * Retrofit has some stringent requirements regarding what you use for your base URL when building
 * a [Retrofit] instance. This class receives a URL (i.e. via client configuration) and does its
 * best to ensure it is properly formatted, so that Retrofit does not throw any exceptions when
 * being built with the provided URL as its base URL.
 *
 * One should also note that the URL itself is dynamic anyway, as defined by the @Url parameter in
 * [PromotedApi.postData]; however, Retrofit still requires a base URL to be provided when being
 * built.
 */
internal class RetrofitProvider {
    fun provide(url: String): Retrofit {
        // TODO - add custom OkHttpClient w/ interceptors for telemetry
        return Retrofit.Builder()
            .baseUrl(url.requireCorrectBaseUrl())
            .build()
    }

    private fun String.requireCorrectBaseUrl(): String {
        when {
            this.isBlank() -> "No URL provided"
            !this.startsWith("http") -> "Non-HTTP URL provided: $this"
            else -> null
        }?.let { message -> throw IllegalArgumentException(message) }

        val httpUrl = HttpUrl.get(this)

        // Because we are building a base URL, we must only have a path without any query
        // parameters. This removes any query parameters and only provides the base path. This is
        // fine to do because, at request time, the URL is dynamically defined anyway using the
        // @Url function param.
        val stringToFormat = if (httpUrl.querySize() > 0) {
            val newHttpUrlBuilder = httpUrl.newBuilder()
            httpUrl.queryParameterNames().forEach {
                newHttpUrlBuilder.removeAllQueryParameters(it)
            }
            newHttpUrlBuilder.build().toString()
        } else this

        // Retrofit requires that the URL end in "/"; rather than requiring this for our users,
        // we will attempt to add Retrofit's required "/" by doing the same check they do, but
        // appending the URL's path instead of throwing an exception
        return if (stringToFormat.last() != '/') "$stringToFormat/"
        else stringToFormat
    }
}

/**
 * An implementation of [NetworkConnection] that executes the [PromotedApiRequest] using Retrofit.
 *
 * This class will only build a [Retrofit] instance & create a [PromotedApi] instance when the
 * URL in [PromotedApiRequest] has changed since the last call to [send] (or on the first call to
 * [send]). Otherwise, it will retain its reference to the originally create [PromotedApi] object
 * so as to preserve memory.
 */
internal class RetrofitNetworkConnection(
    private val retrofitProvider: RetrofitProvider
) : NetworkConnection {
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
        val api = retrofitProvider.provide(url).create<PromotedApi>()

        urlApiPair = url to api

        return api
    }
}
