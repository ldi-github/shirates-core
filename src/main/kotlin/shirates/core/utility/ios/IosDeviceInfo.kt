package shirates.core.utility.ios

class IosDeviceInfo(val platformVersion: String, val line: String) {
    var devicename: String
    var udid: String
    var status: String

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

    init {

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
    }

    override fun toString(): String {
        return "$devicename ($platformVersion) ($udid)"
    }
}