package shirates.core.utility.misc

import com.google.gson.GsonBuilder
import org.json.JSONObject
import shirates.core.exception.TestConfigException
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import java.io.File

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
            data = File(file).readText()
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

    /**
     * toJsonString
     */
    fun toJsonString(obj: Any): String {

        val gson = GsonBuilder().setPrettyPrinting().create()
        val jsonStr = gson.toJson(obj)
        return jsonStr
    }
}