package shirates.core.vision.uitest.android.driver.commandextension

import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.testcode.Want
import shirates.core.vision.driver.commandextension.detect
import shirates.core.vision.driver.commandextension.macro
import shirates.core.vision.driver.commandextension.textIs
import shirates.core.vision.testcode.VisionTest

@Want
@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
class VisionElementAssertionExtensionTest : VisionTest() {

    @Test
    fun textIs() {

        // Arrange
        it.macro("[Android Settings Top Screen]")
        // Act, Assert
        it.detect("network & internet")
            .textIs("Network & internet")
            .textIs("network & internet")
            .textIs("network")
            .textIs("internet")
        // Act, Assert
        assertThatThrownBy {
            it.textIs("")
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("containedText must not be blank")
    }

}