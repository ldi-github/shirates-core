package shirates.core.utility.appium

import org.openqa.selenium.Capabilities
import org.openqa.selenium.MutableCapabilities

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
 * setCapabilityStrict
 */
fun MutableCapabilities.setCapabilityStrict(capabilityName: String, value: Any?): MutableCapabilities {

    if (capabilityName == "platformName") {
        this.setCapability(capabilityName, value)
        return this
    }
    if (capabilityName.startsWith("appium:")) {
        this.setCapability(capabilityName, value)
        return this
    }
    this.setCapability("appium:$capabilityName", value)
    return this
}

/**
 * getMap
 */
fun Capabilities?.getMap(): Map<String, Any?> {

    if (this == null) {
        return mapOf()
    }
    val map = mutableMapOf<String, Any?>()
    for (capabilitiName in this.capabilityNames) {
        map[capabilitiName] = this.getCapability(capabilitiName)
    }
    return map
}