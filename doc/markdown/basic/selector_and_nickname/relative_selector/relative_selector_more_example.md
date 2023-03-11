# Relative selector more example

## Example 2

### RelativeCommand1.kt

(`kotlin/tutorial/basic/RelativeCommand1.kt`)

```kotlin
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
                    it.macro("[Wallpaper Screen]")
                }.expectation {
                    it.exist("My photos")
                        .relative(":right").textIs("Living Universe")
                    it.select("My photos")
                        .relative(":right(2)").textIs("Come Alive")
                    it.select("My photos")
                        .relative(":right(100)").thisIsEmpty()
                    it.select("My photos")
                        .relative(":belowImage").classIs("android.widget.ImageView")
                }
            }
            case(2) {
                expectation {
                    it.exist("My photos")
                        .right().textIs("Living Universe")
                    it.select("My photos")
                        .right(2).textIs("Come Alive")
                    it.select("My photos")
                        .right(100).thisIsEmpty()
                    it.select("My photos")
                        .belowImage().classIs("android.widget.ImageView")
                }
            }
            case(3) {
                expectation {
                    it.exist("My photos") {
                        right().textIs("Living Universe")
                        right(2).textIs("Come Alive")
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
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.exist("Network & internet")
                        .relative(":flow").textIs("Mobile, Wi‑Fi, hotspot")
                        .relative(":flow").classIs("android.widget.ImageView")
                        .relative(":flow").textIs("Connected devices")
                        .relative(":flow").textIs("Bluetooth, pairing")
                        .relative(":flow").classIs("android.widget.ImageView")
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
                        .relative(":vflow").textIs("Mobile, Wi‑Fi, hotspot")
                        .relative(":vflow").textIs("Connected devices")
                        .relative(":vflow").textIs("Bluetooth, pairing")
                        .relative(":vflow").textIs("Apps")
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
                            relative(":child").textIs("Network & internet")
                            relative(":child(2)").textIs("Mobile, Wi‑Fi, hotspot")
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
```

### Link

- [Relative selector command (Direction based)](relative_selector_direction.md)

- [Relative selector command (Widget flow based)](relative_selector_flow.md)

- [Relative selector command (XML based)](relative_selector_xml.md)


- [Relative selector](relative_selector.md)

