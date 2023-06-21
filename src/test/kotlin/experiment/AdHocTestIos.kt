package experiment

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.openqa.selenium.By
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.getUniqueXpath
import shirates.core.driver.commandextension.screenIs
import shirates.core.driver.commandextension.select
import shirates.core.testcode.UITest

@Testrun("testConfig/ios/iOSSettings/testrun.properties")
class AdHocTestIos : UITest() {

    @Test
    @Order(10)
    fun someTest() {

        scenario {
            case(1) {
                condition {
                    it.screenIs("[iOS Settings Top Screen]")
                }.action {
                    it.select("General")
                    val webElements =
//                        driver.appiumDriver.findElements(AppiumBy.iOSNsPredicateString("type=='XCUIElementTypeStaticText' AND label=='General' AND rect.x==81"))
                        driver.appiumDriver.findElements(By.xpath(it.getUniqueXpath()))
                    println()
                }.expectation {
                }
            }
        }
    }

}