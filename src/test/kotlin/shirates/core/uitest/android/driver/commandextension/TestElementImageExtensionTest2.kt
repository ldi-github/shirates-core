package shirates.core.uitest.android.driver.commandextension

import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.exception.TestDriverException
import shirates.core.testcode.UITest
import shirates.core.testcode.Want
import shirates.helper.ImageSetupHelper

@Want
@Testrun("testConfig/android/calendar/testrun.properties")
class TestElementImageExtensionTest2 : UITest() {

    @Test
    @Order(0)
    fun setupImage() {

        scenario {
            ImageSetupHelper.setupImageCalendarWeekScreen()
        }
    }

    @Test
    @Order(10)
    fun findImage() {

        scenario {
            case(1) {
                condition {
                    it.restartApp()
                    it.macro("[Calendar Week Screen]")
                }.expectation {
                    it.findImage("[Day1-1].png").thisIsTrue()
                    it.findImageWithScrollRight("[Day1-1].png", scrollDurationSeconds = 0.2).thisIsTrue()
                    it.findImage("[Day3-1].png", throwsException = false).thisIsFalse()
                    it.findImageWithScrollRight("[Day3-2].png", scrollDurationSeconds = 0.2).thisIsTrue()
                    it.findImage("[Day1-1].png", throwsException = false).thisIsFalse()
                    it.findImageWithScrollLeft("[Day1-1].png", scrollDurationSeconds = 0.2).thisIsTrue()
                }
            }
            case(2) {
                expectation {
                    withScrollRight {
                        it.findImage("[Day3-1].png").thisIsTrue()
                        withScrollLeft {
                            it.findImage("[Day1-1].png").thisIsTrue()
                            it.findImageWithScrollRight("[Day2-1].png", scrollDurationSeconds = 0.2)
                        }
                        it.findImage("[Day3-1].png").thisIsTrue()
                    }
                    it.findImage("[Day1-1].png", throwsException = false).thisIsFalse()
                }
            }
        }
    }

    @Test
    @Order(20)
    fun existImage() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Calendar Week Screen]")
                }.expectation {
                    it
                        .existImage("[Day1-1].png", waitSeconds = 0.2) {
                            imageMatched.thisIsTrue()
                            hasImageMatchResult.thisIsTrue()
                            isFound.thisIsTrue()
                            isDummy.thisIsFalse()
                            isEmpty.thisIsTrue()
                        }
                        .dontExistImage("[Day2-1].png", waitSeconds = 0.2) {
                            imageMatched.thisIsFalse()
                            hasImageMatchResult.thisIsTrue()
                            isFound.thisIsFalse()
                            isDummy.thisIsFalse()
                            isEmpty.thisIsTrue()
                        }
                        .withScrollRight(scrollDurationSeconds = 0.2) {
                            it.existImage("[Day2-1].png")
                        }
                        .dontExistImage("[Day1-1].png")
                        .withScrollLeft(scrollDurationSeconds = 0.2) {
                            it.existImage("[Day1-1].png")
                        }
                }
            }
            case(2) {
                expectation {
                    withScrollRight(scrollDurationSeconds = 0.2, scrollMaxCount = 3) {
                        it.existImage("[Day2-2].png", waitSeconds = 0.2)
                        withScrollLeft(scrollDurationSeconds = 0.2) {
                            it.existImage("[Day1-2].png")
                        }
                        it.existImage("[Day3-3].png")
                        withScrollLeft(scrollDurationSeconds = 0.2, scrollMaxCount = 3) {
                            it.existImage("[Day2-1].png")
                            withScrollRight(scrollDurationSeconds = 0.2) {
                                it.existImage("[Day3-1].png")
                            }
                            it.existImage("[Day1-1].png")
                            withScrollRight() {
                                it.existImage("[Day3-1].png")
                            }
                            it.existImage("[Day2-2].png")
                        }
                        suppressWithScroll {
                            it.dontExistImage("[Day3-3].png")
                        }
                        it.existImage("[Day3-3].png")
                    }
                    withScrollLeft {
                        it.existImage("[Day1-1].png")
                    }
                }
            }
        }
    }

    @Test
    @Order(30)
    fun imageSelectorNotSupported() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Calendar Week Screen]")
                }.expectation {
                    assertThatThrownBy {
                        it.select("[Day1-1].png")
                    }.isInstanceOf(TestDriverException::class.java)
                        .hasMessage("Image selector is not supported in this command. Use other command that supports image selector.")

                    assertThatThrownBy {
                        it.canSelect("[Day1-1].png")
                    }.isInstanceOf(TestDriverException::class.java)
                        .hasMessage("Image selector is not supported in this command. Use other command that supports image selector.")
                }
            }
        }
    }

}