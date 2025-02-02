package shirates.core.proxy

import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import shirates.core.logging.TestLog
import java.time.Duration

object HttpProxy {

    /**
     * getResponse
     */
    fun getResponse(url: HttpUrl): Response {

        val request = Request.Builder()
            .url(url)
            .build()
        val client = OkHttpClient().newBuilder()
            .connectTimeout(Duration.ofMillis(8000))
            .build()

        var response: Response? = null
        try {
            response = client.newCall(request).execute()
            return response
        } finally {
            try {
                response?.close()
            } catch (t: Throwable) {
                TestLog.error(t)
            }
        }
    }

    /**
     * getResponseBody
     */
    fun getResponseBody(url: HttpUrl): String {

        val request = Request.Builder()
            .url(url)
            .build()
        val client = OkHttpClient().newBuilder()
            .connectTimeout(Duration.ofMillis(8000))
            .build()

        var response: Response? = null
        try {
            response = client.newCall(request).execute()
            return response.body!!.string()
        } finally {
            try {
                response?.close()
            } catch (t: Throwable) {
                TestLog.error(t)
            }
        }
    }

}