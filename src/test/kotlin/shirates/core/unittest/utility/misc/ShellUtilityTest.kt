package shirates.core.unittest.utility.misc

import okio.FileNotFoundException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import shirates.core.driver.TestMode
import shirates.core.testcode.UnitTest
import shirates.core.utility.misc.ShellUtility
import shirates.core.utility.toPath
import kotlin.io.path.name

class ShellUtilityTest : UnitTest() {

    @Test
    fun executeCommand() {

        run {
            // Act
            val r = ShellUtility.executeCommand("adb", "devices")
            // Assert
            assertThat(r.hasCompleted).isTrue()

            assertThat(r.executor).isNotNull
            assertThat(r.args.count()).isEqualTo(2)
            assertThat(r.outputStream).isNotNull
            assertThat(r.error).isNull()
            assertThat(r.resultHandler).isNull()
            assertThat(r.command).isEqualTo("adb devices")
            assertThat(r.hasError).isFalse()
            assertThat(r.isAsync).isFalse()
            assertThat(r.resultString).contains("List of devices attached")
        }
        run {
            // Act
            val r = ShellUtility.executeCommand("adb", "devices", log = true)
            r.waitFor()
            // Assert
            assertThat(r.resultString).contains("List of devices attached")
        }
        run {
            // Act
            val r = ShellUtility.executeCommand("adb", "devices", log = true)
            val s = r.waitForResultString()
            // Assert
            assertThat(s).contains("List of devices attached")
        }
    }

    @Test
    fun executeCommandAsync() {

        run {
            // Act
            val r = ShellUtility.executeCommandAsync("adb", "devices", "-l", log = true)
            // Assert
            assertThat(r.hasCompleted).isFalse()

            assertThat(r.executor).isNotNull
            assertThat(r.args.count()).isEqualTo(3)
            assertThat(r.outputStream).isNotNull
            assertThat(r.error).isNull()
            assertThat(r.resultHandler).isNotNull
            assertThat(r.command).isEqualTo("adb devices -l")
            assertThat(r.hasError).isFalse()
            assertThat(r.isAsync).isTrue()
            assertThat(r.resultString).contains("")

            // Act
            val s = r.waitForResultString()
            // Assert
            assertThat(r.error).isNull()
            assertThat(r.hasError).isFalse()
            assertThat(r.hasCompleted).isTrue()
            assertThat(r.resultHandler!!.exitValue).isEqualTo(0)
            assertThat(s).contains("List of devices attached")
        }
        run {
            // Act
            val r = ShellUtility.executeCommandAsync("adb", "devices", log = true)
            // Assert
            assertThat(r.waitForResultString()).contains("List of devices attached")
        }

    }

    @Test
    fun which() {

        run {
            // Act
            val r = ShellUtility.which("emulator")
            // Assert
            if (TestMode.isRunningOnWindows) {
                assertThat(r.toPath().name).isEqualTo("emulator.exe")
            } else {
                assertThat(r.toPath().name).isEqualTo("emulator")
            }
        }
        run {
            // Act
            val r = ShellUtility.which("emulatorX")
            // Assert
            assertThat(r).isEqualTo("")
        }
    }

    @Test
    fun validateCommand() {

        run {
            ShellUtility.validateCommand("emulator")
        }
        assertThatThrownBy {
            ShellUtility.validateCommand("noExistCommand")
        }.isInstanceOf(FileNotFoundException::class.java)
            .hasMessage("`noExistCommand` not found. Install the command, and set environment variables and PATH correctly. Then restart IntelliJ IDEA to take effect.")
    }
}