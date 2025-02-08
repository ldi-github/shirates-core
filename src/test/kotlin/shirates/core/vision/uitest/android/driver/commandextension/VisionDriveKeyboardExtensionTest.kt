package shirates.core.vision.uitest.android.driver.commandextension

import io.appium.java_client.android.nativekey.AndroidKey
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.pressEnter
import shirates.core.driver.commandextension.thisIsFalse
import shirates.core.driver.commandextension.thisIsTrue
import shirates.core.driver.testDrive
import shirates.core.testcode.Want
import shirates.core.utility.android.AndroidMobileShellUtility
import shirates.core.vision.driver.commandextension.*
import shirates.core.vision.driver.wait
import shirates.core.vision.testcode.VisionTest

@Want
@Testrun("testConfig/android/androidSettings/testrun.properties")
class VisionDriveKeyboardExtensionTest : VisionTest() {

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
                    it.tap("Search settings")
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
                    it.screenIs("[Android Home Screen]")
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
                    testDrive.pressEnter()
                }.expectation {
                    it.screenIs("[Internet Screen]")
                }
            }
        }

    }

//    @Test
//    fun pressSearch() {
//
//        scenario {
//            case(1) {
//                condition {
//                    it.pressHome()
//                        .pressHome()
//                        .tap("@Search")
//                        .sendKeys("appium")
//                        .wait()
//                    it.isKeyboardShown.thisIsTrue()
//                }.action {
//                    it.pressSearch()
//                        .wait()
//                }.expectation {
//                    it.isKeyboardShown.thisIsFalse()
//                }
//            }
//        }
//
//    }

    @Test
    fun pressTab() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Network & internet Screen]")
                        .pressTab()
                }.action {
                    testDrive.pressEnter()
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
                            .tap("Search settings")
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

//    @Test
//    fun tapSoftwareKey() {
//
//        scenario {
//            case(1) {
//                condition {
//                    it.macro("[Android Settings Top Screen]")
//                        .tap("#search_action_bar_title")
//                        .screenIs("[Android Settings Search Screen]")
//                }.expectation {
//                    try {
//                        it.tapSoftwareKey("search")
//                        NG()
//                    } catch (t: Throwable) {
//                        OK(t.message!!)
//                    }
//                }
//            }
//        }
//
//    }
}