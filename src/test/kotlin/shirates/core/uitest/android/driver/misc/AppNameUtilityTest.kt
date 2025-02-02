package shirates.core.uitest.android.driver.misc

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.thisIs
import shirates.core.testcode.UITest
import shirates.core.utility.misc.AppNameUtility

@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
class AppNameUtilityTest : UITest() {

    @Test
    fun getAppNameWithoutExtension() {

        scenario {
            case(1) {
                action {
                    s1 = AppNameUtility.getAppNameWithoutExtension("app1.hoge.stg")
                }.expectation {
                    s1.thisIs("app1.hoge")
                }
            }
            case(2) {
                action {
                    s1 = AppNameUtility.getAppNameWithoutExtension("app1.hoge.stg")
                }.expectation {
                    s1.thisIs("app1.hoge")
                }
            }
            case(3) {
                action {
                    s1 = AppNameUtility.getAppNameWithoutExtension("app1.stg")
                }.expectation {
                    s1.thisIs("app1")
                }
            }
            case(4) {
                action {
                    s1 = AppNameUtility.getAppNameWithoutExtension("app1")
                }.expectation {
                    s1.thisIs("app1")
                }
            }
        }
    }

    @Test
    fun getPackageOrBundleId() {

        scenario {
            case(1) {
                action {
                    s1 = AppNameUtility.getPackageOrBundleId("Settings")
                }.expectation {
                    s1.thisIs("com.android.settings")
                }
            }
            case(2) {
                action {
                    s1 = AppNameUtility.getPackageOrBundleId("[Settings]")
                }.expectation {
                    s1.thisIs("com.android.settings")
                }
            }
            case(3) {
                action {
                    s1 = AppNameUtility.getPackageOrBundleId("com.android.settings/.Settings")
                }.expectation {
                    s1.thisIs("com.android.settings")
                }
            }
            case(4) {
                action {
                    s1 = AppNameUtility.getPackageOrBundleId("com.android.settings")
                }.expectation {
                    s1.thisIs("com.android.settings")
                }
            }
        }

    }

    @Test
    fun getAppNickNameFromPackageName() {

        scenario {
            case(1) {
                action {
                    s1 = AppNameUtility.getAppNickNameFromPackageName("com.android.chrome")
                }.expectation {
                    s1.thisIs("[Chrome]")
                }
            }
            case(2) {
                action {
                    s1 = AppNameUtility.getAppNickNameFromPackageName("[no exist]")
                }.expectation {
                    s1.thisIs("")
                }
            }
        }
    }

    @Test
    fun getCurrentAppName() {

        scenario {
            case(1) {
                action {
                    s1 = AppNameUtility.getCurrentAppName()
                }.expectation {
                    s1.thisIs("Settings")
                }
            }
        }
    }
}