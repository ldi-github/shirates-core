package demo

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class AndroidSettingsDemo : UITest() {

    @Test
    fun airplaneModeSwitch() {

        scenario {
            case(1) {
                condition {
                    it.tapAppIcon("Settings")
                        .screenIs("[Android Settings Top Screen]")
                }.action {
                    it.tap("[Network & internet]")
                }.expectation {
                    it.screenIs("[Network & internet Screen]")
                }
            }

            case(2) {
                condition {
                    it.select("{Airplane mode switch}")
                        .checkIsOFF()
                }.action {
                    it.tap("{Airplane mode switch}")
                }.expectation {
                    it.select("{Airplane mode switch}")
                        .checkIsON()
                }
            }

            case(3) {
                action {
                    it.tap("{Airplane mode switch}")
                }.expectation {
                    it.select("{Airplane mode switch}")
                        .checkIsOFF()
                }
            }
        }
    }
}