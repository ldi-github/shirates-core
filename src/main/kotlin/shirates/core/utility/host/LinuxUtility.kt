package shirates.core.utility.host

import shirates.core.utility.misc.ShellUtility

object LinuxUtility {

    private var _processorArchitecture = ""

    /**
     * processorArchitecture
     */
    val processorArchitecture: String
        get() {
            if (_processorArchitecture.isBlank()) {
                _processorArchitecture = ShellUtility.executeCommandSilently("uname", "-m").resultString.trim()
            }
            return _processorArchitecture
        }

    /**
     * isArm64
     */
    val isArm64: Boolean
        get() {
            val a = processorArchitecture.lowercase()
            return a.contains("arm")
        }

    /**
     * isIntel
     */
    val isIntel: Boolean
        get() {
            return isArm64.not()
        }
}