package shirates.core.utility

import org.apache.logging.log4j.core.parser.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

/**
 * toDate
 */
fun String.toDate(
    pattern: String? = null,
    tz: String? = null,
    locale: String? = null,
    strict: Boolean = true
): Date {

    val tokens = this.replace("-", "|-").replace("+", "|+").split("|")
    val datePart = tokens[0]
    val zonePart = if (tokens.count() == 2) tokens[1] else ""
    val zonePattern =
        tz ?: if (tokens.count() == 2) "X"
        else ""

    val dateParts = datePart.split("/")
    if (dateParts.count() == 3 && dateParts[2].contains(" ").not()) {
        try {
            val year = dateParts[0].toInt()
            val month = dateParts[1].toInt()
            val day = dateParts[2].toInt()
            val localDate = LocalDateTime.of(year, month, day, 0, 0, 0)
            return Date.from(localDate.atZone(ZoneId.systemDefault()).toInstant())
        } catch (t: Throwable) {
            throw ParseException("Could not parse date. ($this)", t)
        }
    }

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
        val sdf =
            if (locale == null) SimpleDateFormat(sdfPattern)
            else SimpleDateFormat(sdfPattern, Locale(locale))
        sdf.isLenient = false
        if (zonePart.isNotBlank()) {
            sdf.timeZone = TimeZone.getTimeZone(ZoneId.of(zonePart))
        }
        val date = sdf.parse(this)

        if (strict) {
            val reformat = sdf.format(date)
            if (this != reformat) {
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