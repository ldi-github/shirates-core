package shirates.core.unittest.logging

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import shirates.core.driver.TestMode
import shirates.core.exception.CaseSkipException
import shirates.core.exception.TestConfigException
import shirates.core.exception.TestDriverException
import shirates.core.exception.TestNGException
import shirates.core.logging.LogFileFormat
import shirates.core.logging.LogType
import shirates.core.logging.TestLog
import shirates.core.testcode.UnitTest

class TestLogTest : UnitTest() {

    @Test
    fun write() {

        // Arrange
        val message = "testing write"

        run {
            // Arrange
            val logCount = TestLog.lines.count()
            val historyCount = TestLog.histories.count()

            // Act
            val line = TestLog.write(message)
            // Assert
            assertThat(line.logType).isEqualTo(LogType.NONE)
            assertThat(line.message).isEqualTo(message)
            assertThat(TestLog.lines.count()).isEqualTo(logCount + 1)
            assertThat(TestLog.histories.count()).isEqualTo(historyCount + 1)
        }

        run {
            // Arrange
            val logCount = TestLog.lines.count()
            val historyCount = TestLog.histories.count()

            // Act
            val line = TestLog.write(
                message,
                LogType.INFO,
                log = true
            )
            // Assert
            assertThat(line.logType).isEqualTo(LogType.INFO)
            assertThat(line.message).isEqualTo(message)
            assertThat(TestLog.lines.count()).isEqualTo(logCount + 1)
            assertThat(TestLog.histories.count()).isEqualTo(historyCount + 1)
        }

        run {
            // Arrange
            val logCount = TestLog.lines.count()
            val historyCount = TestLog.histories.count()

            // Act
            val line = TestLog.write(
                message,
                LogType.OK,
                log = false
            )
            // Assert
            assertThat(line.logType).isEqualTo(LogType.OK)
            assertThat(line.message).isEqualTo(message)
            assertThat(TestLog.lines.count()).isEqualTo(logCount)
            assertThat(TestLog.histories.count()).isEqualTo(historyCount)
        }
    }

    @Test
    fun info() {

        // Arrange
        val message = "testing info"

        run {
            // Act
            val line = TestLog.info(message)
            // Assert
            assertThat(line.logType).isEqualTo(LogType.INFO)
            assertThat(line.message).isEqualTo(message)
        }

        run {
            // Act
            val line = TestLog.info(message, true)
            // Assert
            assertThat(line.logType).isEqualTo(LogType.INFO)
            assertThat(line.message).isEqualTo(message)
        }

        run {
            // Act
            val line = TestLog.info(message, false)
            assertThat(line.logType).isEqualTo(LogType.NONE)
            assertThat(line.message).isEqualTo("")
        }
    }

    @Test
    fun warn() {

        // Arrange
        val message = "testing warn"

        run {
            // Act
            val line = TestLog.warn(message)
            // Assert
            assertThat(line.logType).isEqualTo(LogType.WARN)
            assertThat(line.message).isEqualTo(message)
        }

        run {
            // Act
            val line = TestLog.warn(message, true)
            // Assert
            assertThat(line.logType).isEqualTo(LogType.WARN)
            assertThat(line.message).isEqualTo(message)
        }

        run {
            // Act
            val line = TestLog.warn(message, false)
            assertThat(line.logType).isEqualTo(LogType.NONE)
            assertThat(line.message).isEqualTo("")
        }
    }

    @Test
    fun error() {

        // Arrange
        val ex = IllegalArgumentException("testing error")

        run {
            // Act
            val line = TestLog.error(ex)
            // Assert
            assertThat(line.logType).isEqualTo(LogType.ERROR)
            assertThat(line.message).isEqualTo(ex.toString())
        }

        run {
            // Act
            val line = TestLog.error(ex, true)
            // Assert
            assertThat(line.logType).isEqualTo(LogType.ERROR)
            assertThat(line.message).isEqualTo(ex.toString())
        }

        run {
            // Act
            val line = TestLog.error(ex, false)
            // Assert
            assertThat(line.logType).isEqualTo(LogType.NONE)
            assertThat(line.message).isEqualTo("")
        }
    }

    @Test
    fun trace() {

        // Arrange
        val message = "testing trace"
        TestLog.enableTrace = true

        run {
            // Act
            val line = TestLog.trace(message)
            // Assert
            assertThat(line.logType).isEqualTo(LogType.TRACE)
            assertThat(line.message).endsWith(message)
        }

        run {
            // Arrange
            TestLog.enableTrace = false

            // Act
            val line = TestLog.trace(message)
            // Assert
            assertThat(line.logType).isEqualTo(LogType.NONE)
        }
    }

    @Test
    fun action() {

        // Arrange
        val message = "testing action"

        run {
            // Act
            val line = TestLog.action(message)
            // Assert
            assertThat(line.logType).isEqualTo(LogType.ACTION)
            assertThat(line.message).isEqualTo(message)
        }

        run {
            // Act
            val line = TestLog.action(message, true)
            // Assert
            assertThat(line.logType).isEqualTo(LogType.ACTION)
            assertThat(line.message).isEqualTo(message)
        }

        run {
            // Act
            val line = TestLog.action(message, false)
            // Assert
            assertThat(line.logType).isEqualTo(LogType.ACTION)
            assertThat(line.message).isEqualTo(message)
        }
    }

    @Test
    fun ok() {

        // Arrange
        val message = "testing ok"

        run {
            // Act
            val line = TestLog.ok(message)
            // Assert
            assertThat(line.logType).isEqualTo(LogType.ok)
            assertThat(line.message).isEqualTo(message)
        }

        run {
            // Act
            val line = TestLog.ok(message, log = true)
            // Assert
            assertThat(line.logType).isEqualTo(LogType.ok)
            assertThat(line.message).isEqualTo(message)
        }

        run {
            // Act
            val line = TestLog.ok(message, log = false)
            // Assert
            assertThat(line.logType).isEqualTo(LogType.ok)
            assertThat(line.message).isEqualTo(message)
        }
    }

    @Test
    fun ng() {

        // Arrange
        val ex = TestNGException("testing ng")

        run {
            // Act
            val line = TestLog.ng(ex)
            // Assert
            assertThat(line.logType).isEqualTo(LogType.NG)
            assertThat(line.message).isEqualTo(ex.message)
        }

        run {
            // Act
            val line = TestLog.ng(ex, log = true)
            // Assert
            assertThat(line.logType).isEqualTo(LogType.NG)
            assertThat(line.message).isEqualTo(ex.message)
        }

        run {
            // Act
            val line = TestLog.ng(ex, log = false)
            assertThat(line.logType).isEqualTo(LogType.NG)
            assertThat(line.message).isEqualTo(ex.message)
        }
    }

    @Test
    fun notImpl() {

        // Arrange
        val ex = NotImplementedError("testing notimpl")

        run {
            // Act
            val line = TestLog.notImpl(ex)
            // Assert
            assertThat(line.logType).isEqualTo(LogType.NOTIMPL)
            assertThat(line.message).isEqualTo(ex.message)
        }

        run {
            // Act
            val line = TestLog.notImpl(ex, log = true)
            // Assert
            assertThat(line.logType).isEqualTo(LogType.NOTIMPL)
            assertThat(line.message).isEqualTo(ex.message)
        }

        run {
            // Act
            val line = TestLog.notImpl(ex, log = false)
            // Assert
            assertThat(line.logType).isEqualTo(LogType.NOTIMPL)
            assertThat(line.message).isEqualTo(ex.message)
        }

        run {
            // Act
            val line = TestLog.notImpl(ex, log = true)
            // Assert
            assertThat(line.logType).isEqualTo(LogType.NOTIMPL)
            assertThat(line.message).isEqualTo(ex.message)
        }

        run {
            // Act
            val line = TestLog.notImpl(ex, log = false)
            // Assert
            assertThat(line.logType).isEqualTo(LogType.NOTIMPL)
            assertThat(line.message).isEqualTo(ex.message)
        }
    }

    @Test
    fun knownIssue() {

        // Arrange
        val ticketNo = "TICKET-123"
        val ticketUrl = "https://jira.example.com/browse/${ticketNo}"
        val message = "[KNOWNISSUE]${ticketNo} ${ticketUrl}"

        run {
            // Act
            val line = TestLog.knownIssue(message = ticketNo, ticketUrl = ticketUrl)
            // Assert
            assertThat(line.logType).isEqualTo(LogType.KNOWNISSUE)
            assertThat(line.message).isEqualTo(message)
        }

        run {
            // Act
            val line = TestLog.knownIssue(message = ticketNo, ticketUrl = ticketUrl, log = true)
            // Assert
            assertThat(line.logType).isEqualTo(LogType.KNOWNISSUE)
            assertThat(line.message).isEqualTo(message)
        }

        run {
            // Act
            val line = TestLog.knownIssue(message = ticketNo, ticketUrl = ticketUrl, log = false)
            // Assert
            assertThat(line.logType).isEqualTo(LogType.KNOWNISSUE)
            assertThat(line.message).isEqualTo(message)
        }
    }

    @Test
    fun clear() {

        TestLog.clear()
        assertThat(TestLog.lines.count()).isEqualTo(0)

        TestLog.write("a")
        assertThat(TestLog.lines.count()).isEqualTo(1)

        TestLog.clear()
        assertThat(TestLog.lines.count()).isEqualTo(0)
    }

    @Test
    fun getTest() {

        TestLog.clear()
        assertThatThrownBy {
            TestLog[0]
        }.isInstanceOf(IndexOutOfBoundsException::class.java)

        TestLog.write("a")
        val l1 = TestLog[0]
        assertThat(l1.message).isEqualTo("a")

        TestLog.write("b")
        val l2 = TestLog[1]
        assertThat(l2.message).isEqualTo("b")
    }

    @Test
    fun toStringTest() {

        TestLog.clear()
        assertThat(TestLog.toString()).isEqualTo("")

        TestLog.write("a")
        println(TestLog.toString())

        TestLog.write("b")
        println(TestLog.toString())
    }

    @Test
    fun outputLogFile() {

        TestLog.clear()
        TestLog.outputLogFile(
            "test",
            TestLog.lines,
            LogFileFormat.Text
        )
        TestLog.outputLogFile(
            "test",
            TestLog.lines,
            LogFileFormat.Html
        )

        TestLog.write("TestLogTest")
        TestLog.write("write")
        TestLog.info("info")
        TestLog.outputLogFile(
            "test",
            TestLog.lines,
            LogFileFormat.Text
        )
        TestLog.outputLogFile(
            "test",
            TestLog.lines,
            LogFileFormat.Html
        )
    }

    @Test
    fun lastTestLog() {

        // Arrange
        TestLog.write("write")
        // Act
        var last = TestLog.lastTestLog
        // Assert
        assertThat(last?.message).isEqualTo("write")

        // Act
        last = TestLog.info("info")
        // Assert
        assertThat(last.message).isEqualTo("info")
    }

    @Test
    fun enableTestCaseLogging() {

        fun assertLogType(logType: LogType) {

            assertThat(TestLog.lastTestLog?.logType).isEqualTo(logType)
        }

        val enableTrace = TestLog.enableTrace

        try {
            TestLog.enableTrace = true

            // all LogTypes are output

            TestLog.write("write")
            assertLogType(LogType.NONE)

            TestLog.trace("trace")
            assertLogType(LogType.TRACE)

            TestLog.info("info")
            assertLogType(LogType.INFO)

            TestLog.warn("warn")
            assertLogType(LogType.WARN)

            TestLog.error(IllegalArgumentException("error"))
            assertLogType(LogType.ERROR)

            TestLog.ok("ok")
            assertLogType(LogType.ok)

            TestLog.ng(TestNGException("ng"))
            assertLogType(LogType.NG)

            TestLog.notImpl(NotImplementedError("notimpl"))
            assertLogType(LogType.NOTIMPL)

            TestLog.knownIssue("knows issue", "https://jira.example.com/issues/123")
            assertLogType(LogType.KNOWNISSUE)

            TestLog.scenario("testScenarioId")
            assertLogType(LogType.SCENARIO)

            TestLog.case(1)
            assertLogType(LogType.CASE)

            TestLog.condition("condition")
            assertLogType(LogType.CONDITION)

            TestLog.action("action")
            assertLogType(LogType.ACTION)

            TestLog.target("target")
            assertLogType(LogType.TARGET)

            TestLog.expectation("expectation")
            assertLogType(LogType.EXPECTATION)

            TestLog.operate("operate")
            assertLogType(LogType.OPERATE)

            TestLog.check("check")
            assertLogType(LogType.CHECK)

            TestLog.procedure("procedure")
            assertLogType(LogType.PROCEDURE)

            TestLog.caption("caption")
            assertLogType(LogType.CAPTION)

            TestLog.describe("description")
            assertLogType(LogType.DESCRIBE)

        } finally {
            TestLog.enableTrace = enableTrace
        }
    }

    @Test
    fun clear1() {

        // Arrange
        TestLog.scenario("testScenarioId")
        TestLog.case(1)
        assertThat(TestLog.lastScenarioLog).isNotNull
        assertThat(TestLog.lastCaseLog).isNotNull
        assertThat(TestLog.lastTestLog).isNotNull

        // Act
        TestLog.clear()

        // Assert
        assertThat(TestLog.lastScenarioLog).isNull()
        assertThat(TestLog.lastCaseLog).isNull()
        assertThat(TestLog.lastTestLog).isNull()
        assertThat(TestLog.testScenarioId).isEqualTo("")
        assertThat(TestLog.stepNo).isNull()

    }

    @Test
    fun result_OK() {

        // Arrange
        TestLog.clear()

        // ok
        TestMode.runAsExpectationBlock {
            // Arrange
            TestLog.scenario("scenario1")
            TestLog.case(1)
            assertThat(TestLog.lastScenarioLog?.message).isEqualTo("scenario1")
            assertThat(TestLog.lastCaseLog?.message).isEqualTo("(1)")

            // Act
            TestLog.ok("okay")

            // Assert
            assertScenarioAndCase(LogType.OK, "okay")
        }

        // ok can be overwritten by ok
        TestMode.runAsExpectationBlock {
            // Act
            TestLog.ok("okay2")

            // Assert
            assertScenarioAndCase(LogType.OK, "okay2")
        }

        // ok can be overwritten by SKIP(Inconclusive)
        run {
            // Act
            TestLog.skip(CaseSkipException("skip"))

            // Assert
            assertScenarioAndCase(LogType.SKIP, "skip")
        }

        // SKIP(Inconclusive) can not be overwritten by ok
        run {
            // Act
            TestLog.ok("okay")

            // Assert
            assertScenarioAndCase(LogType.SKIP, "skip")
        }

        // MANUAL(Inconclusive) can not be overwritten by ok
        run {
            // Arrange
            TestLog.lastScenarioLog!!.result = LogType.NONE
            TestLog.lastCaseLog!!.result = LogType.NONE
            TestLog.manual("test manually")
            assertScenarioAndCase(LogType.MANUAL, "test manually")

            // Act
            TestLog.ok("okay")

            // Assert
            assertScenarioAndCase(LogType.MANUAL, "test manually")
        }

        // NOTIMPL(Inconclusive) can not be overwritten by ok
        run {
            // Arrange
            TestLog.lastScenarioLog!!.result = LogType.NONE
            TestLog.lastCaseLog!!.result = LogType.NONE
            TestLog.notImpl(NotImplementedError("not implemented"))
            assertScenarioAndCase(LogType.NOTIMPL, "not implemented")

            // Act
            TestLog.ok("okay")

            // Assert
            assertScenarioAndCase(LogType.NOTIMPL, "not implemented")
        }

        // KNOWNISSUE(Inconclusive) can not be overwritten by ok
        run {
            // Arrange
            TestLog.lastScenarioLog!!.result = LogType.NONE
            TestLog.lastCaseLog!!.result = LogType.NONE
            TestLog.knownIssue(message = "known issue", ticketUrl = "http://example.com/issues/123")
            assertScenarioAndCase(LogType.KNOWNISSUE, "known issue")

            // Act
            TestLog.ok("okay")

            // Assert
            assertScenarioAndCase(LogType.KNOWNISSUE, "known issue")
        }

        // NG(Fail) can not be overwritten by ok
        run {
            // Arrange
            TestLog.lastScenarioLog!!.result = LogType.NONE
            TestLog.lastCaseLog!!.result = LogType.NONE
            TestLog.ng(TestNGException("NG"))
            assertScenarioAndCase(LogType.NG, "NG")

            // Act
            TestLog.ok("okay")

            // Assert
            assertScenarioAndCase(LogType.NG, "NG")
        }

        // ERROR(Fail) can not be overwritten by ok
        run {
            // Arrange
            TestLog.lastScenarioLog!!.result = LogType.NONE
            TestLog.lastCaseLog!!.result = LogType.NONE
            TestLog.error(TestDriverException("ERROR"))
            assertScenarioAndCase(LogType.ERROR, "ERROR")

            // Act
            TestLog.ok("okay")

            // Assert
            assertScenarioAndCase(LogType.ERROR, "ERROR")
        }

    }

    private fun assertScenarioAndCase(result: LogType, resultMessage: String) {

        assertThat(TestLog.lastScenarioLog?.result).isEqualTo(result)
        assertThat(TestLog.lastScenarioLog?.resultMessage).isEqualTo(resultMessage)
        assertThat(TestLog.lastCaseLog?.result).isEqualTo(result)
        assertThat(TestLog.lastCaseLog?.resultMessage).isEqualTo(resultMessage)
    }

    @Test
    fun result_SKIP() {

        // SKIP(Inconclusive)
        run {
            // Arrange
            TestLog.clear()
            TestLog.scenario("scenario1")
            TestLog.case(1)
            assertThat(TestLog.lastScenarioLog?.message).isEqualTo("scenario1")
            assertThat(TestLog.lastCaseLog?.message).isEqualTo("(1)")
            // Act
            TestLog.skip(CaseSkipException("skip"))
            // Assert
            assertScenarioAndCase(LogType.SKIP, "skip")
        }

        // SKIP(Inconclusive) can not be overwritten by ok
        TestMode.runAsExpectationBlock {
            // Arrange
            TestLog.lastScenarioLog!!.result = LogType.NONE
            TestLog.lastCaseLog!!.result = LogType.NONE
            TestLog.ok("okay")
            assertScenarioAndCase(LogType.OK, "okay")
            // Act
            TestLog.skip(CaseSkipException("skip"))
            // Assert
            assertScenarioAndCase(LogType.SKIP, "skip")
        }

        // SKIP(Inconclusive) can not be overwritten by SKIP
        run {
            // Arrange
            assertScenarioAndCase(LogType.SKIP, "skip")
            // Act
            TestLog.skip(CaseSkipException("skip 2"))
            // Assert
            assertScenarioAndCase(LogType.SKIP, "skip")
        }

        // MANUAL(Inconclusive) can not be overwritten by SKIP(Inconclusive)
        run {
            // Arrange
            TestLog.lastScenarioLog!!.result = LogType.NONE
            TestLog.lastCaseLog!!.result = LogType.NONE
            TestLog.manual("test manually")
            assertScenarioAndCase(LogType.MANUAL, "test manually")
            // Act
            TestLog.skip(CaseSkipException("skip"))
            // Assert
            assertScenarioAndCase(LogType.MANUAL, "test manually")
        }

        // NOTIMPL(Inconclusive) can not be overwritten by SKIP(Inconclusive)
        run {
            // Arrange
            TestLog.lastScenarioLog!!.result = LogType.NONE
            TestLog.lastCaseLog!!.result = LogType.NONE
            TestLog.notImpl(NotImplementedError("not implemented"))
            assertScenarioAndCase(LogType.NOTIMPL, "not implemented")
            // Act
            TestLog.skip(CaseSkipException("skip"))
            // Assert
            assertScenarioAndCase(LogType.NOTIMPL, "not implemented")
        }

        // KNOWNISSUE(Inconclusive) can not be overwritten by SKIP(Inconclusive)
        run {
            // Arrange
            TestLog.lastScenarioLog!!.result = LogType.NONE
            TestLog.lastCaseLog!!.result = LogType.NONE
            TestLog.knownIssue(message = "known issue", ticketUrl = "http://example.com/issues/123")
            assertScenarioAndCase(LogType.KNOWNISSUE, "known issue")
            // Act
            TestLog.skip(CaseSkipException("skip"))
            // Assert
            assertScenarioAndCase(LogType.KNOWNISSUE, "known issue")
        }

        // NG(Fail) can not be overwritten by SKIP(Inconclusive)
        run {
            // Arrange
            TestLog.lastScenarioLog!!.result = LogType.NONE
            TestLog.lastCaseLog!!.result = LogType.NONE
            TestLog.ng(TestNGException("NG"))
            assertScenarioAndCase(LogType.NG, "NG")
            // Act
            TestLog.skip(CaseSkipException("skip"))
            // Assert
            assertScenarioAndCase(LogType.NG, "NG")
        }

        // ERROR(Fail) can not be overwritten by SKIP(Inconclusivee)
        run {
            // Arrange
            TestLog.lastScenarioLog!!.result = LogType.NONE
            TestLog.lastCaseLog!!.result = LogType.NONE
            TestLog.error(TestDriverException("ERROR"))
            assertScenarioAndCase(LogType.ERROR, "ERROR")
            // Act
            TestLog.skip(CaseSkipException("skip"))
            // Assert
            assertScenarioAndCase(LogType.ERROR, "ERROR")
        }

    }

    @Test
    fun result_MANUAL() {

        // MANUAL(Inconclusive)
        run {
            // Arrange
            TestLog.clear()
            TestLog.scenario("scenario1")
            TestLog.case(1)
            assertThat(TestLog.lastScenarioLog?.message).isEqualTo("scenario1")
            assertThat(TestLog.lastCaseLog?.message).isEqualTo("(1)")
            // Act
            TestLog.manual("test manually")
            // Assert
            assertScenarioAndCase(LogType.MANUAL, "test manually")
        }

        // OK can be overwritten by MANUAL(Inconclusive)
        TestMode.runAsExpectationBlock {
            // Arrange
            TestLog.lastScenarioLog!!.result = LogType.NONE
            TestLog.lastCaseLog!!.result = LogType.NONE
            TestLog.ok("okay")
            assertScenarioAndCase(LogType.OK, "okay")
            // Act
            TestLog.manual("test manually")
            // Assert
            assertScenarioAndCase(LogType.MANUAL, "test manually")
        }

        // SKIP(Inconclusive) can not be overwritten by MANUAL(Inconclusive)
        run {
            // Arrange
            TestLog.lastScenarioLog!!.result = LogType.NONE
            TestLog.lastCaseLog!!.result = LogType.NONE
            TestLog.skip("skipped")
            assertScenarioAndCase(LogType.SKIP, "skipped")
            // Act
            TestLog.manual("test manually")
            // Assert
            assertScenarioAndCase(LogType.SKIP, "skipped")
        }

        // MANUAL(Inconclusive) can not be overwritten by MANUAL
        run {
            // Arrange
            TestLog.lastScenarioLog!!.result = LogType.NONE
            TestLog.lastCaseLog!!.result = LogType.NONE
            TestLog.manual("test manually")
            assertScenarioAndCase(LogType.MANUAL, "test manually")
            // Act
            TestLog.manual("test manually 2")
            // Assert
            assertScenarioAndCase(LogType.MANUAL, "test manually")
        }

        // NOTIMPL(Inconclusive) can not be overwritten by MANUAL(Inconclusive)
        run {
            // Arrange
            TestLog.lastScenarioLog!!.result = LogType.NONE
            TestLog.lastCaseLog!!.result = LogType.NONE
            TestLog.notImpl(NotImplementedError("not implemented"))
            assertScenarioAndCase(LogType.NOTIMPL, "not implemented")
            // Act
            TestLog.manual("test manually")
            // Assert
            assertScenarioAndCase(LogType.NOTIMPL, "not implemented")
        }

        // KNOWNISSUE(Inconclusive) can not be overwritten by MANUAL(Inconclusive)
        run {
            // Arrange
            TestLog.lastScenarioLog!!.result = LogType.NONE
            TestLog.lastCaseLog!!.result = LogType.NONE
            TestLog.knownIssue(message = "known issue", ticketUrl = "http://example.com/issues/123")
            assertScenarioAndCase(LogType.KNOWNISSUE, "known issue")
            // Act
            TestLog.manual("test manually")
            // Assert
            assertScenarioAndCase(LogType.KNOWNISSUE, "known issue")
        }

        // NG(Fail) can not be overwritten by MANUAL(Inconclusive)
        run {
            // Arrange
            TestLog.lastScenarioLog!!.result = LogType.NONE
            TestLog.lastCaseLog!!.result = LogType.NONE
            TestLog.ng(TestNGException("NG"))
            assertScenarioAndCase(LogType.NG, "NG")
            // Act
            TestLog.manual("test manually")
            // Assert
            assertScenarioAndCase(LogType.NG, "NG")
        }

        // ERROR(Fail) can not be overwritten by MANUAL(Inconclusive)
        run {
            // Arrange
            TestLog.lastScenarioLog!!.result = LogType.NONE
            TestLog.lastCaseLog!!.result = LogType.NONE
            TestLog.error(TestDriverException("ERROR"))
            assertScenarioAndCase(LogType.ERROR, "ERROR")
            // Act
            TestLog.manual("test manually")
            // Assert
            assertScenarioAndCase(LogType.ERROR, "ERROR")
        }

    }

    @Test
    fun result_NOTIMPL() {

        // NOTIMPL(Inconclusive)
        run {
            // Arrange
            TestLog.clear()
            TestLog.scenario("scenario1")
            TestLog.case(1)
            assertThat(TestLog.lastScenarioLog?.message).isEqualTo("scenario1")
            assertThat(TestLog.lastCaseLog?.message).isEqualTo("(1)")
            // Act
            TestLog.notImpl(NotImplementedError("not implemented"))
            // Assert
            assertScenarioAndCase(LogType.NOTIMPL, "not implemented")
        }

        // OK can be overwritten by NOTIMPL(Inconclusive)
        TestMode.runAsExpectationBlock {
            // Arrange
            TestLog.lastScenarioLog!!.result = LogType.NONE
            TestLog.lastCaseLog!!.result = LogType.NONE
            TestLog.ok("okay")
            assertScenarioAndCase(LogType.OK, "okay")
            // Act
            TestLog.notImpl(NotImplementedError("not implemented"))
            // Assert
            assertScenarioAndCase(LogType.NOTIMPL, "not implemented")
        }

        // SKIP(Inconclusive) can not be overwritten by NOTIMPL(Inconclusive)
        run {
            // Arrange
            TestLog.lastScenarioLog!!.result = LogType.NONE
            TestLog.lastCaseLog!!.result = LogType.NONE
            TestLog.skip(CaseSkipException("skip"))
            assertScenarioAndCase(LogType.SKIP, "skip")
            // Act
            TestLog.notImpl(NotImplementedError("not implemented"))
            // Assert
            assertScenarioAndCase(LogType.SKIP, "skip")
        }

        // MANUAL(Inconclusive) can not be overwritten by NOTIMPL(Inconclusive)
        run {
            // Arrange
            TestLog.lastScenarioLog!!.result = LogType.NONE
            TestLog.lastCaseLog!!.result = LogType.NONE
            TestLog.manual("test manually")
            assertScenarioAndCase(LogType.MANUAL, "test manually")
            // Act
            TestLog.notImpl(NotImplementedError("not implemented"))
            // Assert
            assertScenarioAndCase(LogType.MANUAL, "test manually")
        }

        // NOTIMPL(Inconclusive) can not be overwritten by NOTIMPL(Inconclusive)
        run {
            // Arrange
            TestLog.lastScenarioLog!!.result = LogType.NONE
            TestLog.lastCaseLog!!.result = LogType.NONE
            TestLog.notImpl(NotImplementedError("not implemented"))
            assertScenarioAndCase(LogType.NOTIMPL, "not implemented")
            // Act
            TestLog.notImpl(NotImplementedError("not implemented 2"))
            // Assert
            assertScenarioAndCase(LogType.NOTIMPL, "not implemented")
        }

        // KNOWNISSUE(Inconclusive) can not be overwritten by NOTIMPL(Inconclusive)
        run {
            // Arrange
            TestLog.lastScenarioLog!!.result = LogType.NONE
            TestLog.lastCaseLog!!.result = LogType.NONE
            TestLog.knownIssue(message = "known issue", ticketUrl = "http://example.com/issues/123")
            assertScenarioAndCase(LogType.KNOWNISSUE, "known issue")
            // Act
            TestLog.notImpl(NotImplementedError("not implemented 2"))
            // Assert
            assertScenarioAndCase(LogType.KNOWNISSUE, "known issue")
        }

        // NG(Fail) can not be overwritten by NOTIMPL(Inconclusive)
        run {
            // Arrange
            TestLog.lastScenarioLog!!.result = LogType.NONE
            TestLog.lastCaseLog!!.result = LogType.NONE
            TestLog.ng(TestNGException("NG"))
            assertScenarioAndCase(LogType.NG, "NG")
            // Act
            TestLog.notImpl(NotImplementedError("not implemented"))
            // Assert
            assertScenarioAndCase(LogType.NG, "NG")
        }

        // ERROR(Fail) can not be overwritten by NOTIMPL(Inconclusive)
        run {
            // Arrange
            TestLog.lastScenarioLog!!.result = LogType.NONE
            TestLog.lastCaseLog!!.result = LogType.NONE
            TestLog.error(TestDriverException("ERROR"))
            assertScenarioAndCase(LogType.ERROR, "ERROR")
            // Act
            TestLog.notImpl(NotImplementedError("not implemented"))
            // Assert
            assertScenarioAndCase(LogType.ERROR, "ERROR")
        }

    }

    @Test
    fun result_NG() {

        // NG(Fali)
        run {
            // Arrange
            TestLog.clear()
            TestLog.scenario("scenario1")
            TestLog.case(1)
            assertThat(TestLog.lastScenarioLog?.message).isEqualTo("scenario1")
            assertThat(TestLog.lastCaseLog?.message).isEqualTo("(1)")
            // Act
            TestLog.ng(TestNGException("no good"))
            // Assert
            assertScenarioAndCase(LogType.NG, "no good")
        }

        // OK can be overwritten by NG(Fali)
        TestMode.runAsExpectationBlock {
            // Arrange
            TestLog.lastScenarioLog!!.result = LogType.NONE
            TestLog.lastCaseLog!!.result = LogType.NONE
            TestLog.ok("okay")
            assertScenarioAndCase(LogType.OK, "okay")
            // Act
            TestLog.ng(TestNGException("no good"))
            // Assert
            assertScenarioAndCase(LogType.NG, "no good")
        }

        // SKIP(Inconclusive) can be overwritten by NG(Fail)
        run {
            // Arrange
            TestLog.lastScenarioLog!!.result = LogType.NONE
            TestLog.lastCaseLog!!.result = LogType.NONE
            TestLog.skip("skipped")
            assertScenarioAndCase(LogType.SKIP, "skipped")
            // Act
            TestLog.ng(TestNGException("no good"))
            // Assert
            assertScenarioAndCase(LogType.NG, "no good")
        }

        // MANUAL(Inconclusive) can be overwritten by NG(Fail)
        run {
            // Arrange
            TestLog.lastScenarioLog!!.result = LogType.NONE
            TestLog.lastCaseLog!!.result = LogType.NONE
            TestLog.manual("test manually")
            assertScenarioAndCase(LogType.MANUAL, "test manually")
            // Act
            TestLog.ng(TestNGException("no good"))
            // Assert
            assertScenarioAndCase(LogType.NG, "no good")
        }

        // NOTIMPL(Inconclusive) can be overwritten by NG(Fail)
        run {
            // Arrange
            TestLog.lastScenarioLog!!.result = LogType.NONE
            TestLog.lastCaseLog!!.result = LogType.NONE
            TestLog.notImpl(NotImplementedError("not implemented"))
            assertScenarioAndCase(LogType.NOTIMPL, "not implemented")
            // Act
            TestLog.ng(TestNGException("no good"))
            // Assert
            assertScenarioAndCase(LogType.NG, "no good")
        }

        // KNOWNISSUE(Inconclusive) can be overwritten by NG(Fail)
        run {
            // Arrange
            TestLog.lastScenarioLog!!.result = LogType.NONE
            TestLog.lastCaseLog!!.result = LogType.NONE
            TestLog.knownIssue(message = "known issue", ticketUrl = "http://example.com/issues/123")
            assertScenarioAndCase(LogType.KNOWNISSUE, "known issue")
            // Act
            TestLog.ng(TestNGException("no good"))
            // Assert
            assertScenarioAndCase(LogType.NG, "no good")
        }

        // NG(Fail) can not be overwritten by NG
        run {
            // Arrange
            TestLog.lastScenarioLog!!.result = LogType.NONE
            TestLog.lastCaseLog!!.result = LogType.NONE
            TestLog.ng(TestNGException("no good"))
            assertScenarioAndCase(LogType.NG, "no good")
            // Act
            TestLog.ng(TestNGException("no good 2"))
            // Assert
            assertScenarioAndCase(LogType.NG, "no good")
        }

        // ERROR(Fail) can not be overwritten by NG(Fail)
        run {
            // Arrange
            TestLog.lastScenarioLog!!.result = LogType.NONE
            TestLog.lastCaseLog!!.result = LogType.NONE
            TestLog.error(TestDriverException("ERROR"))
            assertScenarioAndCase(LogType.ERROR, "ERROR")
            // Act
            TestLog.ng(TestNGException("no good"))
            // Assert
            assertScenarioAndCase(LogType.ERROR, "ERROR")
        }

    }

    @Test
    fun result_ERROR() {

        // ERROR(Fali)
        run {
            // Arrange
            TestLog.clear()
            TestLog.scenario("scenario1")
            TestLog.case(1)
            assertThat(TestLog.lastScenarioLog?.message).isEqualTo("scenario1")
            assertThat(TestLog.lastCaseLog?.message).isEqualTo("(1)")
            // Act
            TestLog.error(TestConfigException("ERROR"))
            // Assert
            assertScenarioAndCase(LogType.ERROR, "ERROR")
        }

        // OK can be overwritten by ERROR(Fali)
        TestMode.runAsExpectationBlock {
            // Arrange
            TestLog.lastScenarioLog!!.result = LogType.NONE
            TestLog.lastCaseLog!!.result = LogType.NONE
            TestLog.ok("okay")
            assertScenarioAndCase(LogType.OK, "okay")
            // Act
            TestLog.error(TestConfigException("ERROR"))
            // Assert
            assertScenarioAndCase(LogType.ERROR, "ERROR")
        }

        // SKIP(Inconclusive) can be overwritten by NG(Fail)
        run {
            // Arrange
            TestLog.lastScenarioLog!!.result = LogType.NONE
            TestLog.lastCaseLog!!.result = LogType.NONE
            TestLog.skip(CaseSkipException("skip"))
            assertScenarioAndCase(LogType.SKIP, "skip")
            // Act
            TestLog.ng(TestNGException("no good"))
            // Assert
            assertScenarioAndCase(LogType.NG, "no good")
        }

        // MANUAL(Inconclusive) can be overwritten by NG(Fail)
        run {
            // Arrange
            TestLog.lastScenarioLog!!.result = LogType.NONE
            TestLog.lastCaseLog!!.result = LogType.NONE
            TestLog.manual("test manually")
            assertScenarioAndCase(LogType.MANUAL, "test manually")
            // Act
            TestLog.ng(TestNGException("no good"))
            // Assert
            assertScenarioAndCase(LogType.NG, "no good")
        }

        // NOTIMPL(Inconclusive) can be overwritten by NG(Fail)
        run {
            // Arrange
            TestLog.lastScenarioLog!!.result = LogType.NONE
            TestLog.lastCaseLog!!.result = LogType.NONE
            TestLog.notImpl(NotImplementedError("not implemented"))
            assertScenarioAndCase(LogType.NOTIMPL, "not implemented")
            // Act
            TestLog.ng(TestNGException("no good"))
            // Assert
            assertScenarioAndCase(LogType.NG, "no good")
        }

        // KNOWNISSUE(Inconclusive) can be overwritten by NG(Fail)
        run {
            // Arrange
            TestLog.lastScenarioLog!!.result = LogType.NONE
            TestLog.lastCaseLog!!.result = LogType.NONE
            TestLog.knownIssue(message = "known issue", ticketUrl = "http://example.com/issues/123")
            assertScenarioAndCase(LogType.KNOWNISSUE, "known issue")
            // Act
            TestLog.ng(TestNGException("no good"))
            // Assert
            assertScenarioAndCase(LogType.NG, "no good")
        }

        // NG(Fail) can not be overwritten by NG
        run {
            // Arrange
            TestLog.lastScenarioLog!!.result = LogType.NONE
            TestLog.lastCaseLog!!.result = LogType.NONE
            TestLog.ng(TestNGException("no good"))
            assertScenarioAndCase(LogType.NG, "no good")
            // Act
            TestLog.ng(TestNGException("no good 2"))
            // Assert
            assertScenarioAndCase(LogType.NG, "no good")
        }

        // ERROR(Fail) can not be overwritten by NG(Fail)
        run {
            // Arrange
            TestLog.lastScenarioLog!!.result = LogType.NONE
            TestLog.lastCaseLog!!.result = LogType.NONE
            TestLog.error(TestDriverException("ERROR"))
            assertScenarioAndCase(LogType.ERROR, "ERROR")
            // Act
            TestLog.ng(TestNGException("no good"))
            // Assert
            assertScenarioAndCase(LogType.ERROR, "ERROR")
        }

    }
}