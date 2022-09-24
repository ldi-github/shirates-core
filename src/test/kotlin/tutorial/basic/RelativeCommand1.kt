package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.testcode.NoLoadRun
import shirates.core.testcode.UITest

@Testrun("testConfig/android/clock/testrun.properties")
class RelativeCommand1 : UITest() {

    @NoLoadRun
    @Test
    @Order(10)
    fun relative_direction() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Alarm Screen]")
                }.expectation {
                    it.exist("Clock")
                        .relative(":right").textIs("Timer")
                    it.select("Clock")
                        .relative(":right(2)").textIs("Stopwatch")
                    it.select("Clock")
                        .relative(":right(100)").thisIsEmpty()
                    it.select("Clock")
                        .relative(":aboveImage").classIs("android.widget.ImageView")
                }
            }
            case(2) {
                expectation {
                    it.exist("Clock")
                        .right().textIs("Timer")
                    it.select("Clock")
                        .right(2).textIs("Stopwatch")
                    it.select("Clock")
                        .right(100).thisIsEmpty()
                    it.select("Clock")
                        .aboveImage().classIs("android.widget.ImageView")
                }
            }
            case(3) {
                expectation {
                    it.exist("Clock") {
                        right().textIs("Timer")
                        right(2).textIs("Stopwatch")
                        right(100).thisIsEmpty()
                        aboveImage().classIs("android.widget.ImageView")
                    }
                }
            }
        }
    }

    @Test
    @Order(20)
    fun relative_flow() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Alarm Screen]")
                }.expectation {
                    it.exist("8:30 AM")
                        .relative(":flow").accessIs("Expand alarm")
                        .relative(":flow").textIs("Mon, Tue, Wed, Thu, Fri")
                        .relative(":flow").idIs("onoff")
                        .relative(":flow").textIs("9:00 AM")
                        .relative(":flow").accessIs("Expand alarm")
                        .relative(":flow").textIs("Sun, Sat")
                        .relative(":flow").idIs("onoff")
                }
            }
            case(2) {
                expectation {
                    it.exist("8:30 AM")
                        .flow().accessIs("Expand alarm")
                        .flow().textIs("Mon, Tue, Wed, Thu, Fri")
                        .flow().idIs("onoff")
                        .flow().textIs("9:00 AM")
                        .flow().accessIs("Expand alarm")
                        .flow().textIs("Sun, Sat")
                        .flow().idIs("onoff")
                }
            }
            case(3) {
                expectation {
                    it.exist("8:30 AM") {
                        flow().accessIs("Expand alarm")
                        flow(2).textIs("Mon, Tue, Wed, Thu, Fri")
                        flow(3).idIs("onoff")
                        flow(4).textIs("9:00 AM")
                        flow(5).accessIs("Expand alarm")
                        flow(6).textIs("Sun, Sat")
                        flow(7).idIs("onoff")
                    }
                }
            }
        }
    }

    @Test
    @Order(30)
    fun relative_vflow() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Alarm Screen]")
                }.expectation {
                    it.exist("8:30 AM")
                        .relative(":vflow").textIs("Mon, Tue, Wed, Thu, Fri")
                        .relative(":vflow").textIs("9:00 AM")
                        .relative(":vflow").textIs("Sun, Sat")
                }
            }
            case(2) {
                expectation {
                    it.exist("8:30 AM")
                        .vflow().textIs("Mon, Tue, Wed, Thu, Fri")
                        .vflow().textIs("9:00 AM")
                        .vflow().textIs("Sun, Sat")
                }
            }
            case(3) {
                expectation {
                    it.exist("8:30 AM") {
                        vflow().textIs("Mon, Tue, Wed, Thu, Fri")
                        vflow(2).textIs("9:00 AM")
                        vflow(3).textIs("Sun, Sat")
                    }
                }
            }
        }
    }

    @Test
    @Order(40)
    fun relative_xml() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Alarm Screen]")
                }.expectation {
                    it.exist("8:30 AM").parent()
                        .apply {
                            relative(":child").textIs("8:30 AM")
                            relative(":child(2)").accessIs("Expand alarm")
                            relative(":child(3)").relative(":child").textIs("Mon, Tue, Wed, Thu, Fri")
                            relative(":child(4)").idIs("onoff")
                        }
                }
            }
            case(2) {
                expectation {
                    it.exist("8:30 AM").parent()
                        .apply {
                            child().textIs("8:30 AM")
                            child(2).accessIs("Expand alarm")
                            child(3).child().textIs("Mon, Tue, Wed, Thu, Fri")
                            child(4).idIs("onoff")
                        }
                }
            }
        }
    }

    @Test
    @Order(50)
    fun relative_nickname() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Alarm Screen]")
                }.expectation {
                    it.exist("8:30 AM")
                        .apply {
                            // Relative from <8:30 AM>
                            exist("[:Expand alarm]").idIs("arrow")
                            exist("[:Days of week]").idIs("days_of_week")
                            exist("[:ON/OFF]").idIs("onoff")
                        }
                }
            }
            case(2) {
                expectation {
                    /**
                     * See nickname definition in testConfig/android/clock/screens/[Alarm Screen].json

                    "[:Expand alarm]": ":flow(1)",
                    "[:Days of week]": ":flow(2)",
                    "[:ON/OFF]": ":flow(3)",

                    "[8:30 AM]": "",
                    "[8:30 AM Expand alarm]": "[8:30 AM][:Expand alarm]",
                    "[8:30 AM Days of week]": "[8:30 AM][:Days of week]",
                    "[8:30 AM ON/OFF]": "[8:30 AM][:ON/OFF]",

                    "[9:00 AM]": "",
                    "[9:00 AM Expand alarm]": "[9:00 AM][:Expand alarm]",
                    "[9:00 AM Days of week]": "[9:00 AM][:Days of week]",
                    "[9:00 AM ON/OFF]": "[9:00 AM][:ON/OFF]"
                     */
                    it.exist("[8:30 AM]")
                        .exist("[8:30 AM Expand alarm]").idIs("arrow")
                        .exist("[8:30 AM Days of week]").idIs("days_of_week")
                        .exist("[8:30 AM ON/OFF]").idIs("onoff")
                    it.exist("[9:00 AM]")
                        .exist("[9:00 AM Expand alarm]").idIs("arrow")
                        .exist("[9:00 AM Days of week]").idIs("days_of_week")
                        .exist("[9:00 AM ON/OFF]").idIs("onoff")
                }
            }
        }

    }
}