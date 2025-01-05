package shirates.core.testcode

import shirates.core.driver.TestDrive
import shirates.core.driver.TestElement

/**
 * UITest
 */
abstract class UITest : UITestBase(), TestDrive {

    var e1 = TestElement()
    var e2 = TestElement()
    var e3 = TestElement()

}