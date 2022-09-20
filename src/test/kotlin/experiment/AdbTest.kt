package experiment

import org.junit.jupiter.api.Test
import shirates.core.utility.misc.ShellUtility

class AdbTest {

    @Test
    fun ps() {

        val result = ShellUtility.executeCommand("adb", "-s", "emulator-5554", "shell", "ps")
        println(result.resultString)
    }

}