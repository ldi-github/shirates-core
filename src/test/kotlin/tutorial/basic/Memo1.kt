package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest

@Testrun("testConfig/android/calculator/testrun.properties")
class Memo1 : UITest() {

    @Order(10)
    @Test
    fun writeMemo_readMemo() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Calculator Main Screen]")
                    writeMemo("First key", "[1]")
                    writeMemo("Second key", "[+]")
                    writeMemo("Third key", "[2]")
                    writeMemo("Fourth key", "[=]")
                    writeMemo("Expected result", "3")
                }.action {
                    it
                        .tap(readMemo("First key"))
                        .tap(readMemo("Second key"))
                        .tap(readMemo("Third key"))
                        .tap(readMemo("Fourth key"))
                }.expectation {
                    it.select("[result final]")
                        .textIs(readMemo("Expected result"))
                }
            }
        }
    }

    @Order(20)
    @Test
    fun memoTextAs_readMemo() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Calculator Main Screen]")
                }.action {
                    it.tap("[1]")
                        .tap("[2]")
                        .tap("[+]")
                        .tap("[3]")
                        .tap("[=]")
                }.expectation {
                    it.select("[result final]")
                        .textIs("15")
                        .memoTextAs("result1")    // memo TestElement.text as "result1"
                }
            }

            case(2) {
                condition {
                    it.tap("[AC]")
                }.action {
                    it.readMemo("result1")
                        .forEach { key ->
                            it.tap("[$key]")
                        }
                    it.tap("[+]")
                        .tap("[5]")
                        .tap("[=]")
                }.expectation {
                    it.select("[result final]")
                        .textIs("20")
                }
            }
        }

    }
}