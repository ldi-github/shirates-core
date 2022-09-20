package shirates.core.utility

fun <T> Map<String, T>?.getInstance(key: String): T {

    if (this == null || this.containsKey(key).not())
        throw IllegalArgumentException("key not found. (key=$key)")

    return this.getInstanceOrNull(key) ?: throw NullPointerException("value not found. (key=$key)")
}

fun <T> Map<String, T?>?.getInstanceOrNull(key: String): T? {

    if (this == null)
        return null
    if (this.containsKey(key).not())
        return null
    return this[key]
}

fun <T> Map<String, T?>?.getStringOrEmpty(key: String): String {

    return this.getInstanceOrNull(key)?.toString() ?: ""
}