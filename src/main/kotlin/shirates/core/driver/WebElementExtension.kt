package shirates.core.driver

import org.openqa.selenium.WebElement
import shirates.core.configuration.Selector

fun WebElement.toTestElement(selector: Selector? = null): TestElement {

    return TestElement(selector = selector, webElement = this)
}
