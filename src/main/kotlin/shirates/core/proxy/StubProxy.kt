package shirates.core.proxy

import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import shirates.core.driver.TestMode
import shirates.core.driver.testContext
import shirates.core.exception.TestEnvironmentException
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog

/**
 * StubProxy
 */
object StubProxy {

    /**
     * setDataPattern
     */
    fun setDataPattern(apiName: String, dataPatternName: String): StubProxyResponse {

        if (TestMode.isNoLoadRun) {
            return StubProxyResponse()
        }

        val url = "${testContext.profile.stubServerUrl}/management/setDataPattern".toHttpUrlOrNull()!!.newBuilder()
            .addQueryParameter("apiName", apiName)
            .addQueryParameter("dataPatternName", dataPatternName)
            .build()
        val stubProxyResponse = request(url)
        return stubProxyResponse
    }

    /**
     * getDataPattern
     */
    fun getDataPattern(apiName: String): StubProxyResponse {

        if (TestMode.isNoLoadRun) {
            return StubProxyResponse()
        }

        val url = "${testContext.profile.stubServerUrl}/management/getDataPattern".toHttpUrlOrNull()!!.newBuilder()
            .addQueryParameter("apiName", apiName)
            .build()
        val stubProxyResponse = request(url)
        return stubProxyResponse
    }

    /**
     * resetDataPattern
     */
    fun resetDataPattern(): StubProxyResponse {

        if (TestMode.isNoLoadRun) {
            return StubProxyResponse()
        }

        val url =
            "${testContext.profile.stubServerUrl}/management/resetDataPattern".toHttpUrlOrNull()!!.newBuilder()
                .build()
        val stubProxyResponse = request(url)
        return stubProxyResponse
    }

    /**
     * listDataPattern
     */
    fun listDataPattern(): StubProxyResponse {

        if (TestMode.isNoLoadRun) {
            return StubProxyResponse()
        }

        val url =
            "${testContext.profile.stubServerUrl}/management/listDataPattern".toHttpUrlOrNull()!!.newBuilder()
                .build()
        val stubProxyResponse = request(url)
        return stubProxyResponse
    }

    fun request(url: HttpUrl): StubProxyResponse {
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()

        var response: Response? = null
        try {
            response = client.newCall(request).execute()
            val r = StubProxyResponse(response)
            return r
        } catch (t: Throwable) {
            throw TestEnvironmentException(message(id = "failedToConnectToStubTool", arg1 = "$url"), t)
        } finally {
            try {
                response?.close()
            } catch (t: Throwable) {
                TestLog.error(t)
            }
        }
    }

    class StubProxyResponse(
        val response: Response? = null
    ) {
        var resultBytes: ByteArray? = null

        val resultString: String?
            get() {
                if (resultBytes == null) {
                    return null
                }
                return String(resultBytes!!)
            }

        val code: Int?
            get() {
                return response?.code
            }

        init {
            if (response != null) {
                resultBytes = response.body?.bytes()
            }
        }
    }
}