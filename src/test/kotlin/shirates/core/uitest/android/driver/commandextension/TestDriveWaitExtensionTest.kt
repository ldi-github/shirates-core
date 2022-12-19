package shirates.core.uitest.android.driver.commandextension

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.describe
import shirates.core.driver.commandextension.wait
import shirates.core.driver.testContext
import shirates.core.testcode.UITest
import shirates.core.testcode.Want
import java.util.*


@Want
@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
class TestDriveWaitExtensionTest : UITest() {

    @Test
    fun waitTest() {

        var t1 = Date()
        var t2 = Date()
        var interval: Long
        var expected: Long
        val assertWaited = { waitSeconds: Double ->

            interval = t2.time.minus(t1.time)
            expected = (waitSeconds * 1000).toLong()
            assertThat(interval).isGreaterThanOrEqualTo(expected)
            OK("expected interval=$interval >= $expected")
        }

        run {
            // Arrange
            describe("waiting default=${testContext.shortWaitSeconds}")
            t1 = Date()
            // Act
            it.wait(testContext.shortWaitSeconds)
            t2 = Date()
            // Assert
            assertWaited(testContext.shortWaitSeconds)
        }

        run {
            // Arrange
            describe("waiting 0.1sec")
            t1 = Date()
            // Act
            it.wait(0.1)
            t2 = Date()
            // Assert
            assertWaited(0.1)
        }

        run {
            // Arrange
            describe("waiting 1.2sec")
            t1 = Date()
            // Act
            it.wait(1.2)
            t2 = Date()
            // Assert
            assertWaited(1.2)
        }

        run {
            // Arrange
            describe("waiting 1.5sec")
            t1 = Date()
            // Act
            it.wait(2)   // Long
            t2 = Date()
            // Assert
            assertWaited(2.toDouble())
        }

    }
}