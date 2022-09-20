package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.branchextension.osaifuKeitai
import shirates.core.driver.branchextension.osaifuKeitaiNot
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class Descriptor1 : UITest() {

    @Test
    @Order(10)
    fun descriptors() {

        scenario {
            case(1) {
                condition {
                    describe("describe")
                    procedure("procedure") {
                        manual("manual")
                    }
                    caption("caption")
                    comment("comment")
                    manual("manual")
                    output("output")
                }.action {
                    caption("caption")
                        .describe("describe1")
                        .describe("describe2")
                    procedure("procedure") {
                        manual("manual")
                    }
                }.expectation {
                    target("target1")
                        .manual("manual")
                    target("target2")
                        .knownIssue("knownIssue", ticketUrl = "https://example.com/ticket/12345")
                }
            }
        }

    }

    @Test
    fun example() {

        scenario {
            case(1) {
                condition {
                    macro("[Setup stock]")
                    macro("[Login]")
                    macro("[Order Screen]")
                }.action {
                    osaifuKeitai {
                        caption("Osaifu keitai")
                            .procedure("Order by osaifu-keitai") {
                                // implement
                            }
                            .comment("note: must be charged")
                    }
                    osaifuKeitaiNot {
                        caption("Not osaifu keitai")
                            .procedure("Order by credit card") {
                                // implement
                            }
                    }
                }.expectation {
                    target("[Completion message]")
                        .manual("is displayed")
                    target("[OK]")
                        .manual("is displayed")
                }
            }

            case(2) {
                action {
                    manual("Tap [OK]")
                }.expectation {
                    manual("[Home screen] is displayed")
                    target("target3")
                        .knownIssue("knownIssue", ticketUrl = "https://example.com/ticket/12345")
                }
            }
        }
    }
}