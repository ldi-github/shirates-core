package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest
import shirates.helper.ImageSetupHelper

@Testrun("testConfig/android/androidSettings/testrun.properties")
class FindImage1 : UITest() {

    @Test
    @Order(10)
    fun croppingImages() {

        scenario {
            ImageSetupHelper.setupImageAndroidSettingsTopScreen()
        }
    }

    @Test
    @Order(20)
    fun findImage() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    withScrollDown {
                        it.findImage("[Network & internet Icon].png")
                        it.findImage("[Display Icon].png")
                        it.findImage("[Tips & support Icon].png")
                    }
                    withScrollUp {
                        it.findImage("[Display Icon].png")
                        it.findImage("[Network & internet Icon].png")
                    }
                }.expectation {
                    withScrollDown {
                        it.existImage("[Network & internet Icon].png")
                        it.existImage("[Display Icon].png")
                        it.existImage("[Tips & support Icon].png")
                    }
                    withScrollUp {
                        it.existImage("[Display Icon].png")
                        it.existImage("[Network & internet Icon].png")
                    }
                }
            }
        }
    }

}