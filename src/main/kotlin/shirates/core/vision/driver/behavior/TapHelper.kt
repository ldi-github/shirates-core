package shirates.core.vision.driver.behavior

import org.openqa.selenium.StaleElementReferenceException
import shirates.core.driver.classic
import shirates.core.driver.commandextension.select
import shirates.core.exception.TestNGException
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.utility.ios.PagerInfo
import shirates.core.utility.sync.SyncUtility
import shirates.core.vision.VisionDrive
import shirates.core.vision.driver.commandextension.*
import shirates.core.vision.driver.it
import shirates.core.vision.driver.wait


object TapHelper : VisionDrive {

    /**
     * swipeLeftAndTapAppIcon
     */
    fun swipeLeftAndTapAppIcon(
        appIconName: String,
    ) {

        if (it.canDetect(appIconName)) {
            it.tap()
        } else {
            it.pressHome()
            if (it.canDetect(appIconName)) {
                it.tap()
            }
            it.pressHome()
            for (i in 1..3) {
                it.wait(0.5)
                screenshot()
                if (canDetect(appIconName)) {
                    it.tap()
                    return
                }
                flickCenterToLeft()
            }
        }

        throw TestNGException(message(id = "appIconNotFound", subject = appIconName))
    }

    /**
     * appIconName
     */
    fun tapAppIconAsIos(
        appIconName: String,
    ) {

        if (it.canDetect(appIconName)) {
            it.tap()
                .wait()
            return
        }

        it.pressHome()
        if (it.canDetect(appIconName)) {
            it.tap()
                .wait()
            return
        }

        it.pressHome()

        val pi = getPagerInfo()
        for (i in 1..pi.lastPageNumber) {
            if (it.canDetect(expression = appIconName)) {
                fun tryTap(): Boolean {
                    try {
                        it.tap()
                        return true
                    } catch (t: Throwable) {
                        if (t is StaleElementReferenceException) {
                            return false
                        }
                        TestLog.warn(t.message ?: t.toString().split("\\n").first())
                        throw t
                    }
                }

                val syncResult = SyncUtility.doUntilTrue(maxLoopCount = 5) {
                    tryTap()    // Workaround for org.openqa.selenium.StaleElementReferenceException
                }
                if (syncResult.hasError) {
                    throw syncResult.error!!
                }
                it.wait()
                return
            } else {
                it.flickRightToLeft()
            }
        }

        throw TestNGException(message(id = "appIconNotFound", subject = appIconName))
    }

    /**
     * getPagerInfo(for iOS)
     */
    fun getPagerInfo(): PagerInfo {
        val pager = classic.select(".XCUIElementTypePageIndicator", useCache = false)
        return PagerInfo(pager.value)
    }

    /**
     * tapAppIconAsGooglePixel
     */
    fun tapAppIconAsGooglePixel(
        appIconName: String,
    ) {
        if (it.canDetect(appIconName)) {
            it.tap()
                .wait()
            return
        }
        it.pressHome()
        it.flickCenterToTop()
        if (it.canDetectWithScrollDown(appIconName)) {
            it.tap()
                .wait()
            return
        }

        throw TestNGException(message(id = "appIconNotFound", subject = appIconName))
    }
}