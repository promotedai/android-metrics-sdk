package ai.promoted.http

import ai.promoted.NetworkConnection
import ai.promoted.PromotedApiRequest
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.create

/**
 * An implementation of [NetworkConnection] that executes the [PromotedApiRequest] using Retrofit.
 *
 * This class will only build a [Retrofit] instance & create a [RetrofitPromotedApi] instance when the
 * URL in [PromotedApiRequest] has changed since the last call to [send] (or on the first call to
 * [send]). Otherwise, it will retain its reference to the originally create [RetrofitPromotedApi] object
 * so as to preserve memory.
 */
internal class RetrofitNetworkConnection(
    private val retrofitProvider: RetrofitProvider
) : NetworkConnection {
    private var urlApiPair: Pair<String, RetrofitPromotedApi>? = null

    override suspend fun send(request: PromotedApiRequest) =
        getApiForUrl(request.url)
            .postData(
                request.url,
                request.headers,
                RequestBody.create(null, request.bodyData)
            )

    private fun getApiForUrl(url: String): RetrofitPromotedApi {
        val lastUrlAndApi = urlApiPair
        return when {
            lastUrlAndApi == null -> buildAndSetNewApi(url)
            lastUrlAndApi.first != url -> buildAndSetNewApi(url)
            else -> lastUrlAndApi.second
        }
    }

    private fun buildAndSetNewApi(url: String): RetrofitPromotedApi {
        val api = retrofitProvider.provide(url).create<RetrofitPromotedApi>()

        urlApiPair = url to api

        return api
    }
}
