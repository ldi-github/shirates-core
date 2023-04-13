package shirates.core.unittest.configuration

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.configuration.ProfileNameParser
import shirates.core.testcode.UnitTest

class ProfileNameParserTest : UnitTest() {

    @Test
    fun init_platformName() {

        run {
            val testProfileName = "Android"
            val parser = ProfileNameParser(testProfileName)
            assertThat(parser.testProfileName).isEqualTo(testProfileName)
            assertThat(parser.platformName).isEqualTo("Android")
            assertThat(parser.model).isEqualTo("")
            assertThat(parser.platformVersion).isEqualTo("")
            assertThat(parser.udid).isEqualTo("")
        }
        run {
            val testProfileName = "Android/googlePixel"
            val parser = ProfileNameParser(testProfileName)
            assertThat(parser.testProfileName).isEqualTo(testProfileName)
            assertThat(parser.platformName).isEqualTo("Android")
            assertThat(parser.model).isEqualTo("")
            assertThat(parser.platformVersion).isEqualTo("")
            assertThat(parser.udid).isEqualTo("")
        }
        run {
            val testProfileName = "ios"
            val parser = ProfileNameParser(testProfileName)
            assertThat(parser.testProfileName).isEqualTo(testProfileName)
            assertThat(parser.platformName).isEqualTo("iOS")
            assertThat(parser.model).isEqualTo("")
            assertThat(parser.platformVersion).isEqualTo("")
            assertThat(parser.udid).isEqualTo("")
        }
        run {
            val testProfileName = "iOS"
            val parser = ProfileNameParser(testProfileName)
            assertThat(parser.testProfileName).isEqualTo(testProfileName)
            assertThat(parser.platformName).isEqualTo("iOS")
            assertThat(parser.model).isEqualTo("")
            assertThat(parser.platformVersion).isEqualTo("")
            assertThat(parser.udid).isEqualTo("")
        }
        run {
            val testProfileName = "iphone"
            val parser = ProfileNameParser(testProfileName)
            assertThat(parser.testProfileName).isEqualTo(testProfileName)
            assertThat(parser.platformName).isEqualTo("iOS")
            assertThat(parser.model).isEqualTo("")
            assertThat(parser.platformVersion).isEqualTo("")
            assertThat(parser.udid).isEqualTo("")
        }
        run {
            val testProfileName = "iPhone"
            val parser = ProfileNameParser(testProfileName)
            assertThat(parser.testProfileName).isEqualTo(testProfileName)
            assertThat(parser.platformName).isEqualTo("iOS")
            assertThat(parser.model).isEqualTo("")
            assertThat(parser.platformVersion).isEqualTo("")
            assertThat(parser.udid).isEqualTo("")
        }
        run {
            val testProfileName = "ipad"
            val parser = ProfileNameParser(testProfileName)
            assertThat(parser.testProfileName).isEqualTo(testProfileName)
            assertThat(parser.platformName).isEqualTo("iOS")
            assertThat(parser.model).isEqualTo("")
            assertThat(parser.platformVersion).isEqualTo("")
            assertThat(parser.udid).isEqualTo("")
        }
        run {
            val testProfileName = "iPad"
            val parser = ProfileNameParser(testProfileName)
            assertThat(parser.testProfileName).isEqualTo(testProfileName)
            assertThat(parser.platformName).isEqualTo("iOS")
            assertThat(parser.model).isEqualTo("")
            assertThat(parser.platformVersion).isEqualTo("")
            assertThat(parser.udid).isEqualTo("")
        }
    }

    @Test
    fun init_model_platformVersion() {

        /**
         * model(platformVersion)
         */
        run {
            val testProfileName = "Pixel 3a(12)"
            val parser = ProfileNameParser(testProfileName)
            assertThat(parser.testProfileName).isEqualTo(testProfileName)
            assertThat(parser.platformName).isEqualTo("Android")
            assertThat(parser.model).isEqualTo("Pixel 3a")
            assertThat(parser.platformVersion).isEqualTo("12")
            assertThat(parser.udid).isEqualTo("")
        }
        run {
            val testProfileName = "iPhone 14 Pro Max(16.2)"
            val parser = ProfileNameParser(testProfileName)
            assertThat(parser.testProfileName).isEqualTo(testProfileName)
            assertThat(parser.platformName).isEqualTo("iOS")
            assertThat(parser.model).isEqualTo("iPhone 14 Pro Max")
            assertThat(parser.platformVersion).isEqualTo("16.2")
            assertThat(parser.udid).isEqualTo("")
        }
        run {
            val testProfileName = "iPad Air (5th generation)(16.2)"
            val parser = ProfileNameParser(testProfileName)
            assertThat(parser.testProfileName).isEqualTo(testProfileName)
            assertThat(parser.platformName).isEqualTo("iOS")
            assertThat(parser.model).isEqualTo("iPad Air (5th generation)")
            assertThat(parser.platformVersion).isEqualTo("16.2")
            assertThat(parser.udid).isEqualTo("")
        }

        /**
         * model(iOS platformVersion)
         */
        run {
            val testProfileName = "iPhone 14 Pro Max(iOS 16.2)"
            val parser = ProfileNameParser(testProfileName)
            assertThat(parser.testProfileName).isEqualTo(testProfileName)
            assertThat(parser.platformName).isEqualTo("iOS")
            assertThat(parser.model).isEqualTo("iPhone 14 Pro Max")
            assertThat(parser.platformVersion).isEqualTo("16.2")
            assertThat(parser.udid).isEqualTo("")
        }
        run {
            val testProfileName = "iPad Air (5th generation)(iOS 16.2)"
            val parser = ProfileNameParser(testProfileName)
            assertThat(parser.testProfileName).isEqualTo(testProfileName)
            assertThat(parser.platformName).isEqualTo("iOS")
            assertThat(parser.model).isEqualTo("iPad Air (5th generation)")
            assertThat(parser.platformVersion).isEqualTo("16.2")
            assertThat(parser.udid).isEqualTo("")
        }

        /**
         * model(Android platformVersion)
         */
        run {
            val testProfileName = "Pixel 3a(Android 12)"
            val parser = ProfileNameParser(testProfileName)
            assertThat(parser.testProfileName).isEqualTo(testProfileName)
            assertThat(parser.platformName).isEqualTo("Android")
            assertThat(parser.model).isEqualTo("Pixel 3a")
            assertThat(parser.platformVersion).isEqualTo("12")
            assertThat(parser.udid).isEqualTo("")
        }
    }

    @Test
    fun init_android_platformVersion() {

        run {
            val testProfileName = "Android 12"
            val parser = ProfileNameParser(testProfileName)
            assertThat(parser.testProfileName).isEqualTo(testProfileName)
            assertThat(parser.platformName).isEqualTo("Android")
            assertThat(parser.model).isEqualTo("")
            assertThat(parser.platformVersion).isEqualTo("12")
            assertThat(parser.udid).isEqualTo("")
        }
        run {
            val testProfileName = "android 12"
            val parser = ProfileNameParser(testProfileName)
            assertThat(parser.testProfileName).isEqualTo(testProfileName)
            assertThat(parser.platformName).isEqualTo("Android")
            assertThat(parser.model).isEqualTo("")
            assertThat(parser.platformVersion).isEqualTo("12")
            assertThat(parser.udid).isEqualTo("")
        }
    }

    @Test
    fun init_ios_platformVersion() {

        run {
            val testProfileName = "iOS 16.2"
            val parser = ProfileNameParser(testProfileName)
            assertThat(parser.testProfileName).isEqualTo(testProfileName)
            assertThat(parser.platformName).isEqualTo("iOS")
            assertThat(parser.model).isEqualTo("")
            assertThat(parser.platformVersion).isEqualTo("16.2")
            assertThat(parser.udid).isEqualTo("")
        }
        run {
            val testProfileName = "ios 16.2"
            val parser = ProfileNameParser(testProfileName)
            assertThat(parser.testProfileName).isEqualTo(testProfileName)
            assertThat(parser.platformName).isEqualTo("iOS")
            assertThat(parser.model).isEqualTo("")
            assertThat(parser.platformVersion).isEqualTo("16.2")
            assertThat(parser.udid).isEqualTo("")
        }
    }

    @Test
    fun init_ipad_platformVersion() {

        run {
            val testProfileName = "iPad 16.2"
            val parser = ProfileNameParser(testProfileName)
            assertThat(parser.testProfileName).isEqualTo(testProfileName)
            assertThat(parser.platformName).isEqualTo("iOS")
            assertThat(parser.model).isEqualTo("")
            assertThat(parser.platformVersion).isEqualTo("16.2")
            assertThat(parser.udid).isEqualTo("")
        }
        run {
            val testProfileName = "ipad 16.2"
            val parser = ProfileNameParser(testProfileName)
            assertThat(parser.testProfileName).isEqualTo(testProfileName)
            assertThat(parser.platformName).isEqualTo("iOS")
            assertThat(parser.model).isEqualTo("")
            assertThat(parser.platformVersion).isEqualTo("16.2")
            assertThat(parser.udid).isEqualTo("")
        }
    }

    @Test
    fun init_emulator() {

        run {
            val testProfileName = "emulator-5554"
            val parser = ProfileNameParser(testProfileName)
            assertThat(parser.testProfileName).isEqualTo(testProfileName)
            assertThat(parser.platformName).isEqualTo("Android")
            assertThat(parser.model).isEqualTo("")
            assertThat(parser.platformVersion).isEqualTo("")
            assertThat(parser.udid).isEqualTo(testProfileName)
        }
    }

    @Test
    fun init_platformVersion() {

        run {
            val testProfileName = "16.2"
            val parser = ProfileNameParser(testProfileName)
            assertThat(parser.testProfileName).isEqualTo(testProfileName)
            assertThat(parser.platformName).isEqualTo("")
            assertThat(parser.model).isEqualTo("")
            assertThat(parser.platformVersion).isEqualTo(testProfileName)
            assertThat(parser.udid).isEqualTo("")
        }
    }

    @Test
    fun init_udid_iosSimulator() {

        run {
            val testProfileName = "2483CD4B-853E-4D40-BACC-FC912CE6E198"
            val parser = ProfileNameParser(testProfileName)
            assertThat(parser.testProfileName).isEqualTo(testProfileName)
            assertThat(parser.platformName).isEqualTo("iOS")
            assertThat(parser.model).isEqualTo("")
            assertThat(parser.platformVersion).isEqualTo("")
            assertThat(parser.udid).isEqualTo(testProfileName)
        }
    }

    @Test
    fun init_udid_AndroidRealDevice() {

        run {
            val testProfileName = "ABCDEFGHIJ"
            val parser = ProfileNameParser(testProfileName)
            assertThat(parser.testProfileName).isEqualTo(testProfileName)
            assertThat(parser.platformName).isEqualTo("Android")
            assertThat(parser.model).isEqualTo("")
            assertThat(parser.platformVersion).isEqualTo("")
            assertThat(parser.udid).isEqualTo(testProfileName)
        }
    }

}