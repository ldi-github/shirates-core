package shirates.core.driver

import org.openqa.selenium.Capabilities
import shirates.core.configuration.repository.ParameterRepository
import shirates.core.exception.TestDriverException
import shirates.core.utility.appium.getCapabilityRelaxed

/**
 * parameters
 */
val Drive.parameters: Map<String, String>
    get() {
        return ParameterRepository.parameters
    }

/**
 * parameter
 */
fun Drive.parameter(name: String): String {
    if (parameters.containsKey(name)) {
        return parameters[name]!!
    }
    if (parameters.containsKey("appium:$name")) {
        return parameters["appium:$name"]!!
    }

    return ""
}

/**
 * capabilities
 */
val Drive.capabilities: Capabilities
    get() {
        if (TestDriver.isInitialized.not()) {
            throw TestDriverException("Failed to get capabilities. TestDriver is not initialized.")
        }
        return TestDriver.capabilities
    }

/**
 * capabilityRelaxed
 */
fun Drive.capabilityRelaxed(capabilityName: String): String {

    return capabilities.getCapabilityRelaxed(capabilityName = capabilityName)
}

/**
 * deviceManufacturer
 */
val Drive.deviceManufacturer: String
    get() {
        return parameter("deviceManufacturer")
    }

/**
 * deviceModel
 */
val Drive.deviceModel: String
    get() {
        return parameter("deviceModel")
    }

/**
 * deviceName
 */
val Drive.deviceName: String
    get() {
        return parameter("deviceName")
    }

/**
 * isEmulator
 */
val Drive.isEmulator: Boolean
    get() {
        return TestMode.isEmulator
    }

/**
 * isSimulator
 */
val Drive.isSimulator: Boolean
    get() {
        return TestMode.isSimulator
    }

/**
 * isVirtualDevice
 */
val Drive.isVirtualDevice: Boolean
    get() {
        return TestMode.isVirtualDevice
    }

/**
 * isRealDevice
 */
val Drive.isRealDevice: Boolean
    get() {
        return TestMode.isRealDevice
    }

/**
 * isStub
 */
val Drive.isStub: Boolean
    get() {
        return TestMode.isStub
    }

/**
 * platformName
 * (Android or iOS)
 */
val Drive.platformName: String
    get() {
        return testContext.profile.platformName
    }

/**
 * platformVersion
 * (os version)
 */
val Drive.platformVersion: String
    get() {
        if (TestDriver.isInitialized) {
            return TestDriver.appiumDriver.capabilities.getCapabilityRelaxed("platformVersion")
        }
        return testContext.profile.platformVersion
    }

/**
 * platformMajorVersion
 * (os major version)
 */
val Drive.platformMajorVersion: Int
    get() {
        val tokens = platformVersion.split(".")
        if (tokens.any()) {
            val value = tokens[0].toIntOrNull()
            if (value != null) {
                return value
            }
        }

        return -1
    }

/**
 * appIconName
 */
val Drive.appIconName: String
    get() {
        return testContext.appIconName
    }
