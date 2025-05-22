package shirates.core.vision.uitest.android.driver.commandextension

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.logging.TestLog
import shirates.core.testcode.CodeExecutionContext
import shirates.core.vision.driver.commandextension.detect
import shirates.core.vision.driver.commandextension.withImageFilter
import shirates.core.vision.testcode.VisionTest

@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
class VisionDriveDetectExtensionTest7 : VisionTest() {

    @Test
    @Order(10)
    fun withImageFilter() {

        scenario {
            case(1) {
                condition {
                    assertThat(CodeExecutionContext.visionImageFilterContext.hasFilter).isFalse
                }.action {
                    withImageFilter.enhanceFaintAreas {
                        assertThat(CodeExecutionContext.visionImageFilterContext.hasFilter).isTrue
                        assertThat(TestLog.lines.any { it.message.contains("filtered.png") }).isFalse
                        it.detect("Network & internet")
                        assertThat(TestLog.lines.count { it.message.contains("filtered.png") }).isEqualTo(1)
                    }
                    assertThat(CodeExecutionContext.visionImageFilterContext.hasFilter).isFalse
                }.expectation {

                }
            }
        }
    }

    @Test
    @Order(20)
    fun autoImageFilter() {

        assertThat(TestLog.lines.count { it.message.contains("filtered.png") }).isEqualTo(0)
        it.detect("Network & internet")
        assertThat(TestLog.lines.count { it.message.contains("filtered.png") }).isEqualTo(0)

        it.detect("not exist", throwsException = false)
        assertThat(TestLog.lines.count { it.message.contains("filtered.png") }).isEqualTo(0)

        it.detect("not exist", throwsException = false, waitSeconds = 0.0, autoImageFilter = true)
        assertThat(TestLog.lines.count { it.message.contains("filtered.png") }).isEqualTo(1)

        it.detect("not exist", throwsException = false, waitSeconds = 0.0, autoImageFilter = true)
        assertThat(TestLog.lines.count { it.message.contains("filtered.png") }).isEqualTo(2)

        it.detect("not exist", throwsException = false, waitSeconds = 0.0, autoImageFilter = false)
        assertThat(TestLog.lines.count { it.message.contains("filtered.png") }).isEqualTo(2)

        it.detect("not exist", throwsException = false, waitSeconds = 0.0)
        assertThat(TestLog.lines.count { it.message.contains("filtered.png") }).isEqualTo(2)

        it.detect("not exist", throwsException = false, waitSeconds = 0.0, autoImageFilter = true)
        assertThat(TestLog.lines.count { it.message.contains("filtered.png") }).isEqualTo(3)
    }

}