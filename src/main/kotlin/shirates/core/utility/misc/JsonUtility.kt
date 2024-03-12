package shirates.core.utility.misc

import org.json.JSONObject
import shirates.core.exception.TestConfigException
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.utility.toPath

/**
 * JsonUtility
 */
object JsonUtility {

    /**
     * getJSONObjectFromFile
     */
    fun getJSONObjectFromFile(file: String): JSONObject {

        val data: String
        try {
            data = file.toPath().toFile().readText()
        } catch (t: Throwable) {
            val ex = TestConfigException(message(id = "fileNotFound", file = file), t)
            TestLog.error(ex)
            throw ex
        }

        try {
            val jso = JSONObject(data)
            return jso
        } catch (t: Throwable) {
            val ex = TestConfigException(
                message(id = "failedToConvertToJSONObject", file = file),
                t
            )
            TestLog.error(ex)
            throw ex
        }
    }

}