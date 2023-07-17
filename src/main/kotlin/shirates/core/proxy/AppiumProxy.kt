package shirates.core.proxy

import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import shirates.core.configuration.PropertiesManager
import shirates.core.driver.*
import shirates.core.driver.TestMode.isAndroid
import shirates.core.driver.TestMode.isiOS
import shirates.core.driver.commandextension.getWebElement
import shirates.core.exception.TestDriverException
import shirates.core.logging.Measure
import shirates.core.logging.TestLog
import shirates.core.server.AppiumServerManager
import shirates.core.utility.element.ElementCacheUtility
import shirates.core.utility.sync.RetryContext
import shirates.core.utility.sync.RetryUtility
import shirates.core.utility.sync.WaitUtility
import java.time.Duration
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * AppiumProxy
 */
object AppiumProxy {

    /**
     * getSource
     */
    fun getSource(): TestElement {

        if (TestMode.isNoLoadRun) {
            return TestElement()
        }

        var e = TestElement()
        fun checkState(): Boolean {
            return try {
                val we = e.getWebElement()
                if (isAndroid) {
                    we.getAttribute("package")
                } else {
                    we.getAttribute("label")
                }
                true
            } catch (t: Throwable) {
                if (PropertiesManager.enableGetSourceLog) {
                    TestLog.info("[Error] AppiumProxy.getSource() checkState(): $t $e")
                }
                false
            }
        }

        WaitUtility.doUntilTrue(
            onMaxLoop = {
                throw TestDriverException("AppiumProxy.getSource() reached maxLoop count.(maxLoopCount=${it.maxLoopCount})")
            },
            onTimeout = {
                throw TestDriverException("AppiumProxy.getSource() timed out. (waitSeconds=${it.waitSeconds})")
            }
        ) {
            e = getSourceCore()
            checkState()
        }

        return e
    }

    private fun getSourceCore(): TestElement {

        val ms = Measure("getSourceCore()")
        try {
            var source = ""

            val retryMaxCount =
                AppiumServerManager.appiumSessionStartupTimeoutSeconds / TestDriver.testContext.retryIntervalSeconds
            val action: (RetryContext<Unit>) -> Unit = {
                val c = it.retryCount + 1
                if (PropertiesManager.enableGetSourceLog) {
                    TestLog.info("getSourceCore($c)")
                }
                source = TestDriver.appiumDriver.pageSource
            }
            val retryPredicate: (RetryContext<Unit>) -> Boolean = {
                if (isiOS) {
                    source == "" || source.contains("<AppiumAUT>\n  <XCUIElementTypeApplication")
                } else {
                    source == ""
                }
            }
            RetryUtility.exec(
                retryMaxCount = retryMaxCount.toLong(),
                retryPredicate = retryPredicate,
                onBeforeRetry = {},
                action = action
            )

            if (source.isBlank()) {
                return TestElement()
            }

            source = source.replace("\u000b", "")    // vertical tab is not valid in XML
            if (PropertiesManager.xmlSourceRemovePattern.isNotBlank()) {
                val regex = PropertiesManager.xmlSourceRemovePattern.toRegex()
                source = source.replace(regex, "")
            }

            val e = ElementCacheUtility.createTestElementFromXml(source = source)
            return e
        } finally {
            ms.end()
        }
    }

    /**
     * getSourceXml
     */
    fun getSourceXml(): String {

        val response = getSourceResponse()
        val body = response.body!!.string()

        val responseJson = JSONObject(body)
        val xml = responseJson.get("value")!!.toString()
        return xml
    }

    /**
     * getSourceResponse
     */
    fun getSourceResponse(): Response {

        val d = TestDriver.appiumDriver

        // ex. http://localhost:8201/wd/hub/session/$sessionId/source
        val url = "${d.remoteAddress}/session/${d.sessionId}/source".toHttpUrlOrNull()!!.newBuilder()
            .build()
        val response = getResponse(url)

        return response
    }

    /**
     * getResponseBody
     */
    fun getResponseBody(uri: String): String {

        val response = getResponse(uri = uri)
        val body = response.body!!.string()

        return body
    }

    /**
     * getResponseXml
     */
    fun getResponseXml(uri: String): String {

        val body = getResponseBody(uri)
        val responseJson = JSONObject(body)
        val xml = responseJson.get("value")!!.toString()
        return xml
    }

    /**
     * getResponse
     */
    fun getResponse(uri: String): Response {

        val d = TestDriver.appiumDriver

        var uri2 = uri
        if (uri2.startsWith("http").not()) {
            uri2 = "${d.remoteAddress}/session/${d.sessionId}/${uri}"
        }

        val url = uri2.toHttpUrlOrNull()!!.newBuilder().build()
        val response = getResponse(url)

        return response
    }

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
     * invoke
     */
    fun invoke(method: String, json: String): String {

        val urlString = getSessionUrl(method)
        val response = getResponse(urlString = urlString, json = json)

        return response.body!!.toString()
    }

    /**
     * refresh
     */
    fun refresh() {

        TestLog.trace()

        val d = TestDriver.appiumDriver

        val response = getResponse("${d.remoteAddress}/session/${d.sessionId}/refresh", "")

        println("responseCode:${response.code}")

        return
    }

    private fun getSessionUrl(method: String): String {

        if (TestDriver.isInitialized.not()) {
            throw NullPointerException("TestDriver.current is null.")
        }
        val d = TestDriver.appiumDriver
        val url = "${d.remoteAddress}session/${d.sessionId}/$method"
        return url
    }

    private fun getResponse(urlString: String, json: String): Response {

        val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
        val url = urlString.toHttpUrlOrNull()!!.newBuilder()
            .build()
        val request = Request.Builder().url(url).post(json.toRequestBody(mediaType))
            .build()
        val client = OkHttpClient().newBuilder()
            .readTimeout((testContext.appiumProxyReadTimeoutSeconds * 1000).toLong(), TimeUnit.MILLISECONDS)
            .build()

        var response: Response? = null
        try {
            response = client.newCall(request).execute()
            return response
        } catch (t: Throwable) {
            throw TestDriverException("${t.message} (appiumProxyReadTimeoutSeconds=${testContext.appiumProxyReadTimeoutSeconds})")
        } finally {
            response?.close()
        }
    }

    /**
     * find
     * @see https://appium.io/docs/en/commands/element/find-element/#example-usage
     */
    fun findElement(using: String, value: String): String {

        TestLog.trace()

        val json = """
{
    "using":"${using}",
    "value":"${value}"
}
        """.trimIndent()

        val urlString = getSessionUrl("element")
        val response = getResponse(urlString, json)

        return response.body!!.toString()
    }

    /**
     * find
     * @see https://appium.io/docs/en/commands/element/find-element/#example-usage
     */
    fun findElements(using: String, value: String): String {

        TestLog.trace()

        val json = """
{
    "using":"${using}",
    "value":"${value}"
}
        """.trimIndent()

        val urlString = getSessionUrl("elements")
        val response = getResponse(urlString, json)

        return response.body!!.toString()
    }

}