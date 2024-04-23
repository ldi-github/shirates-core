package shirates.core.utility.android

class EmulatorProfile(
    val profileName: String,
    avdName: String? = null,
    val emulatorOptions: MutableList<String> = mutableListOf(),
    val emulatorPort: Int? = null
) {
    val avdName: String =
        if (avdName.isNullOrBlank()) AndroidDeviceUtility.getAvdName(profileName = profileName)
        else avdName
    val platformVersion: String

    init {
        val tokens = profileName.replace("(", " ").replace(")", " ").replace("_", " ").split(" ")
        val versionIx = tokens.map { it.lowercase() }.indexOf("android") + 1
        if (versionIx + 1 <= tokens.count()) {
            platformVersion = tokens[versionIx].toIntOrNull()?.toString() ?: ""
        } else {
            platformVersion = ""
        }
    }

    /**
     * getCommandArgs
     */
    fun getCommandArgs(): List<String> {

        val args = mutableListOf<String>()
        args.add("emulator")
        args.add("@$avdName")
        if (emulatorPort != null) {
            args.add("-port")
            args.add("$emulatorPort")
        }
        args.addAll(emulatorOptions)

        return args
    }
}
