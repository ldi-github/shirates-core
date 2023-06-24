package shirates.core.unittest.logging

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import shirates.core.logging.Measure
import shirates.core.logging.TestLog
import shirates.core.testcode.UnitTest

class MeasureTest : UnitTest() {

    override fun beforeAll(context: ExtensionContext?) {
        TestLog.enableTrace = true
    }

    @Test
    fun noMessage() {

        run {
            // Act
            val ms = Measure()
            // Assert
            assertThat(ms.message).isEqualTo("")
            assertThat(ms.startLogLine.message).isEqualTo("[TraceTest.noMessage] -start(${ms.startLogLine.lineNumber})")
            // Act
            ms.end()
            // Assert
            val s = ms.startLogLine
            val e = ms.endLogLine
            assertThat(e.message).startsWith("[TraceTest.noMessage] -end (${s.lineNumber}->${e.lineNumber}: ${ms.elapsedMillis}[ms])")
        }
        run {
            // Act
            val ms = Measure.start()
            // Assert
            assertThat(ms.message).isEqualTo("")
            assertThat(ms.startLogLine.message).isEqualTo("[TraceTest.noMessage] -start(${ms.startLogLine.lineNumber})")
            // Act
            ms.end()
            // Assert
            val s = ms.startLogLine
            val e = ms.endLogLine
            assertThat(e.message).startsWith("[TraceTest.noMessage] -end (${s.lineNumber}->${e.lineNumber}: ${ms.elapsedMillis}[ms])")
        }
    }

    @Test
    fun withMessage() {

        run {
            // Act
            val ms = Measure("message1")
            // Assert
            assertThat(ms.message).isEqualTo("message1")
            assertThat(ms.startLogLine.message).isEqualTo("[TraceTest.withMessage] message1 -start(${ms.startLogLine.lineNumber})")
            // Act
            ms.end()
            // Assert
            val s = ms.startLogLine
            val e = ms.endLogLine
            assertThat(e.message).startsWith("[TraceTest.withMessage] message1 -end (${s.lineNumber}->${e.lineNumber}: ${ms.elapsedMillis}[ms])")
        }
        run {
            // Act
            val ms = Measure.start("message1")
            // Assert
            assertThat(ms.message).isEqualTo("message1")
            assertThat(ms.startLogLine.message).isEqualTo("[TraceTest.withMessage] message1 -start(${ms.startLogLine.lineNumber})")
            // Act
            ms.end()
            // Assert
            val s = ms.startLogLine
            val e = ms.endLogLine
            assertThat(ms.endLogLine.message).startsWith("[TraceTest.withMessage] message1 -end (${s.lineNumber}->${e.lineNumber}: ${ms.elapsedMillis}[ms])")
        }
    }

}