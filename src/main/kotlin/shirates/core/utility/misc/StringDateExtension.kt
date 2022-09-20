package shirates.core.utility

import java.text.SimpleDateFormat
import java.time.ZoneId
import java.util.*

/**
 * toDate
 */
fun String.toDate(pattern: String? = null, tz: String? = null, strict: Boolean = true): Date {

    val tokens = this.replace("-", "|-").replace("+", "|+").split("|")
    val datePart = tokens[0]
    val zonePart = if (tokens.count() == 2) tokens[1] else ""
    val zonePattern =
        if (tz != null) tz
        else if (tokens.count() == 2) "X"
        else ""

    val sdfPattern = pattern
        ?: (when (datePart.length) {
            8 -> "yyyyMMdd"
            10 -> "yyyy/MM/dd"
            14 -> "yyyyMMddHHmmss"
            17 -> "yyyyMMddHHmmssSSS"
            19 -> "yyyy/MM/dd HH:mm:ss"
            23 -> "yyyy/MM/dd HH:mm:ss.SSS"
            else -> "yyyy/MM/dd"
        } + zonePattern)
    try {
        val sdf = SimpleDateFormat(sdfPattern)
        sdf.isLenient = false
        if (zonePart.isNotBlank()) {
            sdf.timeZone = TimeZone.getTimeZone(ZoneId.of(zonePart))
        }
        val date = sdf.parse(this)

        if (strict) {
            val reformat =
                sdf.format(date).replace("Z", "").replace(zonePart, "").replace(zonePart.replace("00", ""), "")
            val thisWithoutZonePart = this.replace(zonePart, "")
            if (thisWithoutZonePart != reformat) {
                throw IllegalArgumentException("strict=$strict")
            }
        }
        return date
    } catch (t: Throwable) {
        throw IllegalArgumentException("Invalid date format.(this=$this, pattern=$pattern)", t)
    }
}

/**
 * toDateOrNull
 *
 * length -> format
 * 8 -> "yyyyMMdd"
 * 10 -> "yyyy/MM/dd"
 * 14 -> "yyyyMMddHHmmss"
 * 17 -> "yyyyMMddHHmmssSSS"
 * 19 -> "yyyy/MM/dd HH:mm:ss"
 * 23 -> "yyyy/MM/dd HH:mm:ss.SSS"
 * else -> "yyyy/MM/dd"
 */
fun String.toDateOrNull(pattern: String? = null): Date? {

    try {
        return this.toDate(pattern = pattern)
    } catch (t: Throwable) {
        return null
    }
}