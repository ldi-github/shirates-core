package experiment

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.PropertiesManager
import shirates.core.configuration.Selector
import shirates.core.configuration.Testrun
import shirates.core.driver.*
import shirates.core.driver.branchextension.android
import shirates.core.driver.commandextension.*
import shirates.core.exception.TestDriverException
import shirates.core.logging.printInfo
import shirates.core.testcode.UITest
import shirates.core.utility.android.AdbUtility

@Testrun("testConfig/android/androidSettings/testrun.properties")
class AdHocTestAndroid : UITest() {

    @Test
    @Order(10)
    fun someTest() {

        scenario(useCache = false) {
            case(1) {
                condition {
                    it.screenIs("[Android Settings Top Screen]")
                }.action {
                }.expectation {
                    it.exist("#settings_homepage_container")
                    it.exist("#settings_homepage*")
                    it.exist("#*homepage_container")
                    it.exist("#*homepage*")

                    it.tap("aaa")
                }
            }
            case(2) {
                expectation {
                    it.exist("Network & internet")
                }
            }
        }
    }

    @Test
    @Order(20)
    fun noCase() {

        scenario {

        }
    }

    @DisableCache
    @Test
    fun select() {

        run {
            val e = it.select("<Battery>:leftImage")
            e.printInfo()
        }

        run {
            val e = it.select("*Battery", waitSeconds = 0.0)
            val widgets = e.widgets
            widgets.printInfo()
        }
        run {
            val widgets = testDrive.widgets
            widgets.printInfo()
        }
    }

    @EnableCache
    @Test
    fun compareCacheAndDirect() {

        fun compare(expression: String, safeElementOnly: Boolean): Pair<TestElement, TestElement> {

            expression.printInfo()

            val sel = Selector(expression)
            sel.printInfo()
            sel.getFullXPathCondition(packageName = "package1").printInfo()

            val c = TestElementCache.select(selector = sel, throwsException = false)
            c.printInfo()
            val d = TestDriver.selectDirect(selector = sel, throwsException = false)
            d.printInfo()
            assertThat(c.toString()).isEqualTo(d.toString())

            return c to d
        }

//        compare("<#search_bar>:previous(1)", safeElementOnly = false)
//        compare("<#search_bar>:previous(2)", safeElementOnly = false)
//
//        compare("<#android:id/icon>:previous(1)", safeElementOnly = true)
//        compare("<#android:id/icon>:previous(2)", safeElementOnly = true)
//
//
//        compare("<#search_bar>:next(1)", safeElementOnly = false)
//        compare("<#search_bar>:next(2)", safeElementOnly = false)

        compare("<Apps>:next(1)", safeElementOnly = true)
        compare("<Apps>:next(2)", safeElementOnly = true)

        compare("<Network & internet>:next(1)", safeElementOnly = true)
        compare("<Network & internet>:next(2)", safeElementOnly = true)

        compare("<#search_bar>:sibling(1)", safeElementOnly = false)
        compare("<#search_bar>:sibling(2)", safeElementOnly = false)

        compare("<Apps>:sibling(1)", safeElementOnly = true)
        compare("<Apps>:sibling(2)", safeElementOnly = true)

        compare("<#search_bar>:parent:child(1)", safeElementOnly = false)
        compare("<#search_bar>:parent:child(2)", safeElementOnly = false)

        compare("<Apps>:parent:child(1)", safeElementOnly = true)
        compare("<Apps>:parent:child(2)", safeElementOnly = true)
    }

    @Test
    fun textEndsWith() {

        useCache {
            it.select("Network & internet")
                .textIs("Network & internet")

            it.select("*work & internet")
                .textIs("Network & internet")
        }
        suppressCache {
            it.select("Network & internet")
                .textIs("Network & internet")

            it.select("*work & internet")
                .textIs("Network & internet")
        }

    }

    @Test
    fun rootElement() {

        suppressCache {
            rootElement.printInfo()
        }
    }

    @Test
    fun rerunScenario() {

        PropertiesManager.setPropertyValue("enableRerunScenario", "true")
        PropertiesManager.setPropertyValue("enableAlwaysRerunOnErrorAndroid", "false")

        scenario {
            case(1) {
                condition {
                    throw TestDriverException("AppiumProxy.getSource() timed out")
                }
            }
        }
    }

    @Test
    fun selectWithScroll_log() {

        scenario {
            case(1) {
                condition {
                    it.screenIs("[Android Settings Top Screen]")
                }.expectation {
                    it.selectWithScrollDown("[Network & internet]", log = true)
                    it.selectWithScrollDown("[Connected devices]", log = false)
                    it.selectWithScrollDown("[Apps]")
                    silent {
                        it.selectWithScrollDown("[Notifications]")
                    }
                    withScrollDown {
                        it.cellOf("[Network & internet]") {
                            it.exist("[Network & internet Icon]")
                        }
                        it.cellOf("[Connected devices]") {
                            it.exist("[Connected devices Icon]")
                        }
                        it.cellOf("[Apps]") {
                            it.exist("[Apps Icon]")
                        }
                        silent {
                            it.cellOf("[Notifications]") {
                                it.exist("[Notifications]")
                            }
                        }
                    }
                }
            }
        }
    }

    @Test
    fun removeEmptyBlock() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {

                    android {

                    }
                    android {
                        printInfo("hoge")
                    }
                }.expectation {
                    android {

                    }
                    android {
                        printInfo("fuga")
                    }
                }
            }
        }

    }

    @Test
    fun gesturalTest() {

        val isEnabled =
            AdbUtility.isOverlayEnabled(
                "com.android.internal.systemui.navbar.gestural",
                udid = shirates.core.driver.testProfile.udid
            )

        println("com.android.internal.systemui.navbar.gestural: $isEnabled")
    }
}