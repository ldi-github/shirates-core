package shirates.core.uitest.android.driver.commandextension

import io.appium.java_client.android.nativekey.AndroidKey
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.driver.wait
import shirates.core.testcode.UITest
import shirates.core.testcode.Want
import shirates.core.utility.android.AndroidMobileShellUtility

@Want
@Testrun("testConfig/android/androidSettings/testrun.properties")
class TestDriveKeyboardExtensionTest : UITest() {

    @Test
    fun isKeyboardShown_hideKeyboard() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.isKeyboardShown.thisIsFalse("isKeyboardShown=false")
                }
            }
            case(2) {
                action {
                    it.tap("[Search settings]")
                }.expectation {
                    it.isKeyboardShown.thisIsTrue("isKeyboardShown=true")
                }
            }
            case(3) {
                action {
                    it.hideKeyboard()
                        .wait()
                }.expectation {
                    it.isKeyboardShown.thisIsFalse("isKeyboardShown=false")
                }
            }
        }
    }

    @Test
    fun pressBack() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Network & internet Screen]")
                }.action {
                    it.pressBack()
                }.expectation {
                    it.screenIs("[Android Settings Top Screen]")
                }
            }
        }

    }

    @Test
    fun pressHome() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    it.pressHome()
                        .pressHome()
                }.expectation {
                    it.screenIs("[Pixel Home Screen]")
                }
            }
        }
    }

    @Test
    fun pressEnter() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Network & internet Screen]")
                        .pressTab()
                }.action {
                    it.pressEnter()
                }.expectation {
                    it.screenIs("[Internet Screen]")
                }
            }
        }

    }

    @Test
    fun pressSearch() {

        scenario {
            case(1) {
                condition {
                    it.pressHome()
                        .pressHome()
                        .tap("@Search")
                        .sendKeys("appium")
                        .wait()
                    it.isKeyboardShown.thisIsTrue()
                }.action {
                    it.pressSearch()
                        .wait()
                }.expectation {
                    it.isKeyboardShown.thisIsFalse()
                }
            }
        }

    }

    @Test
    fun pressTab() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Network & internet Screen]")
                        .pressTab()
                }.action {
                    it.pressEnter()
                }.expectation {
                    it.screenIs("[Internet Screen]")
                }
            }
        }

    }

    @Test
    fun pressAndroid() {

        val original = AndroidMobileShellUtility.getDefaultInputMethod()
        try {
            scenario {
                case(1) {
                    condition {
                        it.macro("[Android Settings Top Screen]")
                            .tap("#search_action_bar_title")
                            .screenIs("[Android Settings Search Screen]")
                    }.action {
                        AndroidMobileShellUtility.setDefaultInputMethod("io.appium.settings/.UnicodeIME")
                        it.pressAndroid(AndroidKey.C)
                        it.pressAndroid(AndroidKey.O)
                        it.pressAndroid(AndroidKey.N)
                        it.pressAndroid(AndroidKey.N)
                        it.pressAndroid(AndroidKey.E)
                        it.pressAndroid(AndroidKey.C)
                        it.pressAndroid(AndroidKey.T)
                        it.pressAndroid(AndroidKey.E)
                        it.pressAndroid(AndroidKey.D)
                        it.pressAndroid(AndroidKey.ENTER)
                    }.expectation {
                        it.exist("connected")
                    }
                }
            }
        } finally {
            AndroidMobileShellUtility.setDefaultInputMethod(original)
        }
    }

}