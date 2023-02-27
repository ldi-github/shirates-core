package shirates.core.unittest.utility.misc

import org.junit.jupiter.api.Test
import shirates.core.testcode.UnitTest
import shirates.core.utility.ios.IosDeviceUtility

class ProcessUtilityTest : UnitTest() {

    @Test
    fun getMacProcessList() {

        val devices = IosDeviceUtility.getBootedSimulatorDeviceList()
        for (device in devices) {
            IosDeviceUtility.terminateSpringBoardByUdid(udid = device.udid)
        }
    }
}