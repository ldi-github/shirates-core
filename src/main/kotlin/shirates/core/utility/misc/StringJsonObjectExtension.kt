package shirates.core.utility.misc

import org.json.JSONArray
import org.json.JSONObject

fun String?.toJSONObject(): JSONObject {

    return JSONObject(this ?: "{}")
}

fun String?.toJSONArray(): JSONArray {

    return JSONArray(this ?: "[]")
}