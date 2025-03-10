package shirates.core.proxy

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
import shirates.core.exception.RerunScenarioException
import shirates.core.exception.TestDriverException
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.utility.element.ElementCacheUtility
import shirates.core.utility.load.CpuLoadService
import shirates.core.utility.sync.WaitUtility
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * AppiumProxy
 */
object AppiumProxy {

    var lastSource = ""

    /**
     * getSource
     */
    fun getSource(
        waitSeconds: Double = testContext.appiumProxyGetSourceTimeoutSeconds,
        intervalSeconds: Double = 1.0,
        maxLoopCount: Int = 60,
        checkContentLoaded: Boolean = true,
        throwOnFinally: Boolean = true
    ): TestElement {

        if (TestMode.isNoLoadRun) {
            return TestElement()
        }

        CpuLoadService.waitForCpuLoadUnder()

        var root = TestElement()
        var emptyScreenErrorCount = 0
        var getWebElementErrorCount = 0

        fun outputCheckLog(count: Int, msg: String) {
            if (count == 1) {
                TestLog.info(msg, PropertiesManager.enableGetSourceLog)
            } else {
                TestLog.warn(msg)
            }
        }

        fun checkState(): Boolean {
            if (checkContentLoaded) {
                if (root.descendants.isEmpty()) {
                    emptyScreenErrorCount += 1
                    outputCheckLog(count = emptyScreenErrorCount, msg = "Contents is empty.($emptyScreenErrorCount)")
                    if (emptyScreenErrorCount >= 3) {
                        throw RerunScenarioException(message(id = "couldNotGetContentsOfScreen"))
                    }
                    return false
                }
            }

            try {
                val we = root.getWebElement()
                if (isAndroid) {
                    we.getAttribute("package")
                } else {
                    we.getAttribute("label")
                }
            } catch (t: Throwable) {
                getWebElementErrorCount += 1
                outputCheckLog(
                    count = getWebElementErrorCount,
                    msg = "AppiumProxy.getSource() checkState(): $t $root ${t.cause ?: ""}"
                )
                return false
            }

            return true
        }

        fun validateSource(): Boolean {
            if (lastSource == "") {
                return false
            }
            if (isiOS) {
                if (lastSource.contains("<AppiumAUT>").not() || lastSource.contains("<XCUIElementTypeApplication")
                        .not()
                ) {
                    return false
                }
            }
            return true
        }

        val wc = WaitUtility.doUntilTrue(
            waitSeconds = waitSeconds,
            intervalSeconds = intervalSeconds,
            maxLoopCount = maxLoopCount,
            throwOnFinally = false,
            onMaxLoop = { c ->
                c.error =
                    TestDriverException(
                        "AppiumProxy.getSource() reached maxLoop count.(maxLoopCount=${c.maxLoopCount})",
                        cause = c.error
                    )
            },
            onTimeout = { c ->
                c.error = TestDriverException(
                    "AppiumProxy.getSource() timed out. (waitSeconds=${c.waitSeconds})",
                    cause = c.error
                )
            }
        ) { c ->
            if (PropertiesManager.enableGetSourceLog) {
                val count = c.count
                TestLog.info("getSource($count)")
            }
            lastSource = TestDriver.appiumDriver.pageSource ?: ""
            val r = validateSource()
            if (r) {
                root = ElementCacheUtility.createTestElementFromXml(sourceXml = lastSource)
                if (isAndroid) {
                    checkState()
                } else {
                    true    // skip checkState to avoid StaleElementReferenceException
                }
            } else {
                false
            }
        }

        if (throwOnFinally && wc.hasError) {
            throw wc.error!!
        }

        return root
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
        val url = "${d.remoteAddress}/session/${d.sessionId}/source".toHttpUrlOrNull()!!.newBuilder()
            .build()
        val response = HttpProxy.getResponse(url)

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
        val response = HttpProxy.getResponse(url)

        return response
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