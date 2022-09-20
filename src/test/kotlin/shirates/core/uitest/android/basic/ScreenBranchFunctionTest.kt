package shirates.core.uitest.android.basic

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.branchextension.ifScreenIs
import shirates.core.driver.branchextension.ifScreenIsNot
import shirates.core.driver.commandextension.waitScreen
import shirates.core.testcode.UITest
import shirates.core.testcode.Want

@Want
@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
class ScreenBranchFunctionTest : UITest() {

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