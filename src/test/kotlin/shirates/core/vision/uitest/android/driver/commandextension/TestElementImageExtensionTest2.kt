package shirates.core.vision.uitest.android.driver.commandextension

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.thisIsFalse
import shirates.core.driver.commandextension.thisIsTrue
import shirates.core.testcode.Want
import shirates.core.vision.driver.commandextension.*
import shirates.core.vision.testcode.VisionTest
import shirates.helper.ImageSetupHelper

@Want
@Testrun("testConfig/android/calendar/testrun.properties")
class TestElementImageExtensionTest2 : VisionTest() {

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
                        .existImage("[Day1-1].png", waitSeconds = 0.2)
                        .dontExistImage("[Day2-1].png", waitSeconds = 0.2)
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
                        withoutScroll {
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

//    @Test
//    @Order(30)
//    fun imageSelector() {
//
//        scenario {
//            case(1) {
//                condition {
//                    it.macro("[Calendar Week Screen]")
//                }.action {
//                    v1 = it.detect("[Day1-1].png")
//                }.expectation {
//                    assertThat(v1.isFound).isTrue()
//                }
//            }
//            case(2) {
//                action {
//                    b1 = it.canDetect("[Day1-1].png")
//                }.expectation {
//                    assertThat(b1).isTrue()
//                }
//            }
//        }
//    }

}