package shirates.tool

import org.junit.jupiter.api.Test
import shirates.core.configuration.PropertiesManager
import shirates.core.utility.misc.DeviceListUtility

class DeviceListUtilityTest {

    @Test
    fun getConnectedDeviceList() {

        PropertiesManager.setup(testrunFile = "testConfig/android/androidSettings/testrun.properties")

        val list = DeviceListUtility.getConnectedDeviceList()
        for (deviceInfo in list) {
            println("Android $deviceInfo")
        }


    }
}