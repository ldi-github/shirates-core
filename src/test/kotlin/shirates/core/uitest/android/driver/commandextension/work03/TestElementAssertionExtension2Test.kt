package shirates.core.uitest.android.driver.commandextension.work03

import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.exception.TestNGException
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class TestElementAssertionExtension2Test : UITest() {

    @Test
    fun selectedIs() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Play Store Screen]")
                }.action {
                    it.tap("Games")
                }.expectation {
                    it.select("Games")
                        .aboveImage()
                        .selectedIs("true")

                    assertThatThrownBy {
                        it.select("Games")
                            .selectedIs("true")
                    }.isInstanceOf(TestNGException::class.java)
                        .hasMessage("<Games>.selected is \"true\" (actual=\"false\")")
                }
            }
        }

    }

}