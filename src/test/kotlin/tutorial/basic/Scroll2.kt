package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest
import shirates.core.utility.*
import java.text.DateFormatSymbols
import java.util.*

@Testrun("testConfig/android/calendar/testrun.properties")
class Scroll2 : UITest() {

    @Test
    @Order(10)
    fun scrollRight_scrollLeft() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Calendar Week Screen]")
                }.action {
                    it
                        .scrollRight()
                        .scrollLeft()
                }
            }
            case(2) {
                action {
                    it
                        .scrollRight(scrollDurationSeconds = 5.0, startMarginRatio = 0.1)
                        .scrollRight(scrollDurationSeconds = 3.0, startMarginRatio = 0.3)
                        .scrollLeft(scrollDurationSeconds = 5.0, startMarginRatio = 0.1)
                        .scrollLeft(scrollDurationSeconds = 3.0, startMarginRatio = 0.3)
                }
            }
        }
    }

    private fun Date.getLabel(): String {

        val dfs = DateFormatSymbols(Locale.ENGLISH)
        val week = dfs.weekdays.get(this.dayOfWeek)
        val day = "${this.dayValue}".padStart(2, '0')
        val month = dfs.months.get(this.monthValue - 1)
        val label = "$week $day $month ${this.yearValue}"
        return label
    }

    @Test
    @Order(20)
    fun scrollToRightEdge_scrollToLeftEdge() {

        val startDay = Date()
        val startDayLabel = "${startDay.getLabel()}, Open Schedule View"

        val endDay = startDay.addDays(7 * 3)
        val endDayLabel = "${endDay.getLabel()}, Open Schedule View"

        scenario {
            case(1) {
                condition {
                    it.macro("[Calendar Week Screen]")
                }.action {
                    it.scrollToRightEdge(repeat = 1, edgeSelector = "@$endDayLabel")
                }.expectation {
                    it.exist("@$endDayLabel")
                }
            }
            case(2) {
                action {
                    it.scrollToLeftEdge(repeat = 1, edgeSelector = "@$startDayLabel")
                }.expectation {
                    it.exist("@$startDayLabel")
                }
            }
        }
    }

    @Test
    @Order(30)
    fun withScroll() {

        val today = Date()
        val todayLabel = "${today.getLabel()}, Open Schedule View"

        val day1 = today.addDays(7)
        val day1Label = "${day1.getLabel()}, Open Schedule View"

        val day2 = today.addDays(7 * 2)
        val day2Label = "${day2.getLabel()}, Open Schedule View"

        val day3 = today.addDays(7 * 3)
        val day3Label = "${day3.getLabel()}, Open Schedule View"

        scenario {
            case(1) {
                condition {
                    it.macro("[Calendar Week Screen]")
                }.expectation {
                    withScrollRight {
                        it.select("@$todayLabel")
                            .accessIs(todayLabel)
                        it.select("@$day1Label")
                            .accessIs(day1Label)
                        it.select("@$day2Label")
                            .accessIs(day2Label)
                        it.select("@$day3Label")
                            .accessIs(day3Label)
                    }
                }
            }
            case(2) {
                expectation {
                    withScrollLeft {
                        it.select("@$day3Label")
                            .accessIs(day3Label)
                        it.select("@$day2Label")
                            .accessIs(day2Label)
                        it.select("@$day1Label")
                            .accessIs(day1Label)
                        it.select("@$todayLabel")
                            .accessIs(todayLabel)
                    }
                }
            }
            case(3) {
                expectation {
                    withScrollRight {
                        it.exist("@$todayLabel")
                        it.exist("@$day1Label")
                        it.exist("@$day2Label")
                        it.exist("@$day3Label")
                    }
                }
            }
            case(4) {
                expectation {
                    withScrollLeft {
                        it.exist("@$day3Label")
                        it.exist("@$day2Label")
                        it.exist("@$day1Label")
                        it.exist("@$todayLabel")
                    }
                }
            }
        }
    }
}