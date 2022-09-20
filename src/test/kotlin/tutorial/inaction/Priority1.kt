package tutorial.inaction

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.output
import shirates.core.testcode.Must
import shirates.core.testcode.Should
import shirates.core.testcode.UITest
import shirates.core.testcode.Want

@Testrun("testConfig/android/androidSettings/testrun.properties")
class Priority1 : UITest() {

    @Must
    @Order(1)
    @Test
    fun must() {
        scenario {
            case(1) {
                action {
                    output("must")
                }
            }
        }
    }

    @Should
    @Order(2)
    @Test
    fun should() {
        scenario {
            case(1) {
                action {
                    output("should")
                }
            }
        }
    }

    @Want
    @Order(3)
    @Test
    fun want() {
        scenario {
            case(1) {
                action {
                    output("want")
                }
            }
        }
    }

    @Order(4)
    @Test
    fun none() {
        scenario {
            case(1) {
                action {
                    output("none")
                }
            }
        }
    }

}