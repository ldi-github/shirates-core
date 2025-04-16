package shirates.core.vision.uitest.android.driver.branchextension

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.testcode.Want
import shirates.core.vision.driver.commandextension.ifScreenIs
import shirates.core.vision.driver.commandextension.ifScreenIsNot
import shirates.core.vision.driver.commandextension.waitScreen
import shirates.core.vision.testcode.VisionTest

@Want
@Testrun("unitTestConfig/vision/android/androidSettings/testrun.properties")
class VisionDriveScreenBranchFunctionTest : VisionTest() {

    @Test
    fun ifScreenIs_ifScreenIsNot_Test() {

        // Arrange
        it.waitScreen("[Android Settings Top Screen]")
        val list = mutableListOf<String>()

        // Act
        ifScreenIs("[Android Settings Top Screen]") {
            list.add("The screen is [Android Settings Top Screen]")
        }
        ifScreenIs("[Network & internet Screen]") {
            list.add("The screen is [Network & internet Screen]")
        }
        ifScreenIsNot("[Android Settings Top Screen]") {
            list.add("The screen is not [Android Settings Top Screen]")
        }
        ifScreenIsNot("[Network & internet Screen]") {
            list.add("The screen is not [Network & internet Screen]")
        }

        // Assert
        assertThat(list.count()).isEqualTo(2)
        assertThat(list[0]).isEqualTo("The screen is [Android Settings Top Screen]")
        assertThat(list[1]).isEqualTo("The screen is not [Network & internet Screen]")

    }

}