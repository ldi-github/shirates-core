package shirates.core.driver

import org.openqa.selenium.Capabilities
import shirates.core.configuration.repository.ParameterRepository
import shirates.core.driver.commandextension.getScrollableElement
import shirates.core.driver.commandextension.getThisOrRootElement
import shirates.core.exception.TestDriverException
import shirates.core.utility.getCapabilityRelaxed

/**
 * parameters
 */
val TestDrive.parameters: Map<String, String>
    get() {
        return ParameterRepository.parameters
    }

/**
 * parameter
 */
fun TestDrive.parameter(name: String): String {
    if (parameters.containsKey(name)) {
        return parameters[name]!!
    }
    if (parameters.containsKey("appium:$name")) {
        return parameters["appium:$name"]!!
    }

    return ""
}

/**
 * viewport
 */
val TestDrive.viewport: Bounds
    get() {
        return rootBounds
    }

/**
 * capabilities
 */
val TestDrive.capabilities: Capabilities
    get() {
        if (TestDriver.isInitialized.not()) {
            throw TestDriverException("Failed to get capabilities. TestDriver is not initialized.")
        }
        return TestDriver.capabilities
    }

/**
 * capabilityRelaxed
 */
fun TestDrive.capabilityRelaxed(capabilityName: String): String {

    return capabilities.getCapabilityRelaxed(capabilityName = capabilityName)
}

/**
 * deviceManufacturer
 */
val TestDrive.deviceManufacturer: String
    get() {
        return parameter("deviceManufacturer")
    }

/**
 * deviceModel
 */
val TestDrive.deviceModel: String
    get() {
        return parameter("deviceModel")
    }

/**
 * deviceName
 */
val TestDrive.deviceName: String
    get() {
        return parameter("deviceName")
    }

/**
 * isEmulator
 */
val TestDrive.isEmulator: Boolean
    get() {
        return TestMode.isEmulator
    }

/**
 * isSimulator
 */
val TestDrive.isSimulator: Boolean
    get() {
        return TestMode.isSimulator
    }

/**
 * isVirtualDevice
 */
val TestDrive.isVirtualDevice: Boolean
    get() {
        return TestMode.isVirtualDevice
    }

/**
 * isRealDevice
 */
val TestDrive.isRealDevice: Boolean
    get() {
        return TestMode.isRealDevice
    }

/**
 * isStub
 */
val TestDrive.isStub: Boolean
    get() {
        return TestMode.isStub
    }

/**
 * platformName
 * (Android or iOS)
 */
val TestDrive.platformName: String
    get() {
        return testContext.profile.platformName
    }

/**
 * platformVersion
 * (os version)
 */
val TestDrive.platformVersion: String
    get() {
        if (TestDriver.isInitialized) {
            return TestDriver.appiumDriver.capabilities.getCapabilityRelaxed("platformVersion")
        }
        return testContext.profile.platformVersion
    }

/**
 * appIconName
 */
val TestDrive.appIconName: String
    get() {
        return testContext.appIconName
    }

/**
 * scrollFrame
 */
val TestDrive.scrollFrame: TestElement
    get() {
        val testElement = getThisOrRootElement()
        TestDriver.lastElement = testElement.getScrollableElement()
        return TestDriver.lastElement
    }

