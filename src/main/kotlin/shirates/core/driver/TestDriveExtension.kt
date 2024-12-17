package shirates.core.driver

import org.openqa.selenium.By
import org.openqa.selenium.Capabilities
import shirates.core.driver.TestDriver.appiumDriver
import shirates.core.driver.TestMode.isNoLoadRun
import shirates.core.driver.commandextension.getScrollableElement
import shirates.core.driver.commandextension.getThisOrIt
import shirates.core.exception.TestDriverException
import shirates.core.logging.Measure
import shirates.core.utility.appium.getCapabilityRelaxed

/**
 * imageProfile
 */
val TestDrive.imageProfile: String
    get() {
        if (TestDriver.isInitialized.not()) {
            return TestMode.platformAnnotation
        }

        val viewportRect = capabilities.getCapabilityRelaxed("viewportRect")
        if (viewportRect.isNotBlank()) {
            val b = Bounds(viewportRect)
            return "${TestMode.platformAnnotation}_${b.width}x${b.height}"
        }

        val b = viewBounds
        if (b.isEmpty) {
            return TestMode.platformAnnotation
        }

        return "${TestMode.platformAnnotation}_${b.width}x${b.height}"
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
        val testElement = getThisOrIt()
        lastElement = testElement.getScrollableElement()
        return lastElement
    }

/**
 * rootElement
 */
var TestDrive.rootElement: TestElement
    get() {
        if (testContext.useCache || isNoLoadRun) {
            return TestElementCache.rootElement
        }
        val ms = Measure("rootElement")
        TestElementCache.rootElement = appiumDriver.findElement(By.xpath("//*")).toTestElement()
        ms.end()
        return TestElementCache.rootElement
    }
    set(value) {
        TestElementCache.rootElement = value
    }

/**
 * sourceXml
 */
val TestDrive.sourceXml: String
    get() {
        return TestElementCache.sourceXml
    }

