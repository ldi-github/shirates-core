package shirates.core.utility.android

class EmulatorProfile(
    val profileName: String,
    val emulatorOptions: MutableList<String> = mutableListOf()
) {
    val avdName: String = AndroidDeviceUtility.getAvdName(profileName = profileName)
    val platformVersion: String

    init {
        val tokens = profileName.replace("(", " ").replace(")", " ").replace("_", " ").split(" ")
        val versionIx = tokens.map { it.lowercase() }.indexOf("android") + 1
        if (versionIx + 1 <= tokens.count()) {
            platformVersion = tokens[versionIx]
        } else {
            platformVersion = ""
        }
    }
}
