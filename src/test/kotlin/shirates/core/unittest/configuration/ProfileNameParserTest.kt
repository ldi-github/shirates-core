package shirates.core.unittest.configuration

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.configuration.ProfileNameParser
import shirates.core.testcode.UnitTest

class ProfileNameParserTest : UnitTest() {

    @Test
    fun init_platformName() {

        run {
            val testProfileName = "ios"
            val parser = ProfileNameParser(testProfileName)
            assertThat(parser.testProfileName).isEqualTo(testProfileName)
            assertThat(parser.platformName).isEqualTo("iOS")
            assertThat(parser.model).isEqualTo("")
            assertThat(parser.osVersion).isEqualTo("")
            assertThat(parser.udid).isEqualTo("")
        }
        run {
            val testProfileName = "iOS"
            val parser = ProfileNameParser(testProfileName)
            assertThat(parser.testProfileName).isEqualTo(testProfileName)
            assertThat(parser.platformName).isEqualTo("iOS")
            assertThat(parser.model).isEqualTo("")
            assertThat(parser.osVersion).isEqualTo("")
            assertThat(parser.udid).isEqualTo("")
        }
        run {
            val testProfileName = "iphone"
            val parser = ProfileNameParser(testProfileName)
            assertThat(parser.testProfileName).isEqualTo(testProfileName)
            assertThat(parser.platformName).isEqualTo("iOS")
            assertThat(parser.model).isEqualTo("")
            assertThat(parser.osVersion).isEqualTo("")
            assertThat(parser.udid).isEqualTo("")
        }
        run {
            val testProfileName = "iPhone"
            val parser = ProfileNameParser(testProfileName)
            assertThat(parser.testProfileName).isEqualTo(testProfileName)
            assertThat(parser.platformName).isEqualTo("iOS")
            assertThat(parser.model).isEqualTo("")
            assertThat(parser.osVersion).isEqualTo("")
            assertThat(parser.udid).isEqualTo("")
        }
        run {
            val testProfileName = "ipad"
            val parser = ProfileNameParser(testProfileName)
            assertThat(parser.testProfileName).isEqualTo(testProfileName)
            assertThat(parser.platformName).isEqualTo("iOS")
            assertThat(parser.model).isEqualTo("")
            assertThat(parser.osVersion).isEqualTo("")
            assertThat(parser.udid).isEqualTo("")
        }
        run {
            val testProfileName = "iPad"
            val parser = ProfileNameParser(testProfileName)
            assertThat(parser.testProfileName).isEqualTo(testProfileName)
            assertThat(parser.platformName).isEqualTo("iOS")
            assertThat(parser.model).isEqualTo("")
            assertThat(parser.osVersion).isEqualTo("")
            assertThat(parser.udid).isEqualTo("")
        }
    }

    @Test
    fun init_model_osVersion() {

        /**
         * model(osVersion)
         */
        run {
            val testProfileName = "Pixel 3a(12)"
            val parser = ProfileNameParser(testProfileName)
            assertThat(parser.testProfileName).isEqualTo(testProfileName)
            assertThat(parser.platformName).isEqualTo("Android")
            assertThat(parser.model).isEqualTo("Pixel 3a")
            assertThat(parser.osVersion).isEqualTo("12")
            assertThat(parser.udid).isEqualTo("")
        }
        run {
            val testProfileName = "iPhone 14 Pro Max(16.2)"
            val parser = ProfileNameParser(testProfileName)
            assertThat(parser.testProfileName).isEqualTo(testProfileName)
            assertThat(parser.platformName).isEqualTo("iOS")
            assertThat(parser.model).isEqualTo("iPhone 14 Pro Max")
            assertThat(parser.osVersion).isEqualTo("16.2")
            assertThat(parser.udid).isEqualTo("")
        }
        run {
            val testProfileName = "iPad Air (5th generation)(16.2)"
            val parser = ProfileNameParser(testProfileName)
            assertThat(parser.testProfileName).isEqualTo(testProfileName)
            assertThat(parser.platformName).isEqualTo("iOS")
            assertThat(parser.model).isEqualTo("iPad Air (5th generation)")
            assertThat(parser.osVersion).isEqualTo("16.2")
            assertThat(parser.udid).isEqualTo("")
        }

        /**
         * model(iOS osVersion)
         */
        run {
            val testProfileName = "iPhone 14 Pro Max(iOS 16.2)"
            val parser = ProfileNameParser(testProfileName)
            assertThat(parser.testProfileName).isEqualTo(testProfileName)
            assertThat(parser.platformName).isEqualTo("iOS")
            assertThat(parser.model).isEqualTo("iPhone 14 Pro Max")
            assertThat(parser.osVersion).isEqualTo("16.2")
            assertThat(parser.udid).isEqualTo("")
        }
        run {
            val testProfileName = "iPad Air (5th generation)(iOS 16.2)"
            val parser = ProfileNameParser(testProfileName)
            assertThat(parser.testProfileName).isEqualTo(testProfileName)
            assertThat(parser.platformName).isEqualTo("iOS")
            assertThat(parser.model).isEqualTo("iPad Air (5th generation)")
            assertThat(parser.osVersion).isEqualTo("16.2")
            assertThat(parser.udid).isEqualTo("")
        }

        /**
         * model(Android osVersion)
         */
        run {
            val testProfileName = "Pixel 3a(Android 12)"
            val parser = ProfileNameParser(testProfileName)
            assertThat(parser.testProfileName).isEqualTo(testProfileName)
            assertThat(parser.platformName).isEqualTo("Android")
            assertThat(parser.model).isEqualTo("Pixel 3a")
            assertThat(parser.osVersion).isEqualTo("12")
            assertThat(parser.udid).isEqualTo("")
        }
    }

    @Test
    fun init_android_osVersion() {

        run {
            val testProfileName = "Android 12"
            val parser = ProfileNameParser(testProfileName)
            assertThat(parser.testProfileName).isEqualTo(testProfileName)
            assertThat(parser.platformName).isEqualTo("Android")
            assertThat(parser.model).isEqualTo("")
            assertThat(parser.osVersion).isEqualTo("12")
            assertThat(parser.udid).isEqualTo("")
        }
        run {
            val testProfileName = "android 12"
            val parser = ProfileNameParser(testProfileName)
            assertThat(parser.testProfileName).isEqualTo(testProfileName)
            assertThat(parser.platformName).isEqualTo("Android")
            assertThat(parser.model).isEqualTo("")
            assertThat(parser.osVersion).isEqualTo("12")
            assertThat(parser.udid).isEqualTo("")
        }
    }

    @Test
    fun init_ios_osVersion() {

        run {
            val testProfileName = "iOS 16.2"
            val parser = ProfileNameParser(testProfileName)
            assertThat(parser.testProfileName).isEqualTo(testProfileName)
            assertThat(parser.platformName).isEqualTo("iOS")
            assertThat(parser.model).isEqualTo("")
            assertThat(parser.osVersion).isEqualTo("16.2")
            assertThat(parser.udid).isEqualTo("")
        }
        run {
            val testProfileName = "ios 16.2"
            val parser = ProfileNameParser(testProfileName)
            assertThat(parser.testProfileName).isEqualTo(testProfileName)
            assertThat(parser.platformName).isEqualTo("iOS")
            assertThat(parser.model).isEqualTo("")
            assertThat(parser.osVersion).isEqualTo("16.2")
            assertThat(parser.udid).isEqualTo("")
        }
    }

    @Test
    fun init_ipad_osVersion() {

        run {
            val testProfileName = "iPad 16.2"
            val parser = ProfileNameParser(testProfileName)
            assertThat(parser.testProfileName).isEqualTo(testProfileName)
            assertThat(parser.platformName).isEqualTo("iOS")
            assertThat(parser.model).isEqualTo("")
            assertThat(parser.osVersion).isEqualTo("16.2")
            assertThat(parser.udid).isEqualTo("")
        }
        run {
            val testProfileName = "ipad 16.2"
            val parser = ProfileNameParser(testProfileName)
            assertThat(parser.testProfileName).isEqualTo(testProfileName)
            assertThat(parser.platformName).isEqualTo("iOS")
            assertThat(parser.model).isEqualTo("")
            assertThat(parser.osVersion).isEqualTo("16.2")
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
            assertThat(parser.osVersion).isEqualTo("")
            assertThat(parser.udid).isEqualTo(testProfileName)
        }
    }

    @Test
    fun init_osVersion() {

        run {
            val testProfileName = "16.2"
            val parser = ProfileNameParser(testProfileName)
            assertThat(parser.testProfileName).isEqualTo(testProfileName)
            assertThat(parser.platformName).isEqualTo("")
            assertThat(parser.model).isEqualTo("")
            assertThat(parser.osVersion).isEqualTo(testProfileName)
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
            assertThat(parser.osVersion).isEqualTo("")
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
            assertThat(parser.osVersion).isEqualTo("")
            assertThat(parser.udid).isEqualTo(testProfileName)
        }
    }

}