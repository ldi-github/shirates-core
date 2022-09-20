package experiment

import io.appium.java_client.android.nativekey.AndroidKey
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.storage.Memo
import shirates.core.testcode.SheetName
import shirates.core.testcode.UITest

@SheetName("NoLoadRun test")
@Testrun("unitTestConfig/android/calculator/noLoadRun.testrun.properties")
class NoLoadRunTest : UITest() {

    @Test
    @Order(10)
    fun AnyAssertionScreenIsExtensionTest() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Restart Calculator]")
                        .screenIs("[Calculator Main Screen]")
                }.action {

                }.expectation {
                    "Akihabara".thisIs(expected = "Akihabara")
                    "Akihabara".thisIsNot(expected = "Asakusa")
                }
            }
        }
    }

    @Test
    @Order(20)
    fun BooleanAssertionExtension() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Restart Calculator]")
                        .screenIs("[Calculator Main Screen]")
                }.action {

                }.expectation {
                    true.thisIsTrue()
                    false.thisIsFalse()
                }
            }
        }
    }

    @Test
    @Order(30)
    fun StringAssertionExtension() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Restart Calculator]")
                        .screenIs("[Calculator Main Screen]")
                }.action {

                }.expectation {
                    "Shinagawa".thisIs("Shinagawa")
                    "Shinagawa".thisIsNot("Shimbashi")
                    "".thisIsEmpty()
                    "a".thisIsNotEmpty()
                    " ".thisIsBlank()
                    "a".thisIsNotBlank()
                    "Tokyo Sky Tree".thisContains("Sky")
                    "Tokyo Tower".thisContainsNot("Sky")
                    "Roppongi".thisStartsWith("R")
                    "Roppongi".thisStartsWithNot("L")
                    "Shibuya".thisEndsWith("ya")
                    "Shibuya".thisEndsWithNot("gi")
                    "Ikebukuro".thisMatches("^Ike.*ro$")
                    "Ikebukuro".thisMatchesNot("^Shi.*ya$")
                }
            }
        }
    }

    @Test
    @Order(40)
    fun StringStorageExtension() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Restart Calculator]")
                        .screenIs("[Calculator Main Screen]")
                }.action {
                    it.memoTextAs("key")
                }.expectation {
                    Memo.read("key").thisIsEmpty()
                }
            }
        }
    }

    @Test
    @Order(50)
    fun AnyAppExtension() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Restart Calculator]")
                        .screenIs("[Calculator Main Screen]")
                }.action {
                    it.isAppInstalled()
                    it
                        .installApp("appPackage1")
                        .removeApp("packageOrBundleId")
                        .terminateApp("appPackage")
                        .restartApp()

                }.expectation {
                }
            }
        }
    }

    @Test
    @Order(50)
    fun AnyAssertionExtension() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Restart Calculator]")
                        .screenIs("[Calculator Main Screen]")
                }.action {

                }.expectation {
                    it
                        .appIs("[Calculator]")
                        .keyboardIsShown()
                        .keyboardIsNotShown()
                        .packageIs("com.google.android.calculator")
                        .screenIs("[Calculator Main Screen]")
                        .exist("human error")
                        .existWithScrollDown("luck")
                        .existWithScrollUp("sweet memories")
                        .existInScanResults("defect")
                        .dontExist("impossible")
                        .dontExistWithScrollDown("luck")
                        .dontExistWithScrollUp("sweet memories")
                        .dontExistInScanResults("defect")
                }
            }
        }
    }

    @Test
    @Order(60)
    fun AnyDescriptorExtension() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Restart Calculator]")
                        .screenIs("[Calculator Main Screen]")
                    it
                        .describe("describe 1")
                        .procedure("procedure 1") {}
                        .manual("manual 1")
                }.action {
                    it
                        .describe("describe 2")
                        .manual("manual 2")
                        .knownIssue("knownIssue 2", "http://ticket.url")
                        .procedure("procedure 2") {
                            it
                                .describe("describe 3")
                                .manual("manual 3")
                                .knownIssue("knownIssue 3", "http://ticket.url")
                                .procedure("procedure 3") {}
                        }
                    it.target("target")
                        .describe("describe 4")
                        .procedure("procedure 4") {}
                        .manual("manual 4")
                        .knownIssue("knownIssue 4", "http://ticket.url")
                }.expectation {
                }
            }
        }
    }

    @Test
    @Order(70)
    fun AnyKeyboardExtension() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Restart Calculator]")
                        .screenIs("[Calculator Main Screen]")
                }.action {
                    it
                        .hideKeyboard()
                        .pressBack()
                        .pressHome()
                        .pressEnter()
                        .pressSearch()
                }.expectation {
                }
            }
        }
    }

    @Test
    @Order(80)
    fun AnyScreenExtension() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Restart Calculator]")
                        .screenIs("[Calculator Main Screen]")
                }.action {
                    it
                        .waitScreenOf("[Screen1]", "[Screen2]")
                        .waitScreen("[Screen1]")
                }.expectation {
                }
            }
        }
    }

    @Test
    @Order(90)
    fun AnyScrollExtension() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Restart Calculator]")
                        .screenIs("[Calculator Main Screen]")
                }.action {
                    it
                        .scrollDown()
                        .scrollUp()
                        .scrollToBottom()
                        .scrollToTop()
                        .scanElements()
                }.expectation {
                }
            }
        }
    }

    @Test
    @Order(100)
    fun AnySwipeExtension() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Restart Calculator]")
                        .screenIs("[Calculator Main Screen]")
                }.action {
                    it
                        .swipePointToPoint(startX = 100, startY = 200, endX = 300, endY = 400)
                        .swipeCenterToTop()
                        .flickCenterToTop()
                        .swipeCenterToBottom()
                        .flickCenterToBottom()
                        .swipeCenterToLeft()
                        .flickCenterToLeft()
                        .swipeCenterToRight()
                        .flickCenterToRight()
                        .swipeLeftToRight()
                        .flickLeftToRight()
                        .swipeRightToLeft()
                        .flickRightToLeft()
                        .swipeBottomToTop()
                        .flickBottomToTop()
                        .swipeTopToBottom()
                        .flickTopToBottom()
                        .swipeElementToElement(startElement = it.select("1"), endElement = it.select("2"))
                        .swipeElementToElementAdjust(startElement = it.select("1"), endElement = it.select("2"))
                }.expectation {
                }
            }
        }
    }

    @Test
    @Order(110)
    fun AnyTapAppIconExtension() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Restart Calculator]")
                        .screenIs("[Calculator Main Screen]")
                }.action {
                    it
                        .tapAppIcon(appIconName = "App1")
                }.expectation {
                }
            }
        }
    }

    @Test
    @Order(120)
    fun AnyTapExtension() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Restart Calculator]")
                        .screenIs("[Calculator Main Screen]")
                }.action {
                    it.tap(x = 100, y = 200)
                    it.select("Network & internet")
                        .tap()
                    it.tap("Connected devices")
                        .tapWithScrollDown("Apps & notifications")
                        .tapWithScrollUp("Network & internet")
                        .pressAndroid(key = AndroidKey.A)
                }.expectation {
                }
            }
        }
    }

    @Test
    @Order(130)
    fun AnyWaitExtension() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Restart Calculator]")
                        .screenIs("[Calculator Main Screen]")
                }.action {
                    it.wait(2.5)
                }.expectation {
                }
            }
        }
    }

    @Test
    @Order(140)
    fun TestElementAssertionExtension() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Restart Calculator]")
                        .screenIs("[Calculator Main Screen]")
                }.action {
                }.expectation {
                    it
                        .idIs(expected = "id1")
                        .textIs(expected = "text1")
                        .accessIs(expected = "access1")
                        .textIsNot(expected = "text1")
                        .textContains(expected = "text1")
                        .textContainsNot(expected = "text1")
                        .textStartsWith(expected = "text1")
                        .textStartsWithNot(expected = "text1")
                        .textEndsWith(expected = "text1")
                        .textEndsWithNot(expected = "text1")
                        .textMatches(expected = "text1")
                        .textMatchesNot(expected = "text1")
                        .textIsEmpty()
                        .textIsNotEmpty()
                        .checkedIs(expected = "true")
                        .checkIsON()
                        .checkIsOFF()
                        .enabledIs(expected = "true")
                        .enabledIsTrue()
                        .enabledIsFalse()
                        .buttonIsActive()
                        .buttonIsNotActive()
                        .selectedIs(expected = "true")
                        .selectedIsTrue()
                        .selectedIsFalse()
                        .displayedIs(expected = "true")
                        .attributeIs(attributeName = "attribute1", expected = "value1")
                        .attributeIsNot(attributeName = "attribute1", expected = "value1")
                        .classIs(expected = "class1")
                }
            }
        }
    }

    @Test
    @Order(150)
    fun TestElementExtension() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Restart Calculator]")
                        .screenIs("[Calculator Main Screen]")
                }.action {
                    it
                        .sendKeys("value1")
                        .clearInput()
                        .sendKeys("abcde")
                }.expectation {
                }
            }
        }
    }

    @Test
    @Order(160)
    fun TestElementStorageExtension() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Restart Calculator]")
                        .screenIs("[Calculator Main Screen]")
                }.action {
                    writeMemo(key = "key1", text = "value1")
                    describe(readMemo(key = "key1"))
                }.expectation {
                    readMemo(key = "key1").thisIs("value1")
                }
            }
        }
    }

    @Test
    @Order(170)
    fun TestElementSwipeExtension() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Restart Calculator]")
                        .screenIs("[Calculator Main Screen]")
                }.action {
                    it
                        .swipeTo("target1")
                        .swipeTo("target1", durationSeconds = 1.0)
                        .swipeToAdjust("target1")
                        .swipeToAdjust("target1", durationSeconds = 2.0)
                        .swipeVerticalTo(endY = 100)
                        .swipeVerticalTo(endY = 100, durationSeconds = 1.0)
                        .swipeHorizontalTo(endX = 100)
                        .swipeHorizontalTo(endX = 100, durationSeconds = 1.0)
                        .swipeToTop()
                        .swipeToTop(durationSeconds = 1.0)
                        .flickToTop()
                        .swipeToBottom()
                        .swipeToBottom(durationSeconds = 1.0)
                        .flickToBottom()
                        .swipeToCenter()
                        .swipeToCenter(durationSeconds = 1.0)
                        .swipeToRight()
                        .swipeToRight(durationSeconds = 1.0)
                        .flickToRight()
                        .swipeToLeft()
                        .swipeToLeft(durationSeconds = 1.0)
                        .flickToLeft()
                }.expectation {
                }
            }
        }
    }
}