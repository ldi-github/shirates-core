package shirates.core.hand.devicetest

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.testcode.UITest

@Testrun(testrunFile = "unitTestConfig/android/androidSettings/androidDeviceUtilityTest.testrun.properties")
class AndroidDeviceUtilityTest2 : UITest() {

    @Test
    fun getActiveDeviceInfo() {

        /**
         * Change "android.profile" property in
         * unitTestConfig/android/androidSettings/androidDeviceUtilityTest.properties
         * and run this test function.
         */
    }
}