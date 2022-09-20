package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.logging.TestLog.info
import shirates.core.logging.TestLog.warn
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class InfoAndWarn1 : UITest() {

    @Test
    @Order(10)
    fun infoAndWarn1() {

        scenario {
            case(1) {
                action {
                    info("info")
                    warn("warn")
                }
            }
        }
    }

}