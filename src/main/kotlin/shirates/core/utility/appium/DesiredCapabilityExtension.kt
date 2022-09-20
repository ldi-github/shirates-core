package shirates.core.utility.appium

import org.openqa.selenium.remote.DesiredCapabilities

/**
 * setCapabilityStrict
 */
fun DesiredCapabilities.setCapabilityStrict(capabilityName: String, value: Any?): DesiredCapabilities {

    if (capabilityName.startsWith("appium:")) {
        this.setCapability(capabilityName, value)
        return this
    }

    val strictName = "appium:$capabilityName"
    this.setCapability(strictName, value)

    return this
}