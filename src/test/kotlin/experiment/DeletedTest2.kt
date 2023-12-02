package experiment

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.describe
import shirates.core.testcode.Deleted
import shirates.core.testcode.SheetName
import shirates.core.testcode.UITest

@Deleted("This test is deleted")
@SheetName("ToBeDeletedTest test")
@Testrun("testConfig/android/androidSettings/testrun.properties")
class DeletedTest2 : UITest() {

    @Test
    @Order(10)
    fun A0010() {

        scenario {
            case(1) {
                condition {
                    describe("condition")
                }.action {
                    describe("action")
                }.expectation {
                    OK("expectation")
                }
            }
            case(2) {
                action {
                    describe("action")
                }.expectation {
                    OK("expectation")
                }
            }
            case(3) {
                action {
                    describe("action")
                }.expectation {
                    OK("expectation")
                }
            }
        }
    }

    @Deleted("Moved to C0120")
    @Test
    @Order(20)
    fun A0020() {

        scenario {
            case(1) {
                condition {
                    describe("condition")
                }.action {
                    describe("action")
                }.expectation {
                    OK("expectation")
                }
            }
            case(2) {
                action {
                    describe("action")
                }.expectation {
                    OK("expectation")
                }
            }
            case(3) {
                action {
                    describe("action")
                }.expectation {
                    OK("expectation")
                }
            }
        }
    }

    @Test
    @Order(30)
    fun A0030() {

        scenario {
            case(1) {
                condition {
                    describe("condition")
                }.action {
                    describe("action")
                }.expectation {
                    OK("expectation")
                }
            }
            case(2) {
                action {
                    describe("action")
                }.expectation {
                    OK("expectation")
                }
            }
            case(3) {
                action {
                    describe("action")
                }.expectation {
                    OK("expectation")
                }
            }
        }
    }

}