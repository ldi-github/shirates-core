package experiment

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest

@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
class ImageSelectorTest : UITest() {

    @Test
    fun image1() {

        scenario {
            case(1) {
                condition {
                    it.screenIs("[Android Settings Top Screen]")
                }.action {
//                    it.selectWithScrollDown("Tips & support").leftImage().cropImage()
                    it.findImage("Network & internet.png")
                    it.findImageWithScrollDown("Display.png")
                    it.findImageWithScrollDown("Tips & support.png")
                    it.findImageWithScrollUp("Display.png")
                    it.findImageWithScrollUp("Network & internet.png")
                }.expectation {
                    it.exist("Network & internet.png")
                    it.existWithScrollDown("Display.png")
                    it.existWithScrollDown("Tips & support.png")
                    it.existWithScrollUp("Display.png")
                    it.existWithScrollUp("Network & internet.png")
                }
            }
        }
    }

}