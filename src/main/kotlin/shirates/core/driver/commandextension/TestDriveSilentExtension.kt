package shirates.core.driver.commandextension

import shirates.core.driver.TestDrive
import shirates.core.driver.TestDriverCommandContext
import shirates.core.driver.TestElement

/**
 * silent
 */
fun TestDrive.silent(
    proc: () -> Unit
): TestElement {

    val testElement = getTestElement()

    val context = TestDriverCommandContext(testElement)
    context.execSilentCommand() {
        proc()
    }
    return lastElement
}
