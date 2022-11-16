package shirates.core.uitest.android.basic

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.opentest4j.TestAbortedException
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.exist
import shirates.core.driver.commandextension.sendKeys
import shirates.core.driver.commandextension.tap
import shirates.core.driver.commandextension.valueIs
import shirates.core.exception.TestConfigException
import shirates.core.exception.TestDriverException
import shirates.core.logging.CodeExecutionContext
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.report.TestResultCollector
import shirates.core.testcode.SheetName
import shirates.core.testcode.UITest

@SheetName("CAEPatternTest")
@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
class CAEPatternTest : UITest() {

    @Test
    @Order(0)
    fun testLog_isIn() {

        assertThat(CodeExecutionContext.isInScenario).isFalse()
        scenario {
            assertThat(CodeExecutionContext.isInScenario).isTrue()
            assertThat(CodeExecutionContext.isInCase).isFalse()
            assertThat(CodeExecutionContext.isInCondition).isFalse()
            assertThat(CodeExecutionContext.isInAction).isFalse()
            assertThat(CodeExecutionContext.isInExpectation).isFalse()
            case(1) {
                assertThat(CodeExecutionContext.isInScenario).isTrue()
                assertThat(CodeExecutionContext.isInCase).isTrue()
                assertThat(CodeExecutionContext.isInCondition).isFalse()
                assertThat(CodeExecutionContext.isInAction).isFalse()
                assertThat(CodeExecutionContext.isInExpectation).isFalse()
                condition {
                    assertThat(CodeExecutionContext.isInScenario).isTrue()
                    assertThat(CodeExecutionContext.isInCase).isTrue()
                    assertThat(CodeExecutionContext.isInCondition).isTrue()
                    assertThat(CodeExecutionContext.isInAction).isFalse()
                    assertThat(CodeExecutionContext.isInExpectation).isFalse()
                }.action {
                    assertThat(CodeExecutionContext.isInScenario).isTrue()
                    assertThat(CodeExecutionContext.isInCase).isTrue()
                    assertThat(CodeExecutionContext.isInCondition).isFalse()
                    assertThat(CodeExecutionContext.isInAction).isTrue()
                    assertThat(CodeExecutionContext.isInExpectation).isFalse()
                }.expectation {
                    assertThat(CodeExecutionContext.isInScenario).isTrue()
                    assertThat(CodeExecutionContext.isInCase).isTrue()
                    assertThat(CodeExecutionContext.isInCondition).isFalse()
                    assertThat(CodeExecutionContext.isInAction).isFalse()
                    assertThat(CodeExecutionContext.isInExpectation).isTrue()
                }
            }
            assertThat(CodeExecutionContext.isInScenario).isTrue()
            assertThat(CodeExecutionContext.isInCase).isFalse()
            case(2) {
                assertThat(CodeExecutionContext.isInScenario).isTrue()
                assertThat(CodeExecutionContext.isInCase).isTrue()
            }
            assertThat(CodeExecutionContext.isInScenario).isTrue()
            assertThat(CodeExecutionContext.isInCase).isFalse()
        }
        assertThat(CodeExecutionContext.isInScenario).isFalse()
        assertThat(CodeExecutionContext.isInCase).isFalse()
    }

    @Test
    @Order(0)
    fun logLine_isIn() {

        TestLog.info("before scenario")
        assertThat(TestLog.lastTestLog?.isInScenario).isFalse()
        assertThat(TestLog.lastTestLog?.isInCase).isFalse()
        assertThat(TestLog.lastTestLog?.isInCondition).isFalse()
        assertThat(TestLog.lastTestLog?.isInAction).isFalse()
        assertThat(TestLog.lastTestLog?.isInExpectation).isFalse()

        scenario {
            TestLog.info("scenario in")
            assertThat(TestLog.lastTestLog?.isInScenario).isTrue()
            assertThat(TestLog.lastTestLog?.isInCase).isFalse()
            assertThat(TestLog.lastTestLog?.isInCondition).isFalse()
            assertThat(TestLog.lastTestLog?.isInAction).isFalse()
            assertThat(TestLog.lastTestLog?.isInExpectation).isFalse()
            case(1) {
                TestLog.info("case in")
                assertThat(TestLog.lastTestLog?.isInScenario).isTrue()
                assertThat(TestLog.lastTestLog?.isInCase).isTrue()
                assertThat(TestLog.lastTestLog?.isInCondition).isFalse()
                assertThat(TestLog.lastTestLog?.isInAction).isFalse()
                assertThat(TestLog.lastTestLog?.isInExpectation).isFalse()
                condition {
                    TestLog.info("condition in")
                    assertThat(TestLog.lastTestLog?.isInScenario).isTrue()
                    assertThat(TestLog.lastTestLog?.isInCase).isTrue()
                    assertThat(TestLog.lastTestLog?.isInCondition).isTrue()
                    assertThat(TestLog.lastTestLog?.isInAction).isFalse()
                    assertThat(TestLog.lastTestLog?.isInExpectation).isFalse()
                }.action {
                    TestLog.info("condition out, action in")
                    assertThat(TestLog.lastTestLog?.isInScenario).isTrue()
                    assertThat(TestLog.lastTestLog?.isInCase).isTrue()
                    assertThat(TestLog.lastTestLog?.isInCondition).isFalse()
                    assertThat(TestLog.lastTestLog?.isInAction).isTrue()
                    assertThat(TestLog.lastTestLog?.isInExpectation).isFalse()
                }.expectation {
                    TestLog.info("action out, expectation in")
                    assertThat(TestLog.lastTestLog?.isInScenario).isTrue()
                    assertThat(TestLog.lastTestLog?.isInCase).isTrue()
                    assertThat(TestLog.lastTestLog?.isInCondition).isFalse()
                    assertThat(TestLog.lastTestLog?.isInAction).isFalse()
                    assertThat(TestLog.lastTestLog?.isInExpectation).isTrue()
                }
                TestLog.info("expectation out")
                assertThat(TestLog.lastTestLog?.isInScenario).isTrue()
                assertThat(TestLog.lastTestLog?.isInCase).isTrue()
                assertThat(TestLog.lastTestLog?.isInCondition).isFalse()
                assertThat(TestLog.lastTestLog?.isInAction).isFalse()
                assertThat(TestLog.lastTestLog?.isInExpectation).isFalse()
            }
            TestLog.info("case out")
            assertThat(TestLog.lastTestLog?.isInScenario).isTrue()
            assertThat(TestLog.lastTestLog?.isInCase).isFalse()
            assertThat(TestLog.lastTestLog?.isInCondition).isFalse()
            assertThat(TestLog.lastTestLog?.isInAction).isFalse()
            assertThat(TestLog.lastTestLog?.isInExpectation).isFalse()
        }
        TestLog.info("scenario out")
        assertThat(TestLog.lastTestLog?.isInScenario).isFalse()
        assertThat(TestLog.lastTestLog?.isInCase).isFalse()
        assertThat(TestLog.lastTestLog?.isInCondition).isFalse()
        assertThat(TestLog.lastTestLog?.isInAction).isFalse()
        assertThat(TestLog.lastTestLog?.isInExpectation).isFalse()
    }

    @Test
    @Order(5)
    fun validateCalling() {

        scenario {
            assertThatThrownBy {
                scenario {

                }
            }.isInstanceOf(TestDriverException::class.java)
                .hasMessage("Calling scenario function in scenario function is not allowed.")

            case(1) {
                assertThatThrownBy {
                    case(2) {

                    }
                }.isInstanceOf(TestDriverException::class.java)
                    .hasMessage("Calling case function in case function is not allowed.")

                condition {
                    assertThatThrownBy {
                        condition {

                        }
                    }.isInstanceOf(TestDriverException::class.java)
                        .hasMessage("Calling condition function in condition function is not allowed.")
                    assertThatThrownBy {
                        action {

                        }
                    }.isInstanceOf(TestDriverException::class.java)
                        .hasMessage("Calling action function in condition function is not allowed.")
                    assertThatThrownBy {
                        expectation {

                        }
                    }.isInstanceOf(TestDriverException::class.java)
                        .hasMessage("Calling expectation function in condition function is not allowed.")
                }.action {
                    assertThatThrownBy {
                        condition {

                        }
                    }.isInstanceOf(TestDriverException::class.java)
                        .hasMessage("Calling condition function in action function is not allowed.")
                    assertThatThrownBy {
                        action {

                        }
                    }.isInstanceOf(TestDriverException::class.java)
                        .hasMessage("Calling action function in action function is not allowed.")
                    assertThatThrownBy {
                        expectation {

                        }
                    }.isInstanceOf(TestDriverException::class.java)
                        .hasMessage("Calling expectation function in action function is not allowed.")
                }.expectation {
                    assertThatThrownBy {
                        condition {

                        }
                    }.isInstanceOf(TestDriverException::class.java)
                        .hasMessage("Calling condition function in expectation function is not allowed.")
                    assertThatThrownBy {
                        action {

                        }
                    }.isInstanceOf(TestDriverException::class.java)
                        .hasMessage("Calling action function in expectation function is not allowed.")
                    assertThatThrownBy {
                        expectation {

                        }
                    }.isInstanceOf(TestDriverException::class.java)
                        .hasMessage("Calling expectation function in expectation function is not allowed.")
                }

            }
        }
    }

    @Test
    @Order(10)
    fun noScenario() {

    }

    @Test
    @Order(20)
    fun multipleScenarioInTestFunction() {

        scenario {

        }

        assertThatThrownBy {
            scenario {

            }
        }.isInstanceOf(TestDriverException::class.java)
            .hasMessage(message(id = "multipleScenarioNotAllowed"))
    }

    @Test
    @Order(30)
    fun caseOutOfScenario() {

        assertThatThrownBy {
            case(1) {

            }
        }.isInstanceOf(TestConfigException::class.java)
            .hasMessage(message(id = "callCaseFunctionInScenarioFunction", subject = "caseOutOfScenario"))
    }

    @Test
    @Order(40)
    fun noTestResult() {

        TestLog.clear()

        assertThatThrownBy {
            scenario {
                case(1) {
                }

                case(2) {
                }
            }
        }.isInstanceOf(TestAbortedException::class.java)
            .hasMessage(message(id = "noTestResultFound"))

        val lines = TestLog.lines.filter { it.testScenarioId == TestLog.testScenarioId }
        val c = TestResultCollector(lines)
        assertThat(c.scenarios.count()).isEqualTo(1)
        assertThat(c.scenarios[0].result.label).isEqualTo("-")

        assertThat(c.cases.count()).isEqualTo(2)
        assertThat(c.cases[0].result.label).isEqualTo("-")
        assertThat(c.cases[1].result.label).isEqualTo("-")
    }

    @Test
    @Order(50)
    fun noTestResult2() {

        TestLog.clear()

        assertThatThrownBy {
            scenario {
                case(1) {
                    condition {
                        OK("ok in condition block")
                    }
                }
            }
        }.isInstanceOf(TestAbortedException::class.java)
            .hasMessage(message(id = "noTestResultFound"))

        val lines = TestLog.lines.filter { it.testScenarioId == TestLog.testScenarioId }
        val c = TestResultCollector(lines)
        assertThat(c.scenarios.count()).isEqualTo(1)
        assertThat(c.scenarios[0].result.label).isEqualTo("ok")

        assertThat(c.cases.count()).isEqualTo(1)
        assertThat(c.cases[0].result.label).isEqualTo("ok")
    }

    @Test
    @Order(60)
    @DisplayName("OK test")
    fun okTest() {

        TestLog.clear()

        scenario {
            case(1) {
                condition {
                    OK("okay")
                }.action {
                    OK("okay 2")
                }.expectation {
                    OK("okay 3")
                }
            }

            case(2) {
                condition {
                    OK("OK")
                }.action {
                    OK("OK2")
                }.expectation {
                    OK("OK3")
                }
            }
        }

        val c = TestResultCollector(TestLog.lines.filter { it.testScenarioId == TestLog.testScenarioId })
        assertThat(c.scenarios[0].result.label).isEqualTo("OK")
        assertThat(c.scenarios[0].resultMessage).isEqualTo("")

        assertThat(c.cases.count()).isEqualTo(2)
        assertThat(c.cases[0].result.label).isEqualTo("OK")
        assertThat(c.cases[0].resultMessage).isEqualTo("")
        assertThat(c.cases[1].result.label).isEqualTo("OK")
        assertThat(c.cases[1].resultMessage).isEqualTo("")
    }

    @Test
    @Order(70)
    @DisplayName("NG test")
    fun ngTest() {

        TestLog.clear()

        assertThatThrownBy {
            scenario {
                case(1) {
                    condition {
                        NG("no good")
                    }.action {
                        OK()
                    }.expectation {
                        OK()
                    }
                }

                case(2) {
                    condition {
                        NG("no good 2")
                    }.action {
                        OK()
                    }.expectation {
                        OK()
                    }
                }
            }
        }.isInstanceOf(AssertionError::class.java)
            .hasMessage("no good")

        val c = TestResultCollector(TestLog.lines.filter { it.testScenarioId == TestLog.testScenarioId })
        assertThat(c.cases.count()).isEqualTo(1)

        assertThat(c.cases[0].result.label).isEqualTo("NG")
        assertThat(c.cases[0].resultMessage).isEqualTo("no good")

        assertThat(c.scenarios[0].result.label).isEqualTo("NG")
        assertThat(c.scenarios[0].resultMessage).isEqualTo("no good")
    }

    @Test
    @Order(80)
    @DisplayName("SKIP test")
    fun skipTest() {

        TestLog.clear()

        assertThatThrownBy {
            scenario {
                case(1) {
                    condition {
                        SKIP_CASE()
                    }.action {
                    }.expectation {
                        it.exist("Network & internet")
                    }
                }
                case(2) {
                    condition {
                        it.exist("[Search Button]")
                    }.action {
                        it.tap("[Search Button]")
                            .sendKeys("abc")
                    }.expectation {
                        it.valueIs("abc")
                    }
                }
            }
        }.isInstanceOf(TestAbortedException::class.java)
            .hasMessage("Test skipped.")

        val c = TestResultCollector(TestLog.lines.filter { it.testScenarioId == TestLog.testScenarioId })
        assertThat(c.cases.count()).isEqualTo(2)

        assertThat(c.cases[0].result.label).isEqualTo("SKIP")
        assertThat(c.cases[0].resultMessage).isEqualTo("Skipping case")

        assertThat(c.cases[1].result.label).isEqualTo("OK")
        assertThat(c.cases[1].resultMessage).isEqualTo("")

        assertThat(c.scenarios[0].result.label).isEqualTo("SKIP")
        assertThat(c.scenarios[0].resultMessage).isEqualTo("Skipping case")
    }

    @Test
    @Order(90)
    @DisplayName("NOTIMPL test")
    fun notimplTest() {

        TestLog.clear()

        assertThatThrownBy {
            scenario {
                case(1) {
                    condition {
                        OK()
                    }.action {
                        OK()
                    }.expectation {
                        OK()
                    }
                }
                case(2) {
                    condition {
                        NOTIMPL()
                    }.action {
                        OK()
                    }.expectation {
                        OK()
                    }
                }
                case(3) {
                    action {
                        OK()
                    }.expectation {
                        OK()
                    }
                }
            }
        }.isInstanceOf(TestAbortedException::class.java)
            .hasMessage(message(id = "NOTIMPL"))

        val c = TestResultCollector(TestLog.lines.filter { it.testScenarioId == TestLog.testScenarioId })
        assertThat(c.cases.count()).isEqualTo(2)

        assertThat(c.cases[0].result.label).isEqualTo("OK")
        assertThat(c.cases[0].resultMessage).isEqualTo("")

        assertThat(c.cases[1].result.label).isEqualTo("NOTIMPL")
        assertThat(c.cases[1].resultMessage).isEqualTo(message(id = "NOTIMPL"))

        assertThat(c.scenarios[0].result.label).isEqualTo("NOTIMPL")
        assertThat(c.scenarios[0].resultMessage).isEqualTo(message(id = "NOTIMPL"))
    }

    @Test
    @Order(100)
    @DisplayName("ExpectationNotImplemented test")
    fun expectationNotImplementedTest() {

        TestLog.clear()

        scenario {
            case(1) {
                condition {

                }.action {

                }.expectation {

                }
            }
            case(2) {
                condition {

                }.action {

                }.expectation {

                }
            }
        }

        val c = TestResultCollector(TestLog.lines.filter { it.testScenarioId == TestLog.testScenarioId })
        assertThat(c.cases.count()).isEqualTo(2)

        assertThat(c.cases[0].result.label).isEqualTo("-")
        assertThat(c.cases[0].resultMessage).isEqualTo("")

        assertThat(c.cases[1].result.label).isEqualTo("-")
        assertThat(c.cases[1].resultMessage).isEqualTo("")

        assertThat(c.scenarios[0].result.label).isEqualTo("-")
        assertThat(c.scenarios[0].resultMessage).isEqualTo("")
    }

}