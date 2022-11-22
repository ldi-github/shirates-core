package shirates.core.hand.unittest.utility

import org.junit.jupiter.api.Test
import shirates.core.utility.ios.IosDeviceUtility

class IosDeviceUtilityTest {

    @Test
    fun getBootedIosDeviceList() {

        val list = IosDeviceUtility.getBootedIosDeviceList()
        list.forEach {
            println(it)
        }
    }

    @Test
    fun getIosDeviceList() {

        val list = IosDeviceUtility.getIosDeviceList()
        list.forEach {
            println(it)
        }

    }

    @Test
    fun getRealDeviceList() {

        val list = IosDeviceUtility.getRealDeviceList()
        list.forEach {
            println(it)
        }
    }

    @Test
    fun getSimulatorDeviceList() {

        val list = IosDeviceUtility.getSimulatorDeviceList()
        list.forEach {
            println(it)
        }
    }

}