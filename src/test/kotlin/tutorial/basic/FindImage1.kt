package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest
import shirates.helper.TestSetupHelper

@Testrun("testConfig/android/androidSettings/testrun.properties")
class FindImage1 : UITest() {

    @Test
    @Order(10)
    fun croppingImages() {

        TestSetupHelper.croppingImagesInNetworkAndInternetScreen()
    }

    /**
     * Note:
     *
     * Run croppingImages()
     * to set up template image files
     * before running this sample.
     */
    @Test
    @Order(20)
    fun findImage() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    it.findImage("[Network & internet Icon].png")
                    it.findImageWithScrollDown("[Display Icon].png")
                    it.findImageWithScrollDown("[Tips & support Icon].png")
                    it.findImageWithScrollUp("[Display Icon].png")
                    it.findImageWithScrollUp("[Network & internet Icon].png")
                }.expectation {
                    it.exist("[Network & internet Icon].png")
                    it.existWithScrollDown("[Display Icon].png")
                    it.existWithScrollDown("[Tips & support Icon].png")
                    it.existWithScrollUp("[Display Icon].png")
                    it.existWithScrollUp("[Network & internet Icon].png")
                }
            }
        }
    }

}