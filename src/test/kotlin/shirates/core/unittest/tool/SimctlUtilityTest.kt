package shirates.core.unittest.tool

import org.junit.jupiter.api.Test
import shirates.core.testcode.UnitTest
import shirates.core.utility.tool.SimctlUtility

class SimctlUtilityTest : UnitTest() {

    @Test
    fun init() {

        // Act
        val list = SimctlUtility.getBootedIosDeviceList(log = true)
        println("Printing simctl list device")
        for (info in list) {
            println(info)
        }
    }

}