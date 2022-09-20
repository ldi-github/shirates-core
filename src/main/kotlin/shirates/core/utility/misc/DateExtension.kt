package shirates.core.utility

import org.apache.commons.lang3.time.DateUtils
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

val Date.calendar: Calendar
    get() {
        val c = Calendar.getInstance()
        c.time = this
        return c
    }

val Date.yearValue: Int
    get() {
        return this.calendar.get(Calendar.YEAR)
    }

val Date.monthValue: Int
    get() {
        return this.calendar.get(Calendar.MONTH) + 1
    }

val Date.dayValue: Int
    get() {
        return this.calendar.get(Calendar.DAY_OF_MONTH)
    }

val Date.hourValue: Int
    get() {
        return this.calendar.get(Calendar.HOUR_OF_DAY)
    }

val Date.minuteValue: Int
    get() {
        return this.calendar.get(Calendar.MINUTE)
    }

val Date.secondValue: Int
    get() {
        return this.calendar.get(Calendar.SECOND)
    }

val Date.millisecondValue: Int
    get() {
        return this.calendar.get(Calendar.MILLISECOND)
    }

val Date.dayOfWeek: Int
    get() {
        return this.calendar.get(Calendar.DAY_OF_WEEK)
    }

fun Date.format(pattern: String? = null, tz: String? = null): String {

    var sdfFormat = (
            if (pattern.isNullOrBlank().not())
                pattern!!
            else if (this.millisecondValue != 0)
                "yyyy/MM/dd HH:mm:ss.SSS"
            else if (this.hourValue != 0 || this.minuteValue != 0 || this.secondValue != 0)
                "yyyy/MM/dd HH:mm:ss"
            else
                "yyyy/MM/dd"
            )
    if (tz != null) {
        when (tz) {
            "z" -> sdfFormat += " z"
            else -> sdfFormat += tz
        }
    }

    try {
        val sdf = SimpleDateFormat(sdfFormat)
        return sdf.format(this)
    } catch (t: Throwable) {
        throw IllegalArgumentException("Format error. (date=$this, pattern=$pattern)")
    }
}

fun Date.addYears(years: Int?): Date {

    years ?: return this
    return DateUtils.addYears(this, years)
}

fun Date.addMonths(months: Int?): Date {

    months ?: return this
    return DateUtils.addMonths(this, months)
}

fun Date.addDays(days: Int?): Date {

    days ?: return this
    return DateUtils.addDays(this, days)
}

fun Date.addHours(hours: Int?): Date {

    hours ?: return this
    return DateUtils.addHours(this, hours)
}

fun Date.addMinutes(minutes: Int?): Date {

    minutes ?: return this
    return DateUtils.addMinutes(this, minutes)
}

fun Date.addSeconds(seconds: Int?): Date {

    seconds ?: return this
    return DateUtils.addSeconds(this, seconds)
}

fun Date.addMilliseconds(milliseconds: Int?): Date {

    milliseconds ?: return this
    return DateUtils.addMilliseconds(this, milliseconds)
}

fun Date.toLocalDateTime(): LocalDateTime {

    return LocalDateTime.of(
        this.yearValue,
        this.monthValue,
        this.dayValue,
        this.hourValue,
        this.minuteValue,
        this.secondValue,
        this.millisecondValue * 1000000     // nano
    )
}

fun Date.toLocalDate(): LocalDate {

    return LocalDate.of(
        this.yearValue,
        this.monthValue,
        this.dayValue
    )
}
