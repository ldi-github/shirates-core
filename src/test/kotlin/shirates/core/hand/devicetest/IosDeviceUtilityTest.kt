package shirates.core.hand.devicetest

import org.junit.jupiter.api.Test
import shirates.core.configuration.TestProfile
import shirates.core.utility.ios.IosDeviceUtility

class IosDeviceUtilityTest {

    @Test
    fun getBootedSimulatorDeviceList() {

        val list = IosDeviceUtility.getBootedSimulatorDeviceList()
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

    @Test
    fun isInstalled() {

        val profile = TestProfile(profileName = "iPhone 14(iOS 16.2)")
        val iosDeviceInfo = IosDeviceUtility.getIosDeviceInfo(testProfile = profile)

        val isInstalled = IosDeviceUtility.isInstalled(
            bundleId = "com.facebook.WebDriverAgentRunner.xctrunner",
            iosDeviceInfo = iosDeviceInfo
        )
        println("isInstalled=$isInstalled")
    }

    @Test
    fun startSimulator() {

        val profile = TestProfile(profileName = "iPhone 14(iOS 16.2)")
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
    fun stopSimulator() {

        val profile = TestProfile(profileName = "iPhone 14(iOS 16.2)")
        val iosDeviceInfo = IosDeviceUtility.getIosDeviceInfo(testProfile = profile)

        IosDeviceUtility.stopSimulator(udid = iosDeviceInfo.udid)
    }

    @Test
    fun restartSimulator() {

//        val profile = TestProfile(profileName = "iPhone 14(iOS 16.2)")
//        val iosDeviceInfo = IosDeviceUtility.getIosDeviceInfo(testProfile = profile)
//
//        IosDeviceUtility.stopSimulator(udid = iosDeviceInfo.udid)
//        val r = IosDeviceUtility.startSimulator(iosDeviceInfo = iosDeviceInfo)
//        val r2 = IosDeviceUtility.waitSimulatorStatus(udid = iosDeviceInfo.udid)
//        println()

        val profile = TestProfile(profileName = "iPhone 14(iOS 16.2)")
        val iosDeviceInfo = IosDeviceUtility.getIosDeviceInfo(testProfile = profile)
        IosDeviceUtility.restartSimulator(udid = iosDeviceInfo.udid)
    }

    @Test
    fun setAppleLanguages() {

        val profile = TestProfile(profileName = "iPhone 14(iOS 16.2)")
        val iosDeviceInfo = IosDeviceUtility.getIosDeviceInfo(testProfile = profile)

        IosDeviceUtility.setAppleLanguages(udid = iosDeviceInfo.udid, "ja-JP", "en-US")
    }

    @Test
    fun setLanguage() {

        val profile = TestProfile(profileName = "iPhone 14(iOS 16.2)")
        val iosDeviceInfo = IosDeviceUtility.getIosDeviceInfo(testProfile = profile)

        if (iosDeviceInfo.status == "Shutdown") {
            IosDeviceUtility.startSimulator(iosDeviceInfo)
        }

        IosDeviceUtility.setAppleLocale(udid = iosDeviceInfo.udid, "ja-JP")
        Thread.sleep(3000)
        IosDeviceUtility.setAppleLocale(udid = iosDeviceInfo.udid, "en-US")
    }

}