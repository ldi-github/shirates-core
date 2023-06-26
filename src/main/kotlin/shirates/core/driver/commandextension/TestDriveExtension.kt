package shirates.core.driver.commandextension

import shirates.core.configuration.NicknameUtility
import shirates.core.driver.*
import shirates.core.driver.TestMode.isAndroid
import shirates.core.logging.Measure
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.utility.misc.AppNameUtility

/**
 * getThisOrRootElement
 */
fun TestDrive.getThisOrRootElement(): TestElement {

    return if (this is TestElement) this
    else rootElement
}

/**
 * sendKeys
 */
fun TestDrive.sendKeys(
    keysToSend: CharSequence,
    waitSeconds: Double = testContext.waitSecondsOnIsScreen
): TestElement {

    val testElement = focusedElement

    val command = "sendKeys"
    val message = message(id = command, key = "$keysToSend")

    val context = TestDriverCommandContext(testElement)
    context.execOperateCommand(command = command, message = message) {
        val m = TestDriver.getFocusedWebElement()
        m.sendKeys(keysToSend)

        TestDriver.refreshCache()

        lastElement = TestDriver.getFocusedElement(waitSeconds = waitSeconds)
    }

    return lastElement
}

/**
 * putSelector
 */
fun TestDrive.putSelector(nickname: String, expression: String? = null): TestElement {

    val screenInfo = TestDriver.screenInfo
    if (expression == null) {
        val selector = screenInfo.getSelector(nickname)
        screenInfo.putSelector(selector)
    } else {
        screenInfo.putSelector(nickname = nickname, expression = expression)
    }
    return lastElement
}

/**
 * screenshot
 */
fun TestDrive.screenshot(
    force: Boolean = false,
    onChangedOnly: Boolean = testContext.onChangedOnly,
    filename: String? = null,
    withXmlSource: Boolean = TestLog.enableXmlSourceDump
): TestElement {

    TestDriver.screenshot(
        force = force,
        onChangedOnly = onChangedOnly,
        filename = filename,
        withXmlSource = withXmlSource
    )
    return lastElement
}

/**
 * getCurrentAppIconName
 */
fun TestDrive.getCurrentAppIconName(): String {
    if (isAndroid) {
        throw NotImplementedError("getCurrentAppIconName function is for iOS. Not supported in Android.")
    } else {
        val e = select(".XCUIElementTypeApplication")
        return e.label
    }
}

/**
 * getCurrentAppName
 */
fun TestDrive.getCurrentAppName(): String {
    return AppNameUtility.getAppNameWithoutExtension(getCurrentAppIconName())
}

/**
 * isApp
 *
 * @param appNameOrAppId
 * Nickname [App1]
 * or appName App1
 * or packageOrBundleId com.example.app1
 */
fun TestDrive.isApp(
    appNameOrAppId: String = testContext.appIconName
): Boolean {

    syncCache()

    val context = TestDriverCommandContext(rootElement)
    var r = false
    context.execBooleanCommand(subject = appNameOrAppId) {

        val packageOrBundleId = AppNameUtility.getPackageOrBundleId(appNameOrAppIdOrActivityName = appNameOrAppId)
        val appNickName = AppNameUtility.getAppNickNameFromPackageName(packageName = packageOrBundleId)
        val appName =
            if (appNickName.isNotBlank()) NicknameUtility.getNicknameText(nickname = appNickName)
            else appNameOrAppId

        if (isAndroid) {
            r = rootElement.packageName == packageOrBundleId
        } else {
            r = rootElement.name == appName
        }
    }

    return r
}

/**
 * tapDefault
 */
fun TestDrive.tapDefault(
    safeElementOnly: Boolean = false
): TestElement {

    val default = TestDriver.screenInfo.default
    if (default.isBlank()) {
        return lastElement
    }

    tap(expression = default, safeElementOnly = safeElementOnly)

    return lastElement
}

/**
 * closeDialogs
 */
fun TestDrive.closeDialogs(
    vararg screenNicknames: String
): TestElement {

    val ms = Measure()
    try {
        for (screenNickname in screenNicknames) {
            if (isScreen(screenName = screenNickname)) {
                tapDefault()
            }
        }
        return refreshLastElement()
    } finally {
        ms.end()
    }
}