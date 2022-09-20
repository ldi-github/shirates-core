package experiment

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest

@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
class SelectTest : UITest() {

    @Test
    fun select_not() {

        scenario {
            case(1) {
                condition {
                    it.screenIs("[Android Settings Top Screen]")
                }.action {
                    it.select("Apps", throwsException = false, waitSeconds = 0.0)
                }.expectation {
                    lastElement.isEmpty.thisIsFalse()
                    lastElement.isDummy.thisIsFalse()
                }
            }
            case(2) {
                action {
                    it.select("<Apps>:not", throwsException = false, waitSeconds = 0.0)
                }.expectation {
                    lastElement.isEmpty.thisIsTrue()
                    lastElement.isDummy.thisIsFalse()
                }
            }
            case(3) {
                action {
                    it.select("Oops", throwsException = false, waitSeconds = 0.0)
                }.expectation {
                    lastElement.isEmpty.thisIsTrue()
                    lastElement.isDummy.thisIsFalse()
                }
            }
            case(4) {
                action {
                    it.select("<Oops>:not", throwsException = false, waitSeconds = 0.0)
                }.expectation {
                    lastElement.isEmpty.thisIsFalse()
                    lastElement.isDummy.thisIsTrue()
                }
            }
        }
    }

    @Test
    fun canSelect_not() {

        scenario {
            case(1) {
                condition {
                    it.screenIs("[Android Settings Top Screen]")
                }.action {
                    it.canSelect("Oops")
                        .thisIsFalse()
                }.expectation {
                    it.isEmpty.thisIsTrue()
                }
            }
            case(2) {
                action {
                    it.canSelect("<Oops>:not")
                        .thisIsTrue()
                }.expectation {
                    it.isEmpty.thisIsFalse()
                }
            }
            case(3) {
                action {
                    it.canSelect("Apps")
                        .thisIsTrue()
                }.expectation {
                    it.isEmpty.thisIsFalse()
                }
            }
            case(4) {
                action {
                    it.canSelect("<Apps>:not")
                        .thisIsFalse()
                }.expectation {
                    it.isEmpty.thisIsTrue()
                }
            }
        }
    }

}