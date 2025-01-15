package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class AssertingAttribute1 : UITest() {

    @Test
    @Order(10)
    fun textAssertion_OK() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    it.select("Network & internet")
                }.expectation {
                    it
                        .textIs("Network & internet")
                        .textIsNot("Notifications")

                        .textStartsWith("Network &")
                        .textStartsWithNot("Connected")

                        .textContains("work & int")
                        .textContainsNot("device")

                        .textEndsWith("& internet")
                        .textEndsWithNot("devices")

                        .textMatches("^Net.*")
                        .textMatchesNot("^Connected.*")

                        .textIsNotEmpty()
                }
            }
            case(2) {
                action {
                    it.select("#account_avatar")
                }.expectation {
                    it.textIsEmpty()
                }
            }
        }
    }

    @Test
    @Order(20)
    fun textAssertion_NG() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    it.select("Network & internet")
                }.expectation {
                    it.textIs("Connected devices")
                }
            }
        }
    }

    @Test
    @Order(30)
    fun idAssertion_OK() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    it.select("#account_avatar")
                }.expectation {
                    it
                        .idIs("account_avatar")
                        .idIs("com.android.settings:id/account_avatar")
                }
            }
        }
    }

    @Test
    @Order(40)
    fun idAssertion_NG() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    it.select("#account_avatar")
                }.expectation {
                    it
                        // OK. expected is converted to "com.android.settings:id/account_avatar"
                        .idIs("account_avatar")

                        // OK. expected is converted to "com.android.settings:id/account_avatar"
                        .idIs("account_avatar", auto = true)

                        // NG. expected is "account_avatar"
                        .idIs("account_avatar", auto = false)
                }
            }
        }
    }

    @Test
    @Order(50)
    fun accessAssertion_OK() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Network & internet Screen]")
                }.action {
                    it.select("@Network & internet")
                }.expectation {
                    it.accessIs("Network & internet")
                        .accessIsNot("System")
                }
            }
        }
    }

    @Test
    @Order(60)
    fun accessAssertion_NG() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Network & internet Screen]")
                }.action {
                    it.select("@Network & internet")
                }.expectation {
                    it.accessIs("Connected devices")
                }
            }
        }
    }

    @Test
    @Order(70)
    fun classNameAssertion_OK() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    it.select("#account_avatar")
                }.expectation {
                    it.classIs("android.widget.ImageView")
                        .classIsNot("android.widget.TextView")
                }
            }
        }
    }

    @Test
    @Order(80)
    fun classNameAssertion_NG() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    it.select("#account_avatar")
                }.expectation {
                    it.classIs("android.widget.TextView")
                }
            }
        }
    }

    @Test
    @Order(90)
    fun attributeAssertion_OK() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    it.select("#account_avatar")
                }.expectation {
                    it.attributeIs("package", "com.android.settings")
                }
            }
        }
    }

    @Test
    @Order(100)
    fun attributeAssertion_NG() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    it.select("#account_avatar")
                }.expectation {
                    it.attributeIs("package", "com.google.android.calculator")
                }
            }
        }
    }
}