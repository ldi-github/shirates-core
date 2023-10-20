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
 * viewportSize
 */
internal fun TestDrive.viewportSize(): String {
    val vp = viewport
    val offset = if (TestMode.isAndroid) -1 else 0
    val width = vp.width + offset
    val height = vp.height + offset
    val viewport = if (vp.isEmpty) "" else "${width}x${height}"
    return viewport
}

/**
 * imageSizeProfile
 */
internal val TestDrive.imageSizeProfile: String
    get() {
        if (TestDriver.isInitialized.not()) {
            return TestMode.platformAnnotation
        }
        val viewport = viewportSize()

        return "${TestMode.platformAnnotation}_${viewport}"
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
 * platformMajorVersion
 * (os major version)
 */
val TestDrive.platformMajorVersion: Int
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

