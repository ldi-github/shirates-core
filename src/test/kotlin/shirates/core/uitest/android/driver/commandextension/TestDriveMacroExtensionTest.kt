package shirates.core.uitest.android.driver.commandextension

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.macro
import shirates.core.driver.commandextension.screenIs
import shirates.core.testcode.UITest

@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
class TestDriveMacroExtensionTest : UITest() {

    @Test
    fun macro() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    it.macro("[Restart Calculator]")
                }.expectation {
                    it.screenIs("[Calculator Main Screen]")
                }
            }
        }
    }
}