package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.branchextension.ifStringIs
import shirates.core.driver.commandextension.describe
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class IfStringIs1 : UITest() {

    @Test
    @Order(10)
    fun ifStringIsTest() {

        scenario {
            case(1) {
                action {
                    "ABC".ifStringIs("ABC") {
                        describe("in ifStringIs")   // executed
                    }.ifStartsWith("AB") {
                        describe("in ifStartsWith") // executed
                    }.ifContains("B") {
                        describe("in ifContains")   // executed
                    }.ifEndsWith("C") {
                        describe("in ifEndsWith")   // executed
                    }.ifElse {
                        describe("in ifElse")   // NOT executed
                    }
                }
            }
            case(2) {
                action {
                    "ABC".ifStringIs("ABC") {
                        describe("in ifStringIs")   // executed
                    }.elseIfContains("AB") {
                        describe("in ifStartsWith") // NOT executed
                    }.elseIfContains("B") {
                        describe("in ifContains")   // NOT executed
                    }.elseIfContains("C") {
                        describe("in ifEndsWith")   // NOT executed
                    }.ifElse {
                        describe("in ifElse")   // NOT executed
                    }
                }
            }
            case(3) {
                action {
                    "ABC".ifStringIs("a") {
                        describe("in ifStringIs")   // NOT executed
                    }.ifStartsWith("b") {
                        describe("in ifStartsWith") // NOT executed
                    }.ifElse {
                        describe("in ifElse")   // executed
                    }
                }
            }
        }
    }

}