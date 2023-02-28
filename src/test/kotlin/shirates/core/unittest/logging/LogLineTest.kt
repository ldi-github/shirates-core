package shirates.core.unittest.logging

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.driver.ScrollDirection
import shirates.core.logging.LogLine
import shirates.core.logging.LogType
import shirates.core.testcode.UnitTest

class LogLineTest : UnitTest() {

    @Test
    fun testScenarioId() {

        assertThat(LogLine().testScenarioId).isEqualTo(null)
        val line =
            LogLine(testScenarioId = "uc1")
        assertThat(line.testScenarioId).isEqualTo("uc1")
    }

    @Test
    fun stepNo() {

        assertThat(LogLine().stepNo).isEqualTo(null)
        val line = LogLine(stepNo = 110)
        assertThat(line.stepNo).isEqualTo(110)
    }


    @Test
    fun testCaseId() {

        assertThat(LogLine().testCaseId).isEqualTo("")

        // Act, Assert
        val testScenarioOnly =
            LogLine(testScenarioId = "a")
        assertThat(testScenarioOnly.testCaseId).isEqualTo("a")

        // Act, Assert
        val testScenarioAndStepNo =
            LogLine(
                testScenarioId = "a",
                stepNo = 110
            )
        assertThat(testScenarioAndStepNo.testCaseId).isEqualTo("a-110")

        // Act, Assert
        val stepNoOnly = LogLine(stepNo = 110)
        assertThat(stepNoOnly.testCaseId).isEqualTo("110")
    }

    @Test
    fun NONE() {

        val logLine = LogLine(message = "Message")
        assertThat(logLine.message).isEqualTo("Message")
        assertThat(logLine.logType).isEqualTo(LogType.NONE)
        assertThat(logLine.lineNumber).isEqualTo(0)
        assertThat(logLine.testCaseId).isEqualTo("")
    }

    @Test
    fun INFO() {

        val logLine =
            LogLine(
                message = "Message",
                logType = LogType.INFO,
                testScenarioId = "a",
                stepNo = 100
            )
        assertThat(logLine.message).isEqualTo("Message")
        assertThat(logLine.logType).isEqualTo(LogType.INFO)
        assertThat(logLine.testCaseId).isEqualTo("a-100")
    }

    @Test
    fun isForDetail() {

        assertThat(LogLine(logType = LogType.NONE).isForDetail).isTrue()
        assertThat(LogLine(logType = LogType.TRACE).isForDetail).isFalse()
        assertThat(LogLine(logType = LogType.INFO).isForDetail).isTrue()
        assertThat(LogLine(logType = LogType.WARN).isForDetail).isTrue()
        assertThat(LogLine(logType = LogType.ERROR).isForDetail).isTrue()

        assertThat(LogLine(logType = LogType.ACTION).isForDetail).isTrue()
        assertThat(LogLine(logType = LogType.OK).isForDetail).isTrue()
        assertThat(LogLine(logType = LogType.NG).isForDetail).isTrue()
    }

    @Test
    fun isForSimple() {

        assertThat(LogLine(logType = LogType.NONE).isForSimple).isTrue()
        assertThat(LogLine(logType = LogType.TRACE).isForSimple).isFalse()
        assertThat(LogLine(logType = LogType.INFO).isForSimple).isFalse()
        assertThat(LogLine(logType = LogType.WARN).isForSimple).isTrue()
        assertThat(LogLine(logType = LogType.ERROR).isForSimple).isTrue()

        assertThat(LogLine(logType = LogType.ACTION).isForSimple).isTrue()
        assertThat(LogLine(logType = LogType.OK).isForSimple).isTrue()
        assertThat(LogLine(logType = LogType.NG).isForSimple).isTrue()

        assertThat(LogLine(logType = LogType.INFO, scriptCommand = "parameter").isForSimple).isFalse()

        // screenshot is true
        assertThat(
            LogLine(
                logType = LogType.SCREENSHOT,
                message = "1.png",
                scriptCommand = "screenshot"
            ).isForSimple
        ).isTrue()
        // screenshot in "withScroll" is false
        assertThat(
            LogLine(
                logType = LogType.SCREENSHOT,
                message = "1.png", scriptCommand = "screenshot", withScrollDirection = ScrollDirection.Down
            ).isForSimple
        ).isFalse()
        // scrollDown in "withScroll" is false
        assertThat(
            LogLine(
                logType = LogType.OPERATE,
                scriptCommand = "scrollDown", withScrollDirection = ScrollDirection.Down
            ).isForSimple
        ).isFalse()
    }

    @Test
    fun isForCommandList() {

        assertThat(LogLine(logType = LogType.NONE).isForCommandList).isTrue()
        assertThat(LogLine(logType = LogType.TRACE).isForCommandList).isFalse()
        assertThat(LogLine(logType = LogType.INFO).isForCommandList).isFalse()
        assertThat(LogLine(logType = LogType.WARN).isForCommandList).isTrue()
        assertThat(LogLine(logType = LogType.ERROR).isForCommandList).isTrue()

        assertThat(LogLine(logType = LogType.ACTION).isForCommandList).isTrue()
        assertThat(LogLine(logType = LogType.OK).isForCommandList).isTrue()
        assertThat(LogLine(logType = LogType.NG).isForCommandList).isTrue()

        assertThat(LogLine(logType = LogType.NONE, scriptCommand = "parameter").isForCommandList).isTrue()

        // screenshot is true
        assertThat(
            LogLine(
                logType = LogType.SCREENSHOT,
                message = "1.png",
                scriptCommand = "screenshot"
            ).isForCommandList
        ).isTrue()
    }

    @Test
    fun isScreenshot() {

        val l1 = LogLine(
            message = "1.png",
            scriptCommand = "screenshot"
        )
        assertThat(l1.isScreenshot).isTrue()

        val l2 = LogLine(
            message = "1.png",
            scriptCommand = "macro"
        )
        assertThat(l2.isScreenshot).isFalse()
    }

    @Test
    fun getHeader() {

        val header = LogLine.getHeader()
        assertThat(header).isEqualTo("lineNo\tlogDateTime\ttestCaseId\tlogType\tos\tspecial\tgroup\tmessage\tlevel\tcommand\tsubject\targ1\targ2\tresult")
    }

}