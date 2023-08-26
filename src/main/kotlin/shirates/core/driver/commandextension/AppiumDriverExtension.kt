package shirates.core.driver.commandextension

import io.appium.java_client.AppiumDriver
import shirates.core.driver.TestDriver
import shirates.core.driver.TestMode.isAndroid
import shirates.core.driver.testContext
import shirates.core.driver.testDrive
import shirates.core.driver.waitForClose
import shirates.core.exception.TestConfigException
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.utility.sync.SyncUtility
import shirates.core.utility.toPath
import java.nio.file.Files

/**
 * isAppInstalled
 */
internal fun AppiumDriver.isAppInstalled(packageOrBundleId: String?): Boolean {

    if (packageOrBundleId == null) {
        return false
    }

    try {
        if (isAndroid) {
            return TestDriver.androidDriver.isAppInstalled(packageOrBundleId)
        } else {
            return TestDriver.iosDriver.isAppInstalled(packageOrBundleId)
        }
    } catch (t: Throwable) {
        return false
    }
}

/**
 * installApp
 */
internal fun AppiumDriver.installApp(appPackageFile: String = testContext.profile.appPackageFullPath) {

    if (appPackageFile.isBlank()) {
        throw IllegalArgumentException("appPackageFile must be specified")
    }

    if (Files.exists(appPackageFile.toPath()).not()) {
        throw TestConfigException("app file not found(${appPackageFile})")
    }
    if (isAndroid) {
        TestDriver.androidDriver.installApp(appPackageFile)
    } else {
        TestDriver.iosDriver.installApp(appPackageFile)
    }
}

internal fun AppiumDriver.removeApp(packageOrBundleId: String? = testContext.profile.packageOrBundleId) {

    if (packageOrBundleId.isNullOrBlank()) {
        throw IllegalAccessException(message(id = "required", subject = "packageOrBundleId", value = packageOrBundleId))
    }

    if (isAppInstalled(packageOrBundleId = packageOrBundleId).not()) {
        TestLog.info(message(id = "appIsNotInstalled", subject = packageOrBundleId))
        return
    }

    SyncUtility.doUntilTrue {
        try {
            if (isAndroid) {
                TestDriver.androidDriver.removeApp(packageOrBundleId)
            } else {
                TestDriver.iosDriver.removeApp(packageOrBundleId)
            }
        } catch (t: Throwable) {
            if (t.message!!.startsWith("Cannot invoke \"java.lang.Boolean.booleanValue()\"") && t.message!!.endsWith("is null")) {
                // ignore this message (This might be a bug)
                // Cannot invoke "java.lang.Boolean.booleanValue()" because the return value of "io.appium.java_client.CommandExecutionHelper.execute(io.appium.java_client.ExecutesMethod, java.util.Map$Entry)" is null
                TestLog.trace(t.message!!)
            } else {
                TestLog.warn(t.message!!)
            }
        }
        isAppInstalled(packageOrBundleId = packageOrBundleId).not()
    }
    testDrive.waitForClose(testContext.appIconName)
}

internal fun AppiumDriver.terminateApp(packageOrBundleId: String?) {

    if (packageOrBundleId.isNullOrBlank()) {
        throw IllegalAccessException(message(id = "required", subject = "packageOrBundleId", value = packageOrBundleId))
    }
    if (isAndroid) {
        try {
            TestDriver.androidDriver.terminateApp(packageOrBundleId)
        } catch (t: Throwable) {
            TestLog.info(t.toString())
        }
    } else {
        TestDriver.iosDriver.terminateApp(packageOrBundleId)
    }
}

internal fun AppiumDriver.hideKeyboard() {

    if (isAndroid) {
        TestDriver.androidDriver.hideKeyboard()
    } else {
        TestDriver.iosDriver.hideKeyboard()
    }
}