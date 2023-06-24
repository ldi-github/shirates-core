package experiment

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.openqa.selenium.By
import shirates.core.configuration.Testrun
import shirates.core.driver.DisableCache
import shirates.core.driver.commandextension.select
import shirates.core.logging.printInfo
import shirates.core.testcode.UITest

@Testrun("testConfig/ios/iOSSettings/testrun.properties")
class AdHocTestIos : UITest() {

    @DisableCache
    @Test
    @Order(10)
    fun select() {

        run {
            val a = driver.appiumDriver.findElements(By.xpath("//*[starts-with(@label,'Gene')]"))
            println(a)
        }
        run {
            val a = driver.appiumDriver.findElements(By.xpath("//*[contains(@label,'enera')]"))
            println(a)
        }
        run {
            val a =
                driver.appiumDriver.findElements(By.xpath("//*[normalize-space(substring(@label,string-length(@label)-string-length('eneral')+1))='eneral']"))
            println(a)
        }

        run {
            val e = it.select("General")
            e.printInfo()
        }

        run {
            val e = it.select("*eneral")
            e.printInfo()
        }
    }

}