package tutorial.inaction

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.testcode.Manual
import shirates.core.testcode.UITest

@Testrun("testConfig/android/calculator/testrun.properties")
class CodeFirst1 : UITest() {

    @Manual
    @Test
    @DisplayName("Start calculator")
    fun A0010() {

        scenario {

        }
    }

    @Manual
    @Test
    @DisplayName("Add")
    fun A0020() {

        scenario {

        }
    }

    @Manual
    @Test
    @DisplayName("Divide by zero")
    fun A0030() {

        scenario {

        }
    }

}