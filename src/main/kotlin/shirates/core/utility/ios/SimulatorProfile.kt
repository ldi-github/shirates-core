package shirates.core.utility.ios

class SimulatorProfile(
    val profileName: String
) {
    val model: String
    val platformVersion: String

    init {

        val ix = profileName.indexOf("(")
        if (ix == -1) {
            model = profileName
            platformVersion = ""
        } else {
            model = profileName.substring(0, ix)

            val regex = ".*\\((.*)\\).*".toRegex()
            val match = regex.find(profileName)
            if (match != null && match.groupValues.count() > 1) {
                val tokens = match.groupValues[1].split(" ")
                platformVersion = tokens.last().trim()
            } else {
                platformVersion = ""
            }
        }

    }

}