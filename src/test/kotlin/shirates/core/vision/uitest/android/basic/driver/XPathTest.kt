package shirates.core.vision.uitest.android.basic.driver

import shirates.core.configuration.Testrun
import shirates.core.driver.EnableCache
import shirates.core.testcode.Want
import shirates.core.vision.testcode.VisionTest

@EnableCache
@Want
@Testrun("unitTestData/testConfig/androidSettings/androidSettingsConfig.testrun.properties")
class XPathTest : VisionTest() {

    /**
     * xpath is not supported in vision mode
     */
//    @Test
//    @Order(10)
//    fun selectAndFindByXpath() {
//
//        scenario {
//            case(1) {
//                // Arrange, Act
//                it.restartApp()
//                val selectedElement = it.detect("xpath=//*[@text='Network & internet']")
//                val e = TestElementCache.select("xpath=//*[@text='Network & internet']", throwsException = false)
//                // Assert
//                assertThat(e).isEqualTo(selectedElement)
//                e.textIs(selectedElement.text)
//            }
//
//            case(2) {
//                // Arrange, Act
//                val xpath = "/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout"
//                val selectedElement = it.detect("xpath=$xpath")
//                val e = TestElementCache.select("xpath=$xpath")
//                // Assert
//                assertThat(e).isEqualTo(selectedElement)
//                e.textIs(selectedElement.text)
//            }
//        }
//    }

}