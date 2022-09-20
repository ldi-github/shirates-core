package shirates.core.utility.host

import shirates.core.driver.TestMode

object WindowsUtility {

    private const val UNKNOWN_ARCHITECTURE = "unknown"
    private var PROCESSOR_ARCHITECTURE = System.getenv().get("PROCESSOR_ARCHITECTURE") ?: UNKNOWN_ARCHITECTURE

    /**
     * processorArchitecture
     */
    val processorArchitecture: String
        get() {
            return PROCESSOR_ARCHITECTURE
        }

    /**
     * isUnknown
     */
    val isUnknown: Boolean
        get() {
            return processorArchitecture == UNKNOWN_ARCHITECTURE
        }

    /**
     * isArm64
     */
    val isArm64: Boolean
        get() {
            if (TestMode.isNoLoadRun) return true

            return processorArchitecture == "ARM64"
        }

    /**
     * isIntel
     */
    val isIntel: Boolean
        get() {
            if (TestMode.isNoLoadRun) return true

            return isUnknown.not() && isArm64.not()
        }
}