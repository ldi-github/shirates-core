package shirates.core.uitest.ios.basic.driver

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.TestMode
import shirates.core.driver.imageProfile
import shirates.core.driver.rootBounds
import shirates.core.testcode.UITest

@Testrun("unitTestConfig/ios/iOSSettings/testrun.properties")
class TestDrivePropertyExtensionTest : UITest() {

    @Test
    fun imageProfile() {

        assertThat(imageProfile).isEqualTo(TestMode.platformAnnotation)

        scenario {
            case(1) {
                val b = rootBounds
                assertThat(imageProfile).isEqualTo("${TestMode.platformAnnotation}_${b.width}x${b.height}")
            }
        }
    }

}