package shirates.core.uitest.ios.driver.commandextension

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.exception.TestDriverException
import shirates.core.testcode.UITest
import shirates.core.testcode.Want
import shirates.core.utility.time.StopWatch

@Want
@Testrun("unitTestConfig/ios/iOSSettings/testrun.properties")
class TestDriveScreenExtensionTest : UITest() {

    @Test
    fun screenName() {

        // Arrange
        it.restartApp()
        // Act, Assert
        assertThat(it.screenName).isEqualTo("[iOS Settings Top Screen]")
    }

    @Test
    fun isScreen() {

        // Arrange
        it.restartApp()
        // Assert
        assertThat(it.isScreen("[iOS Settings Top Screen]")).isTrue()


        // Assert
        assertThat(it.isScreen("[General Screen]")).isFalse()
    }

    @Test
    fun isScreenOf() {

        // Arrange
        it.restartApp()
        // Assert
        assertThat(it.isScreenOf("[iOS Settings Top Screen]", "[About Screen]")).isTrue()


        // Assert
        assertThat(it.isScreenOf("[About Screen]", "[Developer Screen]")).isFalse()
    }

    @Test
    fun waitScreenOf() {

        // Arramge
        it.restartApp()
        // Assert
        it.waitScreenOf("[iOS Settings Top Screen]", "[About Screen]", waitSeconds = 1.1)


        // Assert
        assertThatThrownBy {
            it.waitScreenOf("[About Screen]", "[Developer Screen]", waitSeconds = 2.0)
        }.isInstanceOf(TestDriverException::class.java)
    }

    @Test
    fun waitScreen() {

        // Arrange
        it.restartApp()
        // Assert
        it.waitScreen("[iOS Settings Top Screen]", waitSeconds = 1.1)

        run {
            // Arrange
            val sw = StopWatch()
            // Assert
            assertThatThrownBy {
                sw.start()
                it.waitScreen("[About Screen]", waitSeconds = 1.0)
            }.isInstanceOf(TestDriverException::class.java)
            val sec = sw.elapsedSeconds
            println("sec=$sec")

            assertThat(sec >= 1).isTrue()
        }

        run {
            // Arrange
            val sw = StopWatch()
            // Assert
            assertThatThrownBy {
                sw.start()
                it.waitScreen("[About Screen]", waitSeconds = 5.0)
            }.isInstanceOf(TestDriverException::class.java)
            val sec = sw.elapsedSeconds
            println("sec=$sec")

            assertThat(sec >= 5).isTrue()
        }
    }

}