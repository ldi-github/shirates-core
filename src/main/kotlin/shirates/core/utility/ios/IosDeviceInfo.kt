package shirates.core.utility.ios

class IosDeviceInfo(val platformVersion: String, val line: String) {
    lateinit var devicename: String
    lateinit var udid: String
    lateinit var status: String

    val isSimulator: Boolean
        get() {
            if (_isSimulator == null) {
                _isSimulator = IosDeviceUtility.isSimulator(udid)
            }
            return _isSimulator!!
        }
    private var _isSimulator: Boolean? = null

    val isRealDevice: Boolean
        get() {
            return isSimulator.not()
        }

    var message = ""
    var modelSortKey: Double? = null

    init {
        parse(line = line)
    }

    private fun parse(line: String) {

        if (line.isBlank()) {
            return
        }

        val regex = "[0-9A-F]{8}-[0-9A-F]{4}-[0-9A-F]{4}-[0-9A-F]{4}-[0-9A-F]{12}".toRegex()
        val match = regex.find(line)
        if (match != null) {
            udid = match.value

            val tokens = line.split(udid)
            devicename = tokens.first().trimEnd('(').trim()
            status = tokens.last().trim(' ', '(', ')')
        } else {
            devicename = ""
            udid = ""
            status = ""
        }

        if (devicename.contains("nd generation")) {
            modelSortKey = 0.0
        } else {
            val matchResult = "[0-9]+".toRegex().find(devicename)
            modelSortKey = matchResult?.value?.toDoubleOrNull()
        }
    }

    override fun toString(): String {
        return "$devicename ($platformVersion) ($udid)"
    }
}