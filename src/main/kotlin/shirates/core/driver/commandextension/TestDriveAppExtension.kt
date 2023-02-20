package shirates.core.driver.commandextension

import shirates.core.configuration.Selector
import shirates.core.driver.*
import shirates.core.driver.TestDriver.lastElement
import shirates.core.driver.TestMode.isAndroid
import shirates.core.exception.TestDriverException
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.storage.app
import shirates.core.utility.misc.AppNameUtility

/**
 * isAppInstalled
 */
fun TestDrive?.isAppInstalled(
    appNickname: String? = null,
    packageOrBundleId: String? = testContext.profile.packageOrBundleId
): Boolean {

    val testElement = getTestElement()

    var id = packageOrBundleId
    if (appNickname != null) {
        id = app(datasetName = appNickname, attributeName = "packageOrBundleId")
    }

    val context = TestDriverCommandContext(testElement)
    var isInstalled = false
    context.execBooleanCommand(arg1 = id, fireEvent = false) {

        isInstalled = TestDriver.appiumDriver.isAppInstalled(id)
        if (isInstalled) {
            TestLog.trace("app is installed ($id)")
        } else {
            TestLog.trace("app is not installed ($id)")
        }
    }

    return isInstalled
}

/**
 * installApp
 */
fun TestDrive?.installApp(
    appPackageFile: String = testContext.profile.appPackageFullPath,
): TestElement {

    val testElement = getTestElement()

    val command = "installApp"
    val message = message(id = command, file = appPackageFile)
    val context = TestDriverCommandContext(testElement)
    context.execOperateCommand(command = command, message = message, fireEvent = false) {

        TestDriver.appiumDriver.installApp(appPackageFile)

        if (isAndroid) {
            if (canSelect("送信しない||Don't*||don't*||Do not*||do not*")) {
                lastElement.tap()
            }
        }

        TestDriver.invalidateCache()
    }

    return lastElement
}

/**
 * removeApp
 */
fun TestDrive?.removeApp(
    packageOrBundleId: String? = testContext.profile.packageOrBundleId,
): TestElement {

    val testElement = getTestElement()

    val command = "removeApp"
    val message = message(id = command, subject = packageOrBundleId)
    val context = TestDriverCommandContext(testElement)
    context.execOperateCommand(command = command, message = message, fireEvent = false) {

        TestDriver.appiumDriver.removeApp(packageOrBundleId)

        TestDriver.invalidateCache()
    }

    return testElement
}

/**
 * terminateApp
 */
fun TestDrive?.terminateApp(
    appNameOrAppId: String = testContext.appIconName
): TestElement {

    val testElement = getTestElement()

    val command = "terminateApp"
    val subject = Selector(appNameOrAppId).toString()
    val message = message(id = command, subject = subject)
    val context = TestDriverCommandContext(testElement)
    context.execOperateCommand(command = command, message = message, fireEvent = false) {
        try {
            val packageOrBundleId = AppNameUtility.getPackageOrBundleId(appNameOrAppId = appNameOrAppId)
            TestDriver.appiumDriver.terminateApp(packageOrBundleId = packageOrBundleId)
        } catch (t: Throwable) {
            if (t.message?.contains("is still running after") == true) {
                // 'com.android.settings' is still running after 500ms timeout
                throw TestDriverException(t.message ?: t.toString(), t)
            } else {
                throw t
            }
        }

        TestDriver.invalidateCache()
    }

    return testElement
}

/**
 * restartApp
 */
fun TestDrive?.restartApp(
    appIconNameOrPackageOrBundleId: String = TestDriver.testContext.appIconName
): TestElement {

    if (TestMode.isNoLoadRun) {
        return lastElement
    }

    TestDriver.it.terminateApp(appIconNameOrPackageOrBundleId)
    TestDriver.it.launchApp(appIconNameOrPackageOrBundleId)

    return lastElement
}

