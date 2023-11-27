package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.branchextension.ifTrue
import shirates.core.driver.commandextension.caption
import shirates.core.driver.commandextension.exist
import shirates.core.driver.commandextension.macro
import shirates.core.driver.commandextension.tapWithScrollDown
import shirates.core.driver.isEmulator
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class IfTrueIfFalse1 : UITest() {

    @Test
    @Order(10)
    fun ifTrueIfFalse() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    isEmulator
                        .ifTrue {
                            it.caption("on emulator")
                                .tapWithScrollDown("About emulated device")
                        }
                        .ifElse {
                            it.caption("on real device")
                                .tapWithScrollDown("About phone")
                        }
                }.expectation {
                    isEmulator
                        .ifTrue {
                            it.caption("on emulator")
                                .exist("@About emulated device")
                        }
                        .ifElse {
                            it.caption("on real device")
                                .exist("@About phone")
                        }
                }
            }
        }
    }

    @Test
    @Order(20)
    fun ifTrueIfFalse_withMessage() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    isEmulator
                        .ifTrue("on emulator") {
                            it.tapWithScrollDown("About emulated device")
                        }
                        .ifElse("on real device") {
                            it.tapWithScrollDown("About phone")
                        }
                }.expectation {
                    isEmulator
                        .ifTrue("on emulator") {
                            it.exist("@About emulated device")
                        }
                        .ifElse("on real device") {
                            it.exist("@About phone")
                        }
                }
            }
        }
    }
}