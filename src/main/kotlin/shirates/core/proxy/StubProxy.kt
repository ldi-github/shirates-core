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

    var profile: String? = null

    /**
     * isProfileActivated
     */
    val isProfileActivated: Boolean
        get() {
            return profile.isNullOrBlank().not()
        }

    /**
     * activateProfile
     */
    fun activateProfile(profile: String): String {

        this.profile = profile
        return this.profile!!
    }

    /**
     * registerInstance
     */
    fun registerInstance(instanceKey: String, profile: String): StubProxyResponse {

        if (TestMode.isNoLoadRun) {
            return StubProxyResponse()
        }

        val builder =
            "${testContext.profile.stubServerUrl}/management/registerInstance".toHttpUrlOrNull()!!.newBuilder()
                .addQueryParameter("instanceKey", instanceKey)
                .addQueryParameter("profile", profile)
        val url = builder.build()
        val stubProxyResponse = request(url)
        if (stubProxyResponse.code == 200) {
            activateProfile(profile = profile)
        }
        return stubProxyResponse
    }

    /**
     * getInstanceInfo
     */
    fun getInstanceInfo(profileOrInstanceKeyPrefix: String): StubProxyResponse {

        if (TestMode.isNoLoadRun) {
            return StubProxyResponse()
        }

        val builder = "${testContext.profile.stubServerUrl}/management/getInstanceInfo".toHttpUrlOrNull()!!.newBuilder()
            .addQueryParameter("profileOrInstanceKeyPrefix", profileOrInstanceKeyPrefix)
        val url = builder.build()
        val stubProxyResponse = request(url)
        return stubProxyResponse
    }

    /**
     * getInstanceProfileMap
     */
    fun getInstanceProfileMap(): StubProxyResponse {

        if (TestMode.isNoLoadRun) {
            return StubProxyResponse()
        }

        val builder =
            "${testContext.profile.stubServerUrl}/management/getInstanceProfileMap".toHttpUrlOrNull()!!.newBuilder()
        val url = builder.build()
        val stubProxyResponse = request(url)
        return stubProxyResponse
    }

    /**
     * resetInstance
     */
    fun resetInstance(): StubProxyResponse {

        if (TestMode.isNoLoadRun) {
            return StubProxyResponse()
        }

        val builder =
            "${testContext.profile.stubServerUrl}/management/resetInstance".toHttpUrlOrNull()!!.newBuilder()
        if (isProfileActivated) {
            builder.addQueryParameter("profile", profile)
        }
        val url = builder.build()
        val stubProxyResponse = request(url)
        return stubProxyResponse
    }

    /**
     * removeInstance
     */
    fun removeInstance(profileOrInstanceKeyPrefix: String): StubProxyResponse {

        if (TestMode.isNoLoadRun) {
            return StubProxyResponse()
        }

        val builder =
            "${testContext.profile.stubServerUrl}/management/removeInstance".toHttpUrlOrNull()!!.newBuilder()
                .addQueryParameter("profileOrInstanceKeyPrefix", profileOrInstanceKeyPrefix)
        val url = builder.build()
        val stubProxyResponse = request(url)
        if (stubProxyResponse.code == 200) {
            this.profile = null
        }
        return stubProxyResponse
    }

    /**
     * resetDataPattern
     */
    fun resetDataPattern(): StubProxyResponse {

        if (TestMode.isNoLoadRun) {
            return StubProxyResponse()
        }

        val builder =
            "${testContext.profile.stubServerUrl}/management/resetDataPattern".toHttpUrlOrNull()!!.newBuilder()
        if (isProfileActivated) {
            builder.addQueryParameter("profile", profile)
        }
        val url = builder.build()
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

        val builder =
            "${testContext.profile.stubServerUrl}/management/listDataPattern".toHttpUrlOrNull()!!.newBuilder()
        if (isProfileActivated) {
            builder.addQueryParameter("profile", profile)
        }
        val url = builder.build()
        val stubProxyResponse = request(url)
        return stubProxyResponse
    }

    /**
     * setDataPattern
     */
    fun setDataPattern(urlPathOrApiName: String, dataPatternName: String): StubProxyResponse {

        if (TestMode.isNoLoadRun) {
            return StubProxyResponse()
        }

        val builder = "${testContext.profile.stubServerUrl}/management/setDataPattern".toHttpUrlOrNull()!!.newBuilder()
            .addQueryParameter("urlPathOrApiName", urlPathOrApiName)
            .addQueryParameter("dataPatternName", dataPatternName)
        if (isProfileActivated) {
            builder.addQueryParameter("profile", profile)
        }
        val url = builder.build()
        val stubProxyResponse = request(url)
        return stubProxyResponse
    }

    /**
     * getDataPattern
     */
    fun getDataPattern(urlPathOrApiName: String): StubProxyResponse {

        if (TestMode.isNoLoadRun) {
            return StubProxyResponse()
        }

        val builder = "${testContext.profile.stubServerUrl}/management/getDataPattern".toHttpUrlOrNull()!!.newBuilder()
            .addQueryParameter("urlPathOrApiName", urlPathOrApiName)
        if (isProfileActivated) {
            builder.addQueryParameter("profile", profile)
        }
        val url = builder.build()
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