package tutorial.inaction

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.testcode.NoLoadRun
import shirates.core.testcode.UITest

@Testrun("testConfig/android/calculator/testrun.properties")
class CodeFirst1 : UITest() {

    @NoLoadRun
    @Test
    @DisplayName("Start calculator")
    fun A0010() {

        scenario {

        }
    }

    @NoLoadRun
    @Test
    @DisplayName("Add")
    fun A0020() {

        scenario {

        }
    }

    @NoLoadRun
    @Test
    @DisplayName("Divide by zero")
    fun A0030() {

        scenario {

        }
    }

}