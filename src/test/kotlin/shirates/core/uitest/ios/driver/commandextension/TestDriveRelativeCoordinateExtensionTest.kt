package shirates.core.uitest.ios.driver.commandextension

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest
import shirates.core.testcode.Want

@Want
@Testrun("testConfig/ios/iOSSettings/testrun.properties")
class TestDriveRelativeCoordinateExtensionTest : UITest() {

    @Test
    fun image() {

        scenario {
            case(1) {
                condition {
                    e1 = it.select("Screen Time", useCache = true)
                    e2 = it.select("Screen Time", useCache = false)
                }.expectation {
                    val i1 = e1.leftImage()
                    val i2 = e2.leftImage()
                    i1.type.thisIs("XCUIElementTypeImage")
                    i1.thisIs(i2)
                }
            }
            case(2) {
                condition {
                    e1 = it.selectWithScrollDown("Game Center")
                    e2 = it.selectWithScrollDown("Game Center")
                }.expectation {
                    val i1 = e1.leftImage()
                    val i2 = e2.leftImage()
                    i1.type.thisIs("XCUIElementTypeImage")
                    i1.thisIs(i2)
                }
            }
            case(3) {
                condition {
                    it.selectWithScrollDown("Game Center")
                    e1 = it.select("Siri & Search")
                    e2 = it.select("Game Center")
                }.expectation {
                    val i1 = e1.leftImage().belowImage()
                    val i2 = e2.leftImage().aboveImage()
                    i1.thisIs(i2)
                }
            }
        }
    }

}
