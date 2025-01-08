package shirates.core.uitest.ios.driver.commandextension

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest

@Testrun("unitTestConfig/ios/iOSSettings/testrun.properties")
class TestElementCellExtensionTest : UITest() {

    @Test
    fun cellOfTest() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                        .tap("Screen Time")
                }.expectation {
                    it.cellOf("Screen Distance") {
                        exist("Reduce eye strain")
                    }
                    it.cellOf("Communication Safety") {
                        exist("Protect from sensitive content")
                    }
                }
            }
            case(2) {
                expectation {
                    it.cellOf("Screen Distance") {
                        innerWidget(1).type.thisIs("XCUIElementTypeImage")
                        innerWidget(2).textIs("Screen Distance")
                        innerWidget(3).textIs("Reduce eye strain")
                        innerWidget(4).textIs("chevron")
                    }
                    it.cellOf("Communication Safety") {
                        innerWidget(1).type.thisIs("XCUIElementTypeImage")
                        innerWidget(2).textIs("Communication Safety")
                        innerWidget(3).textIs("Protect from sensitive content")
                        innerWidget(4).textIs("chevron")
                    }
                }
            }
        }
    }

}