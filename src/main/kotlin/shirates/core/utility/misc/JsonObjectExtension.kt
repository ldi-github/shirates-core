package shirates.core.utility

import org.json.JSONArray
import org.json.JSONObject

fun JSONObject.getStringOrNull(key: String): String? {

    if (this.has(key).not()) {
        return null
    }
    val value = this.get(key)
    if (value is String) {
        return value.toString()
    }

    return null
}

fun JSONObject.getJSONArrayOrEmpty(key: String): JSONArray {

    if (this.has(key).not()) {
        return JSONArray()
    }
    return this.getJSONArray(key)
}