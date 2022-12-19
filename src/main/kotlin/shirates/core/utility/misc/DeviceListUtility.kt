package shirates.core.utility.misc

import shirates.core.configuration.PropertiesManager
import shirates.core.configuration.TestConfigContainer
import shirates.core.configuration.TestProfile
import shirates.core.driver.TestMode.isAndroid
import shirates.core.utility.android.AndroidDeviceInfo
import shirates.core.utility.android.AndroidDeviceUtility
import shirates.core.utility.getStringOrEmpty
import shirates.core.utility.ios.IosDeviceInfo
import shirates.core.utility.ios.IosDeviceUtility

object DeviceListUtility {

    class DeviceInfo() {
        var profile = ""
        var udid = ""
        var info = ""

        var profileInstance: TestProfile? = null
        var androidDeviceInfo: AndroidDeviceInfo? = null
        var iosDeviceInfo: IosDeviceInfo? = null

        val isEmulator: Boolean
            get() {
                return isRealDevice.not()
            }

        val isRealDevice: Boolean
            get() {
                return if (isAndroid) androidDeviceInfo?.isRealDevice ?: false
                else iosDeviceInfo?.isRealDevice ?: false
            }

        override fun toString(): String {
            return "$profile\t$info"
        }
    }

    const val NO_PROFILE = "(no profile)"

    /**
     * getConnectedDeviceList
     */
    fun getConnectedDeviceList(): List<DeviceInfo> {

        val list = mutableListOf<DeviceInfo>()

        if (TestConfigContainer.lastCreated == null) {
            TestConfigContainer(PropertiesManager.configFile)
        }

        getAndroidDeviceInfoList(list)
        getIosDeviceInfoList(list)

        val result = mutableListOf<DeviceInfo>()

        val realDevices = list.filter { it.isRealDevice }
        result.addAll(realDevices.filter { it.profile != NO_PROFILE }.sortedBy { it.profile })
        result.addAll(realDevices.filter { it.profile == NO_PROFILE }.sortedBy { it.udid })

        val emulators = list.filter { it.isEmulator }
        result.addAll(emulators.filter { it.profile != NO_PROFILE }.sortedBy { it.profile })
        result.addAll(emulators.filter { it.profile == NO_PROFILE }.sortedBy { it.udid })

        return result
    }

    private fun getAndroidDeviceInfoList(list: MutableList<DeviceInfo>) {
        val androidDevices = AndroidDeviceUtility.getAndroidDeviceList()
        for (androidDeviceInfo in androidDevices) {
            val deviceInfo = DeviceInfo()
            deviceInfo.androidDeviceInfo = androidDeviceInfo
            deviceInfo.profileInstance = TestConfigContainer.lastCreated?.profileMap?.values
                ?.firstOrNull { it.capabilities.getStringOrEmpty("udid") == androidDeviceInfo.udid }
            val profile = deviceInfo.profileInstance?.profileName
                ?: NO_PROFILE
            deviceInfo.profile = profile
            deviceInfo.udid = androidDeviceInfo.udid
            deviceInfo.info = androidDeviceInfo.toString()
            list.add(deviceInfo)
        }
    }

    private fun getIosDeviceInfoList(list: MutableList<DeviceInfo>) {
        val iosDevices = IosDeviceUtility.getBootedSimulatorDeviceList()
        for (iosDeviceInfo in iosDevices) {
            val deviceInfo = DeviceInfo()
            deviceInfo.iosDeviceInfo = iosDeviceInfo
            deviceInfo.profileInstance = TestConfigContainer.lastCreated?.profileMap?.values
                ?.firstOrNull { it.capabilities.getStringOrEmpty("udid") == iosDeviceInfo.udid }
            val profile = deviceInfo.profileInstance?.profileName
                ?: NO_PROFILE
            deviceInfo.profile = profile
            deviceInfo.udid = iosDeviceInfo.udid
            deviceInfo.info = iosDeviceInfo.toString()
            list.add(deviceInfo)
        }
    }

}