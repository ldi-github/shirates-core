package shirates.core.driver.befavior

import shirates.core.driver.TestDrive
import shirates.core.driver.commandextension.*
import shirates.core.exception.TestNGException
import shirates.core.logging.Message.message
import shirates.core.utility.ios.PagerInfo


object TapHelper : TestDrive {

    /**
     * swipeLeftAndTapAppIcon
     */
    fun swipeLeftAndTapAppIcon(
        appIconName: String
    ) {

        if (it.canSelect(appIconName)) {
            it.tap()
        } else {
            it.pressHome()
            if (it.canSelect(appIconName)) {
                it.tap()
            }
            it.pressHome()
            for (i in 1..3) {
                it.wait(0.5)
                refreshCache()
                if (canSelect(appIconName)) {
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
        appIconName: String
    ) {

        if (it.canSelect(appIconName)) {
            it.tap()
                .wait()
            return
        }

        it.pressHome()
        if (it.canSelect(appIconName)) {
            it.tap()
                .wait()
            return
        }

        it.pressHome()

        val pi = getPagerInfo()
        for (i in 1..pi.lastPageNumber) {
            if (it.canSelect(expression = appIconName)) {
                it.tap()
                    .wait()
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
        val pager = it.select(".XCUIElementTypePageIndicator")
        return PagerInfo(pager.value)
    }

    /**
     * tapAppIconAsGooglePixel
     */
    fun tapAppIconAsGooglePixel(
        appIconName: String
    ) {
        if (it.canSelect(appIconName)) {
            it.tap()
                .wait()
            return
        }
        if (it.canSelect("#apps_list_view").not()) {
            it.pressHome()
        }
        it.flickCenterToTop()
        if (it.canSelectWithScrollDown(appIconName)) {
            it.tap()
                .wait()
            return
        }

        throw TestNGException(message(id = "appIconNotFound", subject = appIconName))
    }
}