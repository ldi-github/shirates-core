package shirates.core.driver.commandextension

import shirates.core.configuration.NicknameUtility
import shirates.core.driver.*
import shirates.core.driver.TestDriver.lastElement
import shirates.core.driver.TestMode.isAndroid
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.storage.app
import shirates.core.utility.misc.AppIconNameUtility
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
    return AppIconNameUtility.getAppName(getCurrentAppIconName())
}

/**
 * isApp
 */
fun TestDrive?.isApp(
    nickname: String? = null
): Boolean {

    syncCache()

    val context = TestDriverCommandContext(rootElement)
    var r = false
    context.execBooleanCommand(subject = nickname) {
        if (nickname == null) {
            if (isAndroid) {
                r = rootElement.packageName == testContext.profile.packageOrBundleId
            } else {
                r = rootElement.name == testContext.profile.appIconName
            }
            return@execBooleanCommand
        }

        NicknameUtility.validateNickname(nickname)

        if (isAndroid) {
            val nicknameText = NicknameUtility.getNicknameText(nickname)
            val expectedPackage: String
            val isTestTarget =
                AppIconNameUtility.getAppName(testContext.profile.appIconName!!) == nicknameText
            if (isTestTarget) {
                expectedPackage = testContext.profile.packageOrBundleId!!
            } else {
                expectedPackage = app(datasetName = nickname, attributeName = "packageOrBundleId")
            }

            refreshCache()
            val actualPackage = rootElement.packageName

            val result = actualPackage == expectedPackage
            r = result
        } else {
            val expectedAppName = nickname.trimStart('[').trimEnd(']')
            val actualAppName = TestElementCache.select(".XCUIElementTypeApplication").getCurrentAppName()

            r = actualAppName == expectedAppName
        }
    }

    return r
}

/**
 * doUntilTrue
 */
fun TestDrive?.doUntilTrue(
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
    intervalSecond: Double = shirates.core.Const.DO_UNTIL_INTERVAL_SECONDS,
    maxLoopCount: Int = SyncUtility.MAX_LOOP_COUNT,
    actionFunc: () -> Boolean
): TestElement {

    SyncUtility.doUntilTrue(
        waitSeconds = waitSeconds,
        intervalSecond = intervalSecond,
        maxLoopCount = maxLoopCount,
        actionFunc = actionFunc
    )
    return lastElement
}

