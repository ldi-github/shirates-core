package shirates.core.utility

import org.json.JSONArray
import org.json.JSONObject
import java.math.BigDecimal

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

fun JSONObject.getIntOrNull(key: String): Int? {

    if (this.has(key).not()) {
        return null
    }
    val value = this.get(key)
    if (value is Int) {
        return value
    }

    return null
}

fun JSONObject.getFloatOrNull(key: String): Float? {

    if (this.has(key).not()) {
        return null
    }
    val value = this.get(key)
    if (value is Float) {
        return value
    }
    if (value is Int) {
        return value.toFloat()
    }
    if (value is BigDecimal) {
        return value.toFloat()
    }

    return null
}

fun JSONObject.getDoubleOrNull(key: String): Double? {

    if (this.has(key).not()) {
        return null
    }
    val value = this.get(key)
    if (value is Double) {
        return value
    }
    if (value is Int) {
        return value.toDouble()
    }
    if (value is BigDecimal) {
        return value.toDouble()
    }

    return null
}

fun JSONObject.getJSONArrayOrEmpty(key: String): JSONArray {

    if (this.has(key).not()) {
        return JSONArray()
    }
    return this.getJSONArray(key)
}