package shirates.core.logging

import shirates.core.driver.TestElement

/**
 * ScanRecord
 */
class ScanRecord(
    val lineNo: Int,
    val sourceXml: String,
    val element: TestElement
) {
}