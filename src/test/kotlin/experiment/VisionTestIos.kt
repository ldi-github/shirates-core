package experiment

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.disableCache
import shirates.core.testcode.UITest
import shirates.core.vision.driver.checkIsOFF
import shirates.core.vision.driver.detect
import shirates.core.vision.driver.right
import shirates.core.vision.driver.tap

@Testrun("testConfig/ios/iOSSettings/testrun.properties")
class VisionTestIos : UITest() {

    @Test
    fun visionTest() {

        scenario {
            case(1) {
                condition {
                    disableCache()
                }.action {
                    vision.detect("Accessibility").tap()
                    vision.detect("Display & Text Size").tap()
                    vision.detect("Larger Text").tap()
                }.expectation {
                    vision.detect("Larger Accessibility Sizes")
                        .right()
                        .checkIsOFF()
                }
            }
        }
    }
}