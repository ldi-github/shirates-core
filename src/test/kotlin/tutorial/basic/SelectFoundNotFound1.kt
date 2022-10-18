package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class SelectFoundNotFound1 : UITest() {

    @Test
    @Order(10)
    fun select_found() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    describe("select(\"Network & internet\")")
                        .select("Network & internet")
                }.expectation {
                    it.textIs("Network & internet")
                }
            }
        }
    }

    @Test
    @Order(20)
    fun select_notfound_ERROR() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    describe("select(\"no exist\")")
                        .select("no exist")
                }
            }
        }
    }

    @Test
    @Order(30)
    fun select_notfound_empty() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    describe("select(\"System\", throwsException = false)")
                        .select("System", throwsException = false)
                }.expectation {
                    it.isFound.thisIsFalse("Element not found")
                    it.isEmpty.thisIsTrue("it is empty element")
                    it.hasError.thisIsTrue("hasError is true")
                }
            }
        }
    }

    @Test
    @Order(40)
    fun selectWithScrollDown_found() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    describe("selectWithScrollDown(\"System\")")
                        .selectWithScrollDown("System")
                }.expectation {
                    it.isFound.thisIsTrue("${it.selector} found")
                }
            }
        }
    }

    @Test
    @Order(50)
    fun selectWithScrollDown_notfound() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    describe("selectWithScrollDown(\"no exist\")")
                        .selectWithScrollDown("no exist", throwsException = false)
                }.expectation {
                    it.isFound.thisIsFalse("${it.selector} not found")
                }
            }
        }
    }

}