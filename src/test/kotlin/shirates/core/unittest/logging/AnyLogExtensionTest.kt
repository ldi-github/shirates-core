package shirates.core.unittest.logging

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.configuration.Selector
import shirates.core.logging.*
import shirates.core.testcode.UnitTest

class AnyLogExtensionTest : UnitTest() {

    @Test
    fun printInfo() {

        "String".printInfo()
        assertThat(TestLog.lastTestLog?.message).isEqualTo("String")
        assertThat(TestLog.lastTestLog?.logType).isEqualTo(LogType.INFO)

        123.printInfo()
        assertThat(TestLog.lastTestLog?.message).isEqualTo("123")
        assertThat(TestLog.lastTestLog?.logType).isEqualTo(LogType.INFO)

        Selector("text=text1&&access=access1").printInfo()
        assertThat(TestLog.lastTestLog?.message).isEqualTo("<text1&&@access1>")
        assertThat(TestLog.lastTestLog?.logType).isEqualTo(LogType.INFO)
    }

    @Test
    fun printWarn() {

        "String".printWarn()
        assertThat(TestLog.lastTestLog?.message).isEqualTo("String")
        assertThat(TestLog.lastTestLog?.logType).isEqualTo(LogType.WARN)

        123.printWarn()
        assertThat(TestLog.lastTestLog?.message).isEqualTo("123")
        assertThat(TestLog.lastTestLog?.logType).isEqualTo(LogType.WARN)

        Selector("text=text1&&access=access1").printWarn()
        assertThat(TestLog.lastTestLog?.message).isEqualTo("<text1&&@access1>")
        assertThat(TestLog.lastTestLog?.logType).isEqualTo(LogType.WARN)
    }

    @Test
    fun printLog() {

        "String".printLog(LogType.NONE)
        assertThat(TestLog.lastTestLog?.message).isEqualTo("String")
        assertThat(TestLog.lastTestLog?.logType).isEqualTo(LogType.NONE)

        123.printLog(LogType.INFO)
        assertThat(TestLog.lastTestLog?.message).isEqualTo("123")
        assertThat(TestLog.lastTestLog?.logType).isEqualTo(LogType.INFO)

        Selector("text=text1&&access=access1").printLog(LogType.COMMENT)
        assertThat(TestLog.lastTestLog?.message).isEqualTo("<text1&&@access1>")
        assertThat(TestLog.lastTestLog?.logType).isEqualTo(LogType.COMMENT)
    }
}