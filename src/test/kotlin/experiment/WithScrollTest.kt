package experiment

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class WithScrollTest : UITest() {

    @Test
    @Order(10)
    fun withScroll() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    withScrollDown {
                        it.exist("[System]")

                        withScrollUp {
                            it.exist("[Accessibility]")

                            withoutScroll {
                                it.exist("[Accessibility]")
                                    .existWithScrollDown("[System]")
                            }
                        }
                    }
                }
            }
        }
    }

}