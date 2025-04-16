package shirates.core.vision.driver.branchextension

import shirates.core.driver.TestDriverCommandContext
import shirates.core.driver.TestMode
import shirates.core.driver.TestMode.hasOsaifuKeitai
import shirates.core.driver.TestMode.isEmulator
import shirates.core.driver.TestMode.isRealDevice
import shirates.core.driver.TestMode.isSimulator
import shirates.core.driver.TestMode.isStub
import shirates.core.driver.TestMode.isVirtualDevice
import shirates.core.driver.testContext
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.vision.VisionDrive
import shirates.core.vision.driver.branchextension.result.VisionDriveBooleanCompareResult
import shirates.core.vision.driver.branchextension.result.VisionDriveSpecialTagCompareResult


/**
 * specialTag
 */
fun VisionDrive.specialTag(
    specialTag: String,
    onTrue: () -> Unit
): VisionDriveSpecialTagCompareResult {

    val result = VisionDriveSpecialTagCompareResult()
    result.specialTag(specialTag = specialTag, onTrue = onTrue)

    return result
}

/**
 * stub
 */
fun VisionDrive.stub(
    onTrue: () -> Unit
): VisionDriveBooleanCompareResult {

    val command = "stub"

    val match = isStub || TestMode.isNoLoadRun
    if (match.not()) {
        TestLog.trace("skip stub")
        return VisionDriveBooleanCompareResult(value = false, command = command)
    }

    val context = TestDriverCommandContext(null)
    context.execSpecial(subject = command, condition = command) {
        onTrue()
    }

    return VisionDriveBooleanCompareResult(value = true, command = command)
}

/**
 * stubNot
 */
fun VisionDrive.stubNot(
    onTrue: () -> Unit
): VisionDriveBooleanCompareResult {

    val command = "stubNot"

    val match = isStub.not() || TestMode.isNoLoadRun
    if (match.not()) {
        TestLog.trace("skip stubNot")
        return VisionDriveBooleanCompareResult(value = false, command = command)
    }

    val context = TestDriverCommandContext(null)
    context.execSpecial(subject = command, condition = command) {
        onTrue()
    }

    return VisionDriveBooleanCompareResult(value = true, command = command)
}

/**
 * testRuntimeOnly
 */
fun VisionDrive.testRuntimeOnly(
    onTrue: () -> Unit
): VisionDriveBooleanCompareResult {

    val command = "testRuntimeOnly"
    val isRuntime = TestMode.isNoLoadRun.not()
    val result = VisionDriveBooleanCompareResult(value = isRuntime, command = command)
    result.setExecuted(condition = "true", matched = result.value, message = "testRuntimeOnly")
    if (result.value) {
        onTrue.invoke()
    }

    return result
}

/**
 * android
 */
fun VisionDrive.android(
    onTrue: () -> Unit
): VisionDrive {

    val match = (TestMode.isAndroid || TestMode.isNoLoadRun)
    if (match.not()) {
        TestLog.trace("skip android")
        return this
    }

    val context = TestDriverCommandContext(null)
    context.execOS("Android") {
        onTrue()
    }
    return this
}

/**
 * ios
 */
fun VisionDrive.ios(
    onTrue: () -> Unit
): VisionDrive {

    val match = (TestMode.isiOS || TestMode.isNoLoadRun)
    if (match.not()) {
        TestLog.trace("skip ios")
        return this
    }

    val context = TestDriverCommandContext(null)
    context.execOS("iOS") {
        onTrue()
    }
    return this
}

/**
 * emulator
 */
fun VisionDrive.emulator(
    onTrue: () -> Unit
): VisionDrive {

    val command = "emulator"

    val match = (TestMode.isNoLoadRun || isEmulator)
    if (match.not()) {
        TestLog.trace("skip emulator")
        return this
    }

    val context = TestDriverCommandContext(null)
    context.execSpecial(subject = command, condition = command) {
        onTrue()
    }
    return this
}

/**
 * simulator
 */
fun VisionDrive.simulator(
    onTrue: () -> Unit
): VisionDrive {

    val command = "simulator"

    val match = (TestMode.isNoLoadRun || isSimulator)
    if (match.not()) {
        TestLog.trace("skip simulator")
        return this
    }

    val context = TestDriverCommandContext(null)
    context.execSpecial(subject = command, condition = command) {
        val original = testContext.screenshotWithSimctl
        testContext.screenshotWithSimctl = true
        try {
            onTrue()
        } finally {
            testContext.screenshotWithSimctl = original
        }
    }
    return this
}


/**
 * virtualDevice
 */
fun VisionDrive.virtualDevice(
    onTrue: () -> Unit
): VisionDrive {

    val command = "virtualDevice"

    val match = (TestMode.isNoLoadRun || isVirtualDevice)
    if (match.not()) {
        TestLog.trace("skip virtualDevice")
        return this
    }

    val context = TestDriverCommandContext(null)
    context.execSpecial(subject = command, condition = command) {
        onTrue()
    }
    return this
}

/**
 * realDevice
 */
fun VisionDrive.realDevice(
    onTrue: () -> Unit
): VisionDrive {

    val command = "realDevice"

    val match = isRealDevice || TestMode.isNoLoadRun
    if (match.not()) {
        TestLog.trace("skip realDevice")
        return this
    }

    val context = TestDriverCommandContext(null)
    context.execSpecial(subject = command, condition = command) {
        onTrue()
    }
    return this
}

/**
 * arm64
 */
fun VisionDrive.arm64(
    onTrue: () -> Unit
): VisionDrive {
    val command = "arm64"

    val match = TestMode.isArm64 || TestMode.isNoLoadRun
    if (match.not()) {
        TestLog.trace("skip arm64")
        return this
    }

    val context = TestDriverCommandContext(null)
    context.execSpecial(subject = command, condition = "arm64") {
        onTrue()
    }
    return this
}

/**
 * intel
 */
fun VisionDrive.intel(
    onTrue: () -> Unit
): VisionDrive {

    val command = "intel"

    val match = TestMode.isIntel || TestMode.isNoLoadRun
    if (match.not()) {
        TestLog.trace("skip intel")
        return this
    }

    val context = TestDriverCommandContext(null)
    context.execSpecial(subject = command, condition = "intel") {
        onTrue()
    }
    return this
}

/**
 * osaifuKeitai
 */
fun VisionDrive.osaifuKeitai(
    onTrue: () -> Unit
): VisionDrive {
    val command = "osaifuKeitai"

    val match = hasOsaifuKeitai || TestMode.isNoLoadRun
    if (match.not()) {
        TestLog.trace("skip osaifuKeitai")
        return this
    }

    val context = TestDriverCommandContext(null)
    context.execSpecial(subject = command, condition = message(id = command)) {
        onTrue()
    }
    return this
}

/**
 * osaifuKeitaiNot
 */
fun VisionDrive.osaifuKeitaiNot(
    onTrue: () -> Unit
): VisionDrive {

    val command = "osaifuKeitaiNot"

    val match = hasOsaifuKeitai.not() || TestMode.isNoLoadRun
    if (match.not()) {
        TestLog.trace("skip osaifuKeitaiNot")
        return this
    }

    val context = TestDriverCommandContext(null)
    context.execSpecial(subject = command, condition = message(id = command)) {
        onTrue()
    }
    return this
}
