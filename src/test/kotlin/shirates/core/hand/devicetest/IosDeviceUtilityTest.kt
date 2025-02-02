package shirates.core.hand.devicetest

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.TestProfile
import shirates.core.testcode.UnitTest
import shirates.core.utility.ios.IosDeviceUtility

class IosDeviceUtilityTest : UnitTest() {

    @Test
    @Order(1)
    fun getBootedSimulatorDeviceList() {

        val list = IosDeviceUtility.getBootedSimulatorDeviceList()
        list.forEach {
            println(it)
        }
    }

    @Test
    @Order(2)
    fun getIosDeviceList() {

        val list = IosDeviceUtility.getIosDeviceList()
        list.forEach {
            println(it)
        }

    }

    @Test
    @Order(3)
    fun getRealDeviceList() {

        val list = IosDeviceUtility.getRealDeviceList()
        list.forEach {
            println(it)
        }
    }

    @Test
    @Order(4)
    fun getSimulatorDeviceList() {

        val list = IosDeviceUtility.getSimulatorDeviceList()
        list.forEach {
            println(it)
        }
    }

    val profileName = "iPhone SE (3rd generation)"

    @Test
    @Order(5)
    fun startSimulator() {

        val profile = TestProfile(profileName = profileName)
        val iosDeviceInfo = IosDeviceUtility.getIosDeviceInfo(testProfile = profile)

        val r = IosDeviceUtility.startSimulator(iosDeviceInfo = iosDeviceInfo)
        val r2 = IosDeviceUtility.waitSimulatorStatus(udid = iosDeviceInfo.udid)
    }

//    @Test
//    fun startSimulatorAndInstallWDA() {
//
//        val profile = TestConfig(testConfigFile = "testConfig/ios/iOSSettings/iOSSettingsConfig.json").commonProfile
//        profile.profileName = "iPhone 14(iOS 16.0)-05"
//        IosDeviceUtility.startSimulatorAndInstallWDA(testProfile = profile)
//
//        println()
//    }

    @Test
    @Order(6)
    fun isInstalled() {

        val profile = TestProfile(profileName = profileName)
        val iosDeviceInfo = IosDeviceUtility.getIosDeviceInfo(testProfile = profile)

        val isInstalled = IosDeviceUtility.isInstalled(
            bundleId = "com.facebook.WebDriverAgentRunner.xctrunner",
            iosDeviceInfo = iosDeviceInfo
        )
        println("isInstalled=$isInstalled")
    }

    @Test
    @Order(10)
    fun restartSimulator() {

        val profile = TestProfile(profileName = profileName)
        val iosDeviceInfo = IosDeviceUtility.getIosDeviceInfo(testProfile = profile)
        IosDeviceUtility.restartSimulator(udid = iosDeviceInfo.udid)
    }

    @Test
    @Order(11)
    fun terminateSpringBoardByUdid() {

        val profile = TestProfile(profileName = profileName)
        val iosDeviceInfo = IosDeviceUtility.getIosDeviceInfo(testProfile = profile)
        IosDeviceUtility.terminateSpringBoardByUdid(iosDeviceInfo.udid, log = true)
    }

    @Test
    @Order(12)
    fun stopSimulator() {

        val profile = TestProfile(profileName = profileName)
        val iosDeviceInfo = IosDeviceUtility.getIosDeviceInfo(testProfile = profile)

        IosDeviceUtility.stopSimulator(udid = iosDeviceInfo.udid)
    }

    @Test
    @Order(13)
    fun getWebDriverAgentDirectories() {

        val directories = IosDeviceUtility.getWebDriverAgentDirectories()
        directories.forEach { println(it) }
    }

    @Test
    @Order(14)
    fun getWebDriverAgentDirectory() {

        val dir = IosDeviceUtility.getWebDriverAgentDirectory()
        println(dir)
    }
}