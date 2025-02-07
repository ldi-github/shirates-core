package shirates.core.driver.behavior

import org.openqa.selenium.StaleElementReferenceException
import shirates.core.driver.TapMethod
import shirates.core.driver.TestDrive
import shirates.core.driver.commandextension.*
import shirates.core.driver.wait
import shirates.core.exception.TestNGException
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.utility.ios.PagerInfo
import shirates.core.utility.sync.SyncUtility


object TapHelper : TestDrive {

    /**
     * swipeLeftAndTapAppIcon
     */
    fun swipeLeftAndTapAppIcon(
        appIconName: String,
        tapMethod: TapMethod = TapMethod.click
    ) {

        if (it.canSelect(appIconName)) {
            it.tap(tapMethod = tapMethod)
        } else {
            it.pressHome()
            if (it.canSelect(appIconName)) {
                it.tap(tapMethod = tapMethod)
            }
            it.pressHome()
            for (i in 1..3) {
                it.wait(0.5)
                refreshCache()
                if (canSelect(appIconName)) {
                    it.tap(tapMethod = tapMethod)
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
        tapMethod: TapMethod = TapMethod.click
    ) {

        if (it.canSelect(appIconName)) {
            it.tap(tapMethod = tapMethod)
                .wait()
            return
        }

        it.pressHome()
        if (it.canSelect(appIconName)) {
            it.tap(tapMethod = tapMethod)
                .wait()
            return
        }

        it.pressHome()

        val pi = getPagerInfo()
        for (i in 1..pi.lastPageNumber) {
            if (it.canSelect(expression = appIconName)) {
                fun tryTap(): Boolean {
                    try {
                        it.tap(tapMethod = tapMethod)
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
                    .refreshCache()
            }
        }

        throw TestNGException(message(id = "appIconNotFound", subject = appIconName))
    }

    /**
     * getPagerInfo(for iOS)
     */
    fun getPagerInfo(): PagerInfo {
        val pager = it.select(".XCUIElementTypePageIndicator")
        return PagerInfo(pager.value)
    }

    /**
     * tapAppIconAsGooglePixel
     */
    fun tapAppIconAsGooglePixel(
        appIconName: String,
        tapMethod: TapMethod = TapMethod.auto
    ) {
        if (it.canSelect(appIconName)) {
            it.tap(tapMethod = tapMethod)
                .wait()
            return
        }
        if (it.canSelect("#apps_list_view").not()) {
            it.pressHome()
        }
        it.flickCenterToTop()
        if (it.canSelectWithScrollDown(appIconName)) {
            it.tap(tapMethod = tapMethod)
                .wait()
            return
        }

        throw TestNGException(message(id = "appIconNotFound", subject = appIconName))
    }
}