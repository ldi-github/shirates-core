package shirates.core.configuration

class ProfileNameParser(val testProfileName: String) {

    var platformName = ""
    var model = ""
    var platformVersion = ""
    var udid = ""

    init {
        parse()
    }

    private fun getPlatformVersionPart(platformVersionPart: String): String {
        val match = Regex("([0-9.]+)").find(platformVersionPart)
        return match?.value ?: ""
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
             * model(platformVersion)
             * model(iOS platformVersion)
             * model(Android platformVersion)
             */
            model = text.substring(0, parenthesisStartIndex)
            val platformVersionPart = text.substring(parenthesisStartIndex)
            platformVersion = getPlatformVersionPart(platformVersionPart)
            return
        }
        if (pn.startsWith("android")) {
            /**
             * Android platformVersion
             */
            val platformVersionPart =
                pn.replace("*", "").replace("android", "").replace(" ", "").replace("(", "").replace(")", "")
            platformVersion = getPlatformVersionPart(platformVersionPart)
            return
        }
        if (pn.startsWith("ios") || pn.startsWith("ipad")) {
            /**
             * iOS platformVersion
             */
            platformVersion =
                pn.replace("*", "").replace("ios", "").replace("ipad", "").replace(" ", "").replace("(", "")
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
             * platformVersion
             */
            platformName = ""
            platformVersion = pn
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