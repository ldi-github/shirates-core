package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.TestElementCache.rootElement
import shirates.core.driver.commandextension.appIs
import shirates.core.driver.commandextension.macro
import shirates.core.driver.commandextension.screenIs
import shirates.core.driver.commandextension.verify
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class AssertingAnything1 : UITest() {

    @Test
    @Order(10)
    fun ok() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.verify("The packageName is \"com.android.settings\"") {
                        if (rootElement.packageName == "com.android.settings") {
                            OK()
                        } else {
                            NG()
                        }
                    }
                    it.verify("The app is 'Settings' and the screen is [Android Settings Top Screen]") {
                        it.appIs("Settings")
                        it.screenIs("[Android Settings Top Screen]")
                    }
                }
            }
        }
    }

    @Test
    @Order(20)
    fun ng() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.verify("The app is 'Settings2' and the screen is [Android Settings Top Screen]") {
                        it.appIs("Settings2")
                        it.screenIs("[Android Settings Top Screen]")
                    }
                }
            }
        }
    }

    @Test
    @Order(30)
    fun notImplemented() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.verify("The app is 'Settings' and the screen is [Android Settings Top Screen]") {
                    }
                }
            }
        }
    }
}