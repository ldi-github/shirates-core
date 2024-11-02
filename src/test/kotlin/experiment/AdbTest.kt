package experiment

import org.junit.jupiter.api.Test
import shirates.core.utility.misc.ShellUtility

class AdbTest {

    @Test
    fun ps() {

        val result = ShellUtility.executeCommand("adb", "-s", "emulator-5554", "shell", "ps")
        println(result.resultString)
    }

    @Test
    fun adbProcess() {

        val r = ShellUtility.executeCommand("ps", "-ax").resultLines
            .filter { it.contains("adb -L") }
            .map { it.split(" ")[0] }
        println(r)
        for (pid in r) {
            ShellUtility.executeCommand("kill", "-9", pid)
        }

        val r2 = ShellUtility.executeCommand("ps", "-ax").resultLines
            .filter { it.contains("adb -L") }
            .map { it.split(" ")[0] }
        if (r2.isEmpty()) {
            println("no adb process")
        } else {
            println(r2)
        }

    }
}