package shirates.core.configuration

class ProfileNameParser(val testProfileName: String) {

    var platformName = ""
    var model = ""
    var osVersion = ""
    var udid = ""

    init {
        parse()
    }

    private fun parse() {

        // platformName
        val pn = testProfileName.lowercase()
        if (pn.contains("ios") || pn.contains("iphone") || pn.contains("ipad")) {
            platformName = "iOS"
        } else {
            platformName = "Android"
        }

        var text = testProfileName
        run {
            val regex = "[0-9a-f]{40}".toRegex()
            val match = regex.find(text)
            if (match != null) {
                /**
                 * udid (iOS Real Device)
                 */
                platformName = "iOS"
                udid = match.value
            }
        }
        if (udid.isBlank()) {
            val regex = "[0-9A-F]{8}-[0-9A-F]{4}-[0-9A-F]{4}-[0-9A-F]{4}-[0-9A-F]{12}".toRegex()
            val match = regex.find(text)
            if (match != null) {
                /**
                 * udid (iOS Simulator)
                 */
                platformName = "iOS"
                udid = match.value
            }
        }

        if (udid.isNotBlank() && testProfileName != udid) {
            text = testProfileName.substring(0, testProfileName.indexOf(udid) - 1)
        }

        val parenthesisStartIndex = text.lastIndexOf("(")
        if (parenthesisStartIndex != -1) {
            /**
             * model(osVersion)
             * model(iOS osVersion)
             * model(Android osVersion)
             */
            model = text.substring(0, parenthesisStartIndex)
            val osVersionPart = text.substring(parenthesisStartIndex)
            val match = Regex("([0-9.]+)").find(osVersionPart)
            if (match != null) {
                osVersion = match.value
            }
            return
        }
        if (pn.startsWith("android")) {
            /**
             * Android osVersion
             */
            osVersion = pn.replace("*", "").replace("android", "").replace(" ", "").replace("(", "").replace(")", "")
            return
        }
        if (pn.startsWith("ios") || pn.startsWith("ipad")) {
            /**
             * iOS osVersion
             */
            osVersion = pn.replace("*", "").replace("ios", "").replace("ipad", "").replace(" ", "").replace("(", "")
                .replace(")", "")
            return
        }
        if (pn.startsWith("emulator-")) {
            /**
             * emulator-****
             */
            platformName = "Android"
            udid = text
            return
        }
        val v = pn.replace(".", "").toDoubleOrNull()
        if (v != null) {
            /**
             * osVersion
             */
            platformName = ""
            osVersion = pn
            return
        }

        /**
         * udid (Android real device)
         */
        if (platformName == "Android") {
            udid = text
        }
    }
}