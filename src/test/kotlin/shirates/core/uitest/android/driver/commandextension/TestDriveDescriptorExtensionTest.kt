package shirates.core.uitest.android.driver.commandextension

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.logging.LogType
import shirates.core.logging.TestLog
import shirates.core.testcode.UITest

@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
class TestDriveDescriptorExtensionTest : UITest() {

    @Test
    fun captionTest() {

        // Arrange
        TestLog.clear()
        assertThat(TestLog.lines.count() { it.logType == LogType.CAPTION }).isEqualTo(0)
        // Act
        caption("caption1")
        run {
            val lines = TestLog.lines.filter { it.logType == LogType.CAPTION }
            // Assert
            assertThat(lines.count()).isEqualTo(1)
            assertThat(lines[0].message).isEqualTo("(caption1)")
            assertThat(lines[0].logType).isEqualTo(LogType.CAPTION)
            assertThat(lines[0].subject).isEqualTo("caption1")
            assertThat(lines[0].result).isEqualTo(LogType.NONE)
        }


        // Act
        caption("caption2")
        run {
            val lines = TestLog.lines.filter { it.logType == LogType.CAPTION }
            // Assert
            assertThat(lines.count()).isEqualTo(2)
            assertThat(lines[1].message).isEqualTo("(caption2)")
            assertThat(lines[1].logType).isEqualTo(LogType.CAPTION)
            assertThat(lines[1].subject).isEqualTo("caption2")
            assertThat(lines[1].result).isEqualTo(LogType.NONE)
        }


        // Act
        caption("caption3")
        run {
            val lines = TestLog.lines.filter { it.logType == LogType.CAPTION }
            // Assert
            assertThat(lines.count()).isEqualTo(3)
            assertThat(lines[2].message).isEqualTo("(caption3)")
            assertThat(lines[2].logType).isEqualTo(LogType.CAPTION)
            assertThat(lines[2].subject).isEqualTo("caption3")
            assertThat(lines[2].result).isEqualTo(LogType.NONE)
        }
    }

    @Test
    fun describeTest() {

        // Arrange
        TestLog.clear()
        assertThat(TestLog.lines.count() { it.logType == LogType.DESCRIBE }).isEqualTo(0)
        // Act
        describe("describe1")
        run {
            val lines = TestLog.lines.filter { it.logType == LogType.DESCRIBE }
            // Assert
            assertThat(lines.count()).isEqualTo(1)
            assertThat(lines[0].message).isEqualTo("describe1")
            assertThat(lines[0].logType).isEqualTo(LogType.DESCRIBE)
            assertThat(lines[0].subject).isEqualTo("describe1")
            assertThat(lines[0].result).isEqualTo(LogType.NONE)
        }


        // Act
        describe("describe2")
        run {
            val lines = TestLog.lines.filter { it.logType == LogType.DESCRIBE }
            // Assert
            assertThat(lines.count()).isEqualTo(2)
            assertThat(lines[1].message).isEqualTo("describe2")
            assertThat(lines[1].logType).isEqualTo(LogType.DESCRIBE)
            assertThat(lines[1].subject).isEqualTo("describe2")
            assertThat(lines[1].result).isEqualTo(LogType.NONE)
        }


        // Act
        describe("describe3")
        run {
            val lines = TestLog.lines.filter { it.logType == LogType.DESCRIBE }
            // Assert
            assertThat(lines.count()).isEqualTo(3)
            assertThat(lines[2].message).isEqualTo("describe3")
            assertThat(lines[2].logType).isEqualTo(LogType.DESCRIBE)
            assertThat(lines[2].subject).isEqualTo("describe3")
            assertThat(lines[2].result).isEqualTo(LogType.NONE)
        }
    }

    @Test
    fun procedureTest() {

        // Arrange
        TestLog.clear()
        assertThat(TestLog.lines.count() { it.scriptCommand == "procedure" }).isEqualTo(0)
        // Act
        procedure("procedure1") {
            caption("caption1")
                .describe("describe1")
                .exist("Network & internet")
                .textIs("Network & internet")
        }
        val lines = TestLog.lines

        // Assert
        val line = lines.last() { it.logType == LogType.PROCEDURE }
        assertThat(line.message).isEqualTo("procedure1")
        assertThat(line.logType).isEqualTo(LogType.PROCEDURE)
        assertThat(line.commandGroup).isEqualTo("procedure")
        assertThat(line.commandLevel).isEqualTo(1)
        assertThat(line.scriptCommand).isEqualTo("procedure")
        assertThat(line.subject).isEqualTo("procedure1")
        assertThat(line.result).isEqualTo(LogType.ok)

        assertThat(lines.count() { it.logType == LogType.CAPTION }).isEqualTo(0)
        assertThat(lines.count() { it.logType == LogType.DESCRIBE }).isEqualTo(0)
        assertThat(lines.count() { it.message == "[Network & internet] exists" }).isEqualTo(0)
        assertThat(lines.count() { it.message == "[Network & internet] is \"Network & internet\"" }).isEqualTo(0)
    }

    @Test
    fun targetTest() {

        // Act
        target("target1")
        val lines = TestLog.lines

        // Assert
        val line = lines.last() { it.logType == LogType.TARGET }
        assertThat(line.message).isEqualTo("target1")
        assertThat(line.logType).isEqualTo(LogType.TARGET)
        assertThat(line.commandGroup).isEqualTo("target")
        assertThat(line.commandLevel).isEqualTo(1)
        assertThat(line.scriptCommand).isEqualTo("target")
        assertThat(line.subject).isEqualTo("target1")
        assertThat(line.arg1).isEqualTo("")
        assertThat(line.arg2).isEqualTo("")
        assertThat(line.result).isEqualTo(LogType.NONE)
    }

    @Test
    fun manualTest() {

        // Act
        manual("message", "arg1", "arg2")
        val lines = TestLog.lines

        // Assert
        val line = lines.last() { it.logType == LogType.MANUAL }
        assertThat(line.message).isEqualTo("message")
        assertThat(line.logType).isEqualTo(LogType.MANUAL)
        assertThat(line.commandGroup).isEqualTo("manual")
        assertThat(line.commandLevel).isEqualTo(1)
        assertThat(line.scriptCommand).isEqualTo("manual")
        assertThat(line.subject).isEqualTo("message")
        assertThat(line.arg1).isEqualTo("arg1")
        assertThat(line.arg2).isEqualTo("arg2")
        assertThat(line.result).isEqualTo(LogType.MANUAL)
    }

    @Test
    fun knownIssue() {

        // Act
        it.knownIssue("message", ticketUrl = "http://example.com")
        val lines = TestLog.lines

        // Assert
        val line = lines.last() { it.logType == LogType.KNOWNISSUE }
        assertThat(line.message).isEqualTo("[KNOWNISSUE]message http://example.com")
        assertThat(line.logType).isEqualTo(LogType.KNOWNISSUE)
        assertThat(line.commandGroup).isEqualTo("")
        assertThat(line.commandLevel).isEqualTo(0)
        assertThat(line.scriptCommand).isEqualTo("knownIssue")
        assertThat(line.subject).isEqualTo("")
        assertThat(line.arg1).isEqualTo("")
        assertThat(line.arg2).isEqualTo("")
        assertThat(line.result).isEqualTo(LogType.KNOWNISSUE)
    }
}