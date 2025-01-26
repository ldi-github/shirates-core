package shirates.core.uitest.android.testcode

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.Const
import shirates.core.configuration.PropertiesManager
import shirates.core.testcode.UITest

class UITestTest : UITest() {

    @Test
    fun default() {

        assertThat(PropertiesManager.getDefaultTestrunFile()).isEqualTo(Const.TESTRUN_PROPERTIES)
        assertThat(PropertiesManager.testrunFile).isEqualTo(Const.TESTRUN_PROPERTIES)
    }

}