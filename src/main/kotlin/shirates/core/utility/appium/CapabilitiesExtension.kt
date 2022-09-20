package shirates.core.utility

import org.openqa.selenium.Capabilities
import shirates.core.driver.TestMode.isAndroid

/**
 * getCapabilityRelaxed
 */
fun Capabilities?.getCapabilityRelaxed(capabilityName: String): String {

    if (this == null)
        return ""

    if (this.capabilityNames.contains(capabilityName)) {
        return getCapability(capabilityName).toString()
    }
    if (this.capabilityNames.contains("appium:$capabilityName")) {
        return getCapability("appium:$capabilityName").toString()
    }

    return ""
}

/**
 * getUdid
 */
fun Capabilities?.getUdid(): String {

    return if (isAndroid) getCapabilityRelaxed("deviceUDID")
    else getCapabilityRelaxed("udid")
}

