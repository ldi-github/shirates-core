package shirates.core.uitest.ios.driver.commandextension

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.cropImage
import shirates.core.driver.commandextension.screenIs
import shirates.core.driver.commandextension.selectWithScrollDown
import shirates.core.testcode.UITest
import shirates.core.testcode.Want
import shirates.core.utility.image.saveImage

@Want
@Testrun("testConfig/ios/iOSSettings/testrun.properties")
class TestElementImageExtensionTest : UITest() {

    @Test
    @Order(10)
    fun cropImage() {

        scenario {
            case(1) {
                condition {
                    it.screenIs("[iOS Settings Top Screen]")
                }.action {
                    val path =
                        it.profile.testConfigPath!!.parent.resolve("screens/[iOS Settings Top Screen]")
                    it.selectWithScrollDown("[General Icon]").cropImage().lastCropInfo!!.croppedImage!!
                        .saveImage(path.resolve(it.subject).toString())
                    it.selectWithScrollDown("[Accessibility Icon]").cropImage().lastCropInfo!!.croppedImage!!
                        .saveImage(path.resolve(it.subject).toString())
                    it.selectWithScrollDown("[Privacy & Security Icon]").cropImage().lastCropInfo!!.croppedImage!!
                        .saveImage(path.resolve(it.subject).toString())
                    it.selectWithScrollDown("[Privacy & Security Icon]").cropImage().lastCropInfo!!.croppedImage!!
                        .saveImage(path.resolve(it.subject).toString())
                    it.selectWithScrollDown("[Developer Icon]").cropImage().lastCropInfo!!.croppedImage!!
                        .saveImage(path.resolve(it.subject).toString())

                }.expectation {

                }
            }
        }

    }

}