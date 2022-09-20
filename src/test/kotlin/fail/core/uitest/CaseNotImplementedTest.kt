package fail.core.uitest

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class CaseNotImplementedTest : UITest() {

    @Test
    fun caseNotImplemented() {

        scenario {

        }
    }
}