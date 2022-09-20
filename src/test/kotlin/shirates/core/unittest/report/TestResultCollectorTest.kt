package shirates.core.unittest.report

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.logging.TestLog
import shirates.core.report.TestResultCollector
import shirates.core.testcode.UnitTest

class TestResultCollectorTest : UnitTest() {

    @Test
    fun collect() {

        // Arrange
        TestLog.clear()

        run {

            TestLog.scenario("scenario1")
            Thread.sleep(100)

            TestLog.case(1)
            Thread.sleep(100)

            TestLog.action("action")
            Thread.sleep(100)

            TestLog.case(2)
            Thread.sleep(100)

            TestLog.action("action")
            Thread.sleep(100)
        }

        run {
            TestLog.scenario("scenario2")
            Thread.sleep(100)

            TestLog.case(1)
            Thread.sleep(100)

            TestLog.action("action")
            Thread.sleep(100)
        }

        // Act
        val collector = TestResultCollector(TestLog.lines)

        // Assert
        val scenario1 = collector.scenarios[0]
        val processingTime1 =
            collector.cases.filter { it.testScenarioId == scenario1.testScenarioId }.sumOf { it.processingTime.toInt() }
        assertThat(processingTime1).isEqualTo(scenario1.processingTime)

        // Assert
        val scenario2 = collector.scenarios[1]
        val processingTime2 =
            collector.cases.filter { it.testScenarioId == scenario2.testScenarioId }.sumOf { it.processingTime.toInt() }
        assertThat(processingTime2).isEqualTo(scenario2.processingTime)
    }
}