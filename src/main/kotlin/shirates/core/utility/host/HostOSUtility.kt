package shirates.core.utility.host

object HostOSUtility {

    val OS_NAME = System.getProperty("os.name").lowercase()

    val isMacOS: Boolean
        get() {
            return OS_NAME.startsWith("mac")
        }

    val isWindows: Boolean
        get() {
            return OS_NAME.startsWith("windows")
        }

    val isLinux: Boolean
        get() {
            return OS_NAME.startsWith("linux")
        }

    val isArm64: Boolean
        get() {
            if (isMacOS) {
                return MacUtility.isArm64
            }
            if (isLinux) {
                return LinuxUtility.isArm64
            }

            return WindowsUtility.isArm64
        }

    val isIntel: Boolean
        get() {
            if (isMacOS) {
                return MacUtility.isIntel
            }
            if (isLinux) {
                return LinuxUtility.isIntel
            }

            return WindowsUtility.isIntel
        }
}