package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class RelativeCommand1 : UITest() {

    @Test
    @Order(10)
    fun relative_direction() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Wallpaper & style Screen]")
                }.expectation {
                    it.exist("Lock screen")
                        .relative(":right", margin = 0).textIs("Home screen")
                    it.select("Lock screen")
                        .relative(":right(2)", margin = 0).thisIsEmpty()
                    it.select("Lock screen")
                        .relative(":belowImage", margin = 0).classIs("android.widget.ImageView")
                }
            }
            case(2) {
                expectation {
                    it.exist("Lock screen")
                        .right().textIs("Home screen")
                    it.select("Lock screen")
                        .right(2).thisIsEmpty()
                    it.select("Lock screen")
                        .belowImage().classIs("android.widget.ImageView")
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
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.exist("Network & internet")
                        .relative(":flow", margin = 0).textIs("Mobile, Wi‑Fi, hotspot")
                        .relative(":flow", margin = 0).classIs("android.widget.ImageView")
                        .relative(":flow", margin = 0).textIs("Connected devices")
                        .relative(":flow", margin = 0).textIs("Bluetooth, pairing")
                        .relative(":flow", margin = 0).classIs("android.widget.ImageView")
                }
            }
            case(2) {
                expectation {
                    it.exist("Network & internet")
                        .flow().textIs("Mobile, Wi‑Fi, hotspot")
                        .flow().classIs("android.widget.ImageView")
                        .flow().textIs("Connected devices")
                        .flow().textIs("Bluetooth, pairing")
                        .flow().classIs("android.widget.ImageView")
                }
            }
            case(3) {
                expectation {
                    it.exist("Network & internet") {
                        flow().textIs("Mobile, Wi‑Fi, hotspot")
                        flow(2).classIs("android.widget.ImageView")
                        flow(3).textIs("Connected devices")
                        flow(4).textIs("Bluetooth, pairing")
                        flow(5).classIs("android.widget.ImageView")
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
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.exist("Network & internet")
                        .relative(":vflow", margin = 0).textIs("Mobile, Wi‑Fi, hotspot")
                        .relative(":vflow", margin = 0).textIs("Connected devices")
                        .relative(":vflow", margin = 0).textIs("Bluetooth, pairing")
                        .relative(":vflow", margin = 0).textIs("Apps")
                }
            }
            case(2) {
                expectation {
                    it.exist("Network & internet")
                        .vflow().textIs("Mobile, Wi‑Fi, hotspot")
                        .vflow().textIs("Connected devices")
                        .vflow().textIs("Bluetooth, pairing")
                        .vflow().textIs("Apps")
                }
            }
            case(3) {
                expectation {
                    it.exist("Network & internet") {
                        vflow().textIs("Mobile, Wi‑Fi, hotspot")
                        vflow(2).textIs("Connected devices")
                        vflow(3).textIs("Bluetooth, pairing")
                        vflow(4).textIs("Apps")
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
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.exist("Network & internet").parent()
                        .apply {
                            relative(":child", margin = 0).textIs("Network & internet")
                            relative(":child(2)", margin = 0).textIs("Mobile, Wi‑Fi, hotspot")
                        }
                }
            }
            case(2) {
                expectation {
                    it.exist("Network & internet").parent()
                        .apply {
                            child().textIs("Network & internet")
                            child(2).textIs("Mobile, Wi‑Fi, hotspot")
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
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.exist("Network & internet")
                        .exist("[:Summary]").textIs("Mobile, Wi‑Fi, hotspot")
                }
            }
            case(2) {
                expectation {
                    /**
                     * See nickname definition in testConfig/android/androidSettings/screens/[Android Settings Top Screen].json

                    "[:Summary]": ":belowLabel"

                     */
                    it.exist("[Network & internet]")
                        .exist("[:Summary]").textIs("Mobile, Wi‑Fi, hotspot")
                }
            }
        }

    }
}