package shirates.core.driver.commandextension

import shirates.core.Const
import shirates.core.configuration.NicknameUtility
import shirates.core.driver.*
import shirates.core.driver.TestDriver.lastElement
import shirates.core.driver.TestMode.isAndroid
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.utility.misc.AppNameUtility
import shirates.core.utility.sync.SyncUtility

/**
 * sendKeys
 */
fun TestDrive?.sendKeys(
    keysToSend: CharSequence,
    waitSeconds: Double = testContext.waitSecondsOnIsScreen
): TestElement {

    val testElement = getTestElement()

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
fun TestDrive?.putSelector(nickname: String, expression: String? = null): TestElement {

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
fun TestDrive?.screenshot(
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
fun TestDrive?.getCurrentAppIconName(): String {
    if (isAndroid) {
        throw NotImplementedError("getCurrentAppIconName function is not supported in Android.")
    } else {
        val e = TestElementCache.select(".XCUIElementTypeApplication")
        return e.label
    }
}

/**
 * getCurrentAppName
 */
fun TestDrive?.getCurrentAppName(): String {
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
fun TestDrive?.isApp(
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
 * doUntilTrue
 */
fun TestDrive?.doUntilTrue(
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
    intervalSecond: Double = Const.SYNC_UTILITY_DO_UNTIL_INTERVAL_SECONDS,
    maxLoopCount: Int = SyncUtility.MAX_LOOP_COUNT,
    actionFunc: (SyncUtility.SyncContext) -> Boolean
): TestElement {

    SyncUtility.doUntilTrue(
        waitSeconds = waitSeconds,
        intervalSeconds = intervalSecond,
        maxLoopCount = maxLoopCount,
        actionFunc = actionFunc
    )
    return lastElement
}

