package shirates.core.vision.uitest.android.driver.misc

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.vision.driver.commandextension.detect
import shirates.core.vision.driver.commandextension.tap
import shirates.core.vision.driver.commandextension.textIs
import shirates.core.vision.testcode.VisionTest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class VisionElementTest : VisionTest() {

    @Test
    fun shapeText() {

        scenario {
            case(1) {
                condition {
                    it.tap("Network & internet")
                }.action {
                    v1 = it.detect("Airplane mode", looseMatch = true)
                    v2 = v1.shapeText()
                    v3 = v1.shapeText("Airplane")
                    v4 = v1.shapeText("mode")
                }.expectation {
                    v1.textIs("# Airplane mode")
                    v2.textIs("Airplane mode")
                    v3.textIs("Airplane")
                    v4.textIs("mode")
                }
            }
            case(2) {
                action {
                    v1 = it.detect("Airplane mode")
                    v2 = v1.shapeText()
                    v3 = v1.shapeText("Airplane")
                    v4 = v1.shapeText("mode")
                }.expectation {
                    v1.textIs("Airplane mode")
                    v2.textIs("Airplane mode")
                    v3.textIs("Airplane")
                    v4.textIs("mode")
                }
            }
        }
    }
}