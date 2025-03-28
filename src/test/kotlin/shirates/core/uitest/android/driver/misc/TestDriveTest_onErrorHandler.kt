package shirates.core.uitest.android.driver.misc

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.branchextension.ifScreenIs
import shirates.core.driver.commandextension.*
import shirates.core.driver.testContext
import shirates.core.logging.printWarn
import shirates.core.testcode.UITest
import shirates.helper.ImageSetupHelper

@Testrun("testConfig/android/androidSettings/testrun.properties")
class TestDriveTest_onErrorHandler : UITest() {

    @Test
    @Order(10)
    fun select() {

        testContext.onSelectErrorHandler = {
            ifScreenIs("[Android Settings Top Screen]") {
                printWarn("onSelectErrorHandler is called. Redirecting to [Network & internet Screen]")
                it.tap("Network & internet")
            }
        }

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    it.select("Airplane mode", waitSeconds = 1.0)
                }.expectation {
                    it.screenIs("[Network & internet Screen]")
                }
            }
        }
    }

    @Test
    @Order(20)
    fun exist() {

        testContext.onExistErrorHandler = {
            ifScreenIs("[Android Settings Top Screen]") {
                printWarn("onExistErrorHandler is called. Redirecting to [Network & internet Screen]")
                it.tap("Network & internet")
            }
        }

        scenario {
            case(1) {
                condition {
                    it.terminateApp()
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.exist("Airplane mode", waitSeconds = 1.0) {
                        imageMatched.thisIsFalse()
                        isFound.thisIsTrue()
                        isDummy.thisIsFalse()
                        isEmpty.thisIsFalse()
                    }
                    it.screenIs("[Network & internet Screen]")
                }
            }
        }
    }

    @Test
    @Order(30)
    fun existImageSetup() {

        scenario {
            ImageSetupHelper.setupImageAndroidSettingsTopScreen()
            ImageSetupHelper.setupImageAndroidSettingsNetworkAndInternetScreen()
        }
    }

    /**
     * Run existImageSetup()
     * to set up image files
     * before execute this test
     */
    @Test
    @Order(32)
    fun existImage() {

        testContext.onExistErrorHandler = {
            ifScreenIs("[Android Settings Top Screen]") {
                printWarn("onExistErrorHandler is called. Redirecting to [Network & internet Screen]")
                it.tap("Network & internet")
            }
        }

        scenario {
            case(1) {
                condition {
                    it.terminateApp()
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.existImage("[Airplane mode Icon]") {
                        imageMatched.thisIsTrue("imageMatched")
                        isFound.thisIsTrue("isFound")
                        isDummy.thisIsTrue("isDummy")
                        isEmpty.thisIsFalse("isEmpty")
                    }
                    it.screenIs("[Network & internet Screen]")
                }
            }
        }
    }

    @Test
    @Order(40)
    fun screenIs() {

        testContext.onScreenErrorHandler = {
            ifScreenIs("[Android Settings Top Screen]") {
                printWarn("onScreenErrorHandler is called. Redirecting to [Network & internet Screen]")
                it.tap("Network & internet")
            }
        }

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.screenIs("[Network & internet Screen]")
                }
            }
        }
    }
}