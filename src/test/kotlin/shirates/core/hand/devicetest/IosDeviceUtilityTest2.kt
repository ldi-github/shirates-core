package shirates.core.hand.devicetest

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.testcode.UITest

@Testrun(testrunFile = "unitTestConfig/ios/iOSSettings/iosDeviceUtilityTest.testrun.properties")
class IosDeviceUtilityTest2 : UITest() {

    @Test
    fun getIosDeviceInfo() {

        /**
         * Change "android.profile" property in
         * unitTestConfig/android/androidSettings/androidDeviceUtilityTest.properties
         * and run this test function.
         */
    }

}