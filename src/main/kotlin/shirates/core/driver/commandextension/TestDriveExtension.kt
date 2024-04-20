package shirates.core.driver.commandextension

import shirates.core.configuration.NicknameUtility
import shirates.core.configuration.Selector
import shirates.core.configuration.isRelativeNickname
import shirates.core.configuration.repository.ScreenRepository
import shirates.core.driver.*
import shirates.core.driver.TestMode.isAndroid
import shirates.core.exception.TestDriverException
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.utility.misc.AppNameUtility

/**
 * Returns this if this is TestElement
 * Else lastElement
 */
fun TestDrive.getThisOrLastElement(): TestElement {
    if (this is TestElement) {
        return this
    }
    return TestDriver.lastElement
}

/**
 * Returns this if this is TestElement
 * else TestDriver.it
 */
fun TestDrive.getThisOrIt(): TestElement {
    if (this is TestElement) {
        return this
    }
    return TestDriver.it
}

/**
 * sendKeys
 */
fun TestDrive.sendKeys(
    keysToSend: CharSequence,
): TestElement {

    val command = "sendKeys"
    val message = message(id = command, key = "$keysToSend")

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message) {
        val testElement = TestDriver.getFocusedElement()
        if (testElement.isEmpty) {
            throw TestDriverException("Focused element not found.")
        }
        val we = testElement.webElement ?: testElement.getWebElement()
        we.sendKeys(keysToSend)

        TestDriver.refreshCache()

        lastElement = TestDriver.getFocusedElement()
    }

    return lastElement
}

/**
 * getSelector
 */
fun TestDrive.getSelector(expression: String): Selector {

    val sel = TestDriver.screenInfo.expandExpression(expression = expression)
    val newSel = sel.copy()
    if (newSel.isRelative.not()) {
        return newSel
    }

    if (this is TestElement) {
        if (TestMode.isNoLoadRun && expression.isRelativeNickname()) {
            return Selector("${this.selector}$expression")
        }
        return this.getChainedSelector(newSel)
    }

    return newSel
}

/**
 * tempSelector
 */
fun TestDrive.tempSelector(nickname: String, expression: String): TestElement {

    ScreenRepository.tempSelectorList.removeIf { it.first == nickname }
    ScreenRepository.tempSelectorList.add(Pair(nickname, expression))
    TestLog.info(message(id = "nicknameRegistered", key = nickname, value = expression))

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
        return rootElement.label
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
