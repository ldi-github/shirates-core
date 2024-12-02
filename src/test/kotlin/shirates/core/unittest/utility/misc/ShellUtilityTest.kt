package shirates.core.unittest.utility.misc

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.testcode.UnitTest
import shirates.core.utility.misc.ShellUtility

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

//    @Test
//    fun executeCommandAsync2() {
//
//        // Arrange
//        val avdName = setupEmulator()
//        val deviceInfo = AndroidDeviceUtility.waitEmulatorStatusByAvdName(avdName = avdName)
//        val udid = deviceInfo.udid
//        // Act
//        val r = ShellUtility.executeCommandAsync(
//            "adb", "-s", udid, "shell", "pm", "dump", "com.android.settings"
//        )
//        // Assert
//        assertThat(r.hasCompleted).isFalse()
//
//        assertThat(r.executor).isNotNull
//        assertThat(r.args.count()).isEqualTo(7)
//        assertThat(r.outputStream).isNotNull
//        assertThat(r.error).isNull()
//        assertThat(r.resultHandler).isNotNull
//        assertThat(r.command).isEqualTo("adb -s $udid shell pm dump com.android.settings")
//        assertThat(r.hasError).isFalse()
//        assertThat(r.isAsync).isTrue()
//        assertThat(r.resultString).contains("")
//
//        // Act
//        val s = r.waitForResultString()
//        // Assert
//        assertThat(r.error).isNull()
//        assertThat(r.hasError).isFalse()
//        assertThat(r.hasCompleted).isTrue()
//        assertThat(r.resultHandler!!.exitValue).isEqualTo(0)
//        assertThat(s).contains("DUMP OF SERVICE package:")
//
//        val port = udid.split("-").last().toInt()
//        val pid = ProcessUtility.getPid(port)
//        if (pid != null) {
//            ProcessUtility.terminateProcess(pid = pid)
//        }
//    }
//
//    private fun setupEmulator(): String {
//        val avdNames = ShellUtility.executeCommand("emulator", "-list-avds").resultLines
//        if (avdNames.isEmpty()) {
//            throw IllegalStateException("No AVD found.")
//        }
//        val avdName = avdNames.first()
//
//        val devices = ShellUtility.executeCommand("adb", "devices", "-l").resultLines
//        if (devices.contains("emulator-").not()) {
//            /**
//             * Start emulator
//             */
//            ShellUtility.executeCommandAsync("emulator", "-avd", avdName)
//            WaitUtility.doUntilTrue {
//                val r = ShellUtility.executeCommand("adb", "devices").resultString
//                r.contains("emulator-")
//            }
//        }
//        return avdName
//    }
}