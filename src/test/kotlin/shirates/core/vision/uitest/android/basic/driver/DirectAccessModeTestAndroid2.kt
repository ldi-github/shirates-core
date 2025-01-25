package shirates.core.vision.uitest.android.basic.driver

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.TestMode
import shirates.core.driver.testContext
import shirates.core.exception.TestConfigException
import shirates.core.vision.driver.commandextension.enableCache
import shirates.core.vision.driver.commandextension.screenIs
import shirates.core.vision.testcode.VisionTest

@Testrun("unitTestConfig/vision/android/androidSettings/testrun.properties")
class DirectAccessModeTestAndroid2 : VisionTest() {

    @Test
    @Order(10)
    fun screensDirectoryIsNotConfigured() {

        enableCache()

        assertThat(TestMode.isVisionTest).isTrue()
        assertThat(testContext.useCache).isTrue()

        scenario {
            case(1) {
                condition {
                    assertThatThrownBy {
                        it.screenIs("[Android Settings Top Screen]")
                    }.isInstanceOf(TestConfigException::class.java)
                        .hasMessage("screens directory is not configured. (useCache=${testContext.useCache})")
                }
            }
        }
    }

}