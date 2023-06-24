package experiment

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest

@Testrun("testConfig/android/clock/testrun.properties")
class SelectDirectTest : UITest() {

    @Test
    fun relative_right() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Timer Screen]")
                }.expectation {
                    val e1 = it.select("[1]")
                    e1.right().textIs("2")
                    e1.relative(":right").textIs("2")

                    e1.right(2).textIs("3")
                    e1.relative(":right(2)").textIs("3")

                    e1.right(3).thisIsEmpty()
                    e1.relative(":right(3)").thisIsEmpty()
                }.expectation(useCache = false) {
                    val e1 = it.select("[1]")
                    e1.right().textIs("2")
                    e1.relative(":right").textIs("2")

                    e1.right(2).textIs("3")
                    e1.relative(":right(2)").textIs("3")

                    e1.right(3).thisIsEmpty()
                    e1.relative(":right(3)").thisIsEmpty()
                }
            }
            case(2) {
                expectation {
                    val e1 = it.select("[1]")
                    e1.right("pos=1").textIs("2")
                    e1.relative(":right(pos=1)").textIs("2")

                    e1.right("pos=2").textIs("3")
                    e1.relative(":right(pos=2)").textIs("3")

                    e1.right("pos=3").thisIsEmpty()
                    e1.relative(":right(pos=3)").thisIsEmpty()
                }.expectation(useCache = false) {
                    val e1 = it.select("[1]")
                    e1.right("pos=1").textIs("2")
                    e1.relative(":right(pos=1)").textIs("2")

                    e1.right("pos=2").textIs("3")
                    e1.relative(":right(pos=2)").textIs("3")

                    e1.right("pos=3").thisIsEmpty()
                    e1.relative(":right(pos=3)").thisIsEmpty()
                }
            }
            case(3) {
                expectation {
                    val e1 = it.select("[Alarm]")
                    e1.rightLabel().textIs("Clock")
                    e1.relative(":rightLabel").textIs("Clock")
                    e1.relative("[:Right label]").textIs("Clock")

                    e1.rightLabel(2).textIs("Timer")
                    e1.relative(":rightLabel(2)").textIs("Timer")
                    e1.relative("[:Right label(2)]").textIs("Timer")
                }
            }
        }

    }

    @Test
    fun relative_flow() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Timer Screen]")
                }.expectation {
                    val e1 = it.select("[1]")
                    e1.flow().textIs("2")
                    e1.relative(":flow").textIs("2")

                    e1.flow(2).textIs("3")
                    e1.relative(":flow(2)").textIs("3")

                    e1.flow(3).textIs("4")
                    e1.relative(":flow(3)").textIs("4")
                }.expectation(useCache = false) {
                    val e1 = it.select("[1]")
                    e1.flow().textIs("2")
                    e1.relative(":flow").textIs("2")

                    e1.flow(2).textIs("3")
                    e1.relative(":flow(2)").textIs("3")

                    e1.flow(3).textIs("4")
                    e1.relative(":flow(3)").textIs("4")
                }
            }
            case(2) {
                expectation {
                    val e1 = it.select("[1]")
                    e1.flow("pos=1").textIs("2")
                    e1.relative(":flow(pos=1)").textIs("2")

                    e1.flow("pos=2").textIs("3")
                    e1.relative(":flow(pos=2)").textIs("3")

                    e1.flow("pos=3").textIs("4")
                    e1.relative(":flow(pos=3)").textIs("4")
                }.expectation(useCache = false) {
                    val e1 = it.select("[1]")
                    e1.flow("pos=1").textIs("2")
                    e1.relative(":flow(pos=1)").textIs("2")

                    e1.flow("pos=2").textIs("3")
                    e1.relative(":flow(pos=2)").textIs("3")

                    e1.flow("pos=3").textIs("4")
                    e1.relative(":flow(pos=3)").textIs("4")
                }
            }
            case(3) {
                expectation {
                    val e1 = it.select("[Alarm]")
                    e1.flow().textIs("Clock")
                    e1.relative(":flow").textIs("Clock")

                    e1.flow(2).textIs("Timer")
                    e1.relative(":flow(2)").textIs("Timer")
                }
            }
        }

    }

    @Test
    fun relative_innerFlow() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Timer Screen]")
                }.expectation {
                    val e1 = it.select("#navigation_bar")
                    e1.innerFlow().classIs("android.widget.ImageView")
                    e1.relative(":innerFlow").classIs("android.widget.ImageView")

                    e1.innerFlow(5).classIs("android.widget.ImageView")
                    e1.relative(":innerFlow(5)").classIs("android.widget.ImageView")

                    e1.innerFlow(6).textIs("Alarm")
                    e1.relative(":innerFlow(6)").textIs("Alarm")
                }.expectation(useCache = false) {
                    val e1 = it.select("#navigation_bar")
                    e1.innerFlow().classIs("android.widget.ImageView")
                    e1.relative(":innerFlow").classIs("android.widget.ImageView")

                    e1.innerFlow(5).classIs("android.widget.ImageView")
                    e1.relative(":innerFlow(5)").classIs("android.widget.ImageView")

                    e1.innerFlow(6).textIs("Alarm")
                    e1.relative(":innerFlow(6)").textIs("Alarm")
                }
            }
            case(2) {
                expectation {
                    val e1 = it.select("#timer_setup")
                    e1.innerFlow("pos=1").textIs("00h 00m 00s")
                    e1.relative(":innerFlow(pos=1)").textIs("00h 00m 00s")

                    e1.innerFlow("pos=2").textIs("1")
                    e1.relative(":innerFlow(pos=2)").textIs("1")

                    e1.innerFlow("pos=3").textIs("2")
                    e1.relative(":innerFlow(pos=3)").textIs("2")

                    e1.innerFlow("pos=4").textIs("3")
                    e1.relative(":innerFlow(pos=4)").textIs("3")

                    e1.innerFlow("pos=5").textIs("4")
                    e1.relative(":innerFlow(pos=5)").textIs("4")
                }.expectation(useCache = false) {
                    val e1 = it.select("#timer_setup")
                    e1.innerFlow("pos=1").textIs("00h 00m 00s")
                    e1.relative(":innerFlow(pos=1)").textIs("00h 00m 00s")

                    e1.innerFlow("pos=2").textIs("1")
                    e1.relative(":innerFlow(pos=2)").textIs("1")

                    e1.innerFlow("pos=3").textIs("2")
                    e1.relative(":innerFlow(pos=3)").textIs("2")

                    e1.innerFlow("pos=4").textIs("3")
                    e1.relative(":innerFlow(pos=4)").textIs("3")

                    e1.innerFlow("pos=5").textIs("4")
                    e1.relative(":innerFlow(pos=5)").textIs("4")
                }
            }
        }

    }

    @Test
    fun relative_xml_based() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Timer Screen]")
                }.expectation {
                    val e1 = it.select("[1]")
                    e1.parent().classIs("android.view.ViewGroup")
                    e1.relative(":parent").classIs("android.view.ViewGroup")

                    e1.parent().child(1).textIs("1")
                    e1.relative(":parent").relative(":child(1)").textIs("1")

                    e1.parent().child(2).textIs("2")
                    e1.relative(":parent").relative(":child(2)").textIs("2")

                }.expectation(useCache = false) {
                    val e1 = it.select("[1]")
                    e1.parent().classIs("android.view.ViewGroup")
                    e1.relative(":parent").classIs("android.view.ViewGroup")

                    e1.parent().child(1).textIs("1")
                    e1.relative(":parent").relative(":child(1)").textIs("1")

                    e1.parent().child(2).textIs("2")
                    e1.relative(":parent").relative(":child(2)").textIs("2")
                }
            }
            case(2) {
                expectation {
                    val e1 = it.select("[2]")
                    e1.sibling(1).textIs("1")
                    e1.sibling(2).textIs("2")
                    e1.sibling(3).textIs("3")
                }.expectation(useCache = false) {
                    val e1 = it.select("[2]")
                    e1.sibling(1).textIs("1")
                    e1.sibling(2).textIs("2")
                    e1.sibling(3).textIs("3")
                }
            }
            case(3) {
                expectation {
                    val e1 = it.select("[2]")
                    e1.ancestor(1).classIs("android.view.ViewGroup")
                    e1.ancestor(2).classIs("android.widget.FrameLayout")
                        .idIs("com.google.android.deskclock:id/timer_setup_digits")
                    e1.ancestor(3).classIs("android.view.ViewGroup").idIs("com.google.android.deskclock:id/timer_setup")
                }.expectation(useCache = false) {
                    val e1 = it.select("[2]")
                    e1.ancestor(1).classIs("android.view.ViewGroup")
                    e1.ancestor(2).classIs("android.widget.FrameLayout")
                        .idIs("com.google.android.deskclock:id/timer_setup_digits")
                    e1.ancestor(3).classIs("android.view.ViewGroup").idIs("com.google.android.deskclock:id/timer_setup")
                }
            }
            case(4) {
                expectation {
                    val e1 = it.select("@Alarm")
                    e1.descendant(1).idIs("com.google.android.deskclock:id/navigation_bar_item_icon_container")
                    e1.descendant(2).classIs("android.widget.ImageView")
                    e1.descendant(3).idIs("com.google.android.deskclock:id/navigation_bar_item_labels_group")
                    e1.descendant(4).textIs("Alarm")
                    e1.descendant(5).thisIsEmpty()
                }.expectation(useCache = false) {
                    val e1 = it.select("@Alarm")
                    e1.descendant(1).idIs("com.google.android.deskclock:id/navigation_bar_item_icon_container")
                    e1.descendant(2).classIs("android.widget.ImageView")
                    e1.descendant(3).idIs("com.google.android.deskclock:id/navigation_bar_item_labels_group")
                    e1.descendant(4).textIs("Alarm")
                    e1.descendant(5).thisIsEmpty()
                }
            }
            case(5) {
                expectation {
                    val e1 = it.select("@Alarm")
                    e1.next(1).idIs("com.google.android.deskclock:id/navigation_bar_item_icon_container")
                    e1.next(2).classIs("android.widget.ImageView")
                    e1.next(3).idIs("com.google.android.deskclock:id/navigation_bar_item_labels_group")
                    e1.next(4).textIs("Alarm")
                    e1.next(5).accessIs("Clock")
                }.expectation(useCache = false) {
                    val e1 = it.select("@Alarm")
                    e1.next(1).idIs("com.google.android.deskclock:id/navigation_bar_item_icon_container")
                    e1.next(2).classIs("android.widget.ImageView")
                    e1.next(3).idIs("com.google.android.deskclock:id/navigation_bar_item_labels_group")
                    e1.next(4).textIs("Alarm")
                    e1.next(5).accessIs("Clock")
                }
            }
            case(6) {
                expectation {
                }.expectation(useCache = false) {
                    val e1 = it.select("@Clock")
                    e1.previous(1).textIs("Alarm")
                    e1.previous(2).idIs("com.google.android.deskclock:id/navigation_bar_item_labels_group")
                    e1.previous(3).classIs("android.widget.ImageView")
                    e1.previous(4).idIs("com.google.android.deskclock:id/navigation_bar_item_icon_container")
                }
            }

        }

    }

    @Test
    fun relative_misc() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Timer Screen]")
                }.expectation {
                    it.select("[Title]").textIs("Timer")
                        .rightImage().accessIs("More options")
                        .flow().textIs("00h 00m 00s")
                        .belowButton().textIs("1")
                        .rightButton(2).textIs("3")
                        .leftButton(2).textIs("1")
                        .belowImage()
                        .belowLabel().textIs("Clock")
                }.expectation(useCache = false) {
                    it.select("[Title]").textIs("Timer")
                        .rightImage().accessIs("More options")
                        .flow().textIs("00h 00m 00s")
                        .belowButton().textIs("1")
                        .rightButton(2).textIs("3")
                        .leftButton(2).textIs("1")
                        .belowImage()
                        .belowLabel().textIs("Clock")
                }
            }
        }
        case(2) {
            expectation {
                it.select("[1][:Right button]").textIs("2")
                    .select("[:Below button]").textIs("5")
                    .select("[:Left button]").textIs("4")
                    .select("[:Above button]").textIs("1")
            }.expectation(useCache = false) {
                it.select("[1][:Right button]").textIs("2")
                    .select("[:Below button]").textIs("5")
                    .select("[:Left button]").textIs("4")
                    .select("[:Above button]").textIs("1")
            }
        }

    }

}