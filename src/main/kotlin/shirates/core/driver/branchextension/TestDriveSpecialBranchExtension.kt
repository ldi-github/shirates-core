package shirates.core.driver.branchextension

import shirates.core.driver.*
import shirates.core.driver.TestMode.hasOsaifuKeitai
import shirates.core.driver.branchextension.result.BooleanCompareResult
import shirates.core.driver.branchextension.result.SpecialTagCompareResult
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog

/**
 * specialTag
 */
fun TestDrive.specialTag(
    specialTag: String,
    onTrue: () -> Unit
): SpecialTagCompareResult {

    val result = SpecialTagCompareResult()
    result.specialTag(specialTag = specialTag, onTrue = onTrue)

    return result
}

/**
 * stub
 */
fun TestDrive.stub(
    onTrue: () -> Unit
): BooleanCompareResult {

    val command = "stub"

    val match = isStub || TestMode.isNoLoadRun
    if (match.not()) {
        TestLog.trace("skip stub")
        return BooleanCompareResult(value = false, command = command)
    }

    val context = TestDriverCommandContext(null)
    context.execSpecial(subject = command, condition = command) {
        onTrue()
    }

    return BooleanCompareResult(value = true, command = command)
}

/**
 * stubNot
 */
fun TestDrive.stubNot(
    onTrue: () -> Unit
): BooleanCompareResult {

    val command = "stubNot"

    val match = isStub.not() || TestMode.isNoLoadRun
    if (match.not()) {
        TestLog.trace("skip stubNot")
        return BooleanCompareResult(value = false, command = command)
    }

    val context = TestDriverCommandContext(null)
    context.execSpecial(subject = command, condition = command) {
        onTrue()
    }

    return BooleanCompareResult(value = true, command = command)
}

/**
 * testRuntimeOnly
 */
fun TestDrive.testRuntimeOnly(
    onTrue: () -> Unit
): BooleanCompareResult {

    val command = "testRuntimeOnly"
    val isRuntime = TestMode.isNoLoadRun.not()
    val result = BooleanCompareResult(value = isRuntime, command = command)
    result.setExecuted(condition = "true", matched = result.value, message = "testRuntimeOnly")
    if (result.value) {
        onTrue.invoke()
    }

    return result
}

/**
 * android
 */
fun TestDrive.android(
    onTrue: () -> Unit
): TestDrive {

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
fun TestDrive.ios(
    onTrue: () -> Unit
): TestDrive {

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
fun TestDrive.emulator(
    onTrue: () -> Unit
): TestDrive {

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
fun TestDrive.simulator(
    onTrue: () -> Unit
): TestDrive {

    val command = "simulator"

    val match = (TestMode.isNoLoadRun || isSimulator)
    if (match.not()) {
        TestLog.trace("skip simulator")
        return this
    }

    val context = TestDriverCommandContext(null)
    context.execSpecial(subject = command, condition = command) {
        onTrue()
    }
    return this
}


/**
 * virtualDevice
 */
fun TestDrive.virtualDevice(
    onTrue: () -> Unit
): TestDrive {

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
fun TestDrive.realDevice(
    onTrue: () -> Unit
): TestDrive {

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
fun TestDrive.arm64(
    onTrue: () -> Unit
): TestDrive {
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
fun TestDrive.intel(
    onTrue: () -> Unit
): TestDrive {

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
fun TestDrive.osaifuKeitai(
    onTrue: () -> Unit
): TestDrive {
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
fun TestDrive.osaifuKeitaiNot(
    onTrue: () -> Unit
): TestDrive {

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
