package shirates.core.vision.uitest.android.driver.commandextension

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.thisIsFalse
import shirates.core.driver.commandextension.thisIsTrue
import shirates.core.testcode.Want
import shirates.core.vision.driver.commandextension.*
import shirates.core.vision.testcode.VisionTest

@Want
@Testrun("testConfig/android/files/testrun.properties")
class TestElementImageExtensionTest2 : VisionTest() {

    @Test
    @Order(10)
    fun findImage() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Files Top Screen]")
                }.expectation {
                    var imagesIcon = it.findImage("[Images Button]")
                    imagesIcon.isFound.thisIsTrue()

                    imagesIcon.onLine {
                        val thisWeekIcon = it.findImageWithScrollRight("[This week Button]")
                        thisWeekIcon.isFound.thisIsTrue()

                        imagesIcon = it.findImageWithScrollLeft("[Images Button]")
                        imagesIcon.isFound.thisIsTrue()
                    }
                }
            }
            case(2) {
                expectation {
                    withScrollRight {
                        it.findImage("[This week Button]").isFound.thisIsTrue()
                        withScrollLeft {
                            it.findImage("[Images Button]").isFound.thisIsTrue()
                            it.findImageWithScrollRight("[This week Button]", scrollDurationSeconds = 0.2)
                        }
                        it.findImage("[This week Button]").isFound.thisIsTrue()
                    }
                    it.findImage("[Images Button]", throwsException = false).isFound.thisIsFalse()
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
                    it.macro("[Files Top Screen]")
                }.expectation {
                    it
                        .existImage("[Images Button]", waitSeconds = 0.2)
                        .dontExistImage("[This week Button]", waitSeconds = 0.2)
                        .withScrollRight("Images", scrollDurationSeconds = 0.2) {
                            it.existImage("[This week Button]")
                        }
                        .dontExistImage("[Images Button]")
                        .withScrollLeft("This week", scrollDurationSeconds = 0.2) {
                            it.existImage("[Images Button]")
                        }
                }
            }
            case(2) {
                expectation {
                    withScrollRight(scrollDurationSeconds = 0.2, scrollMaxCount = 3) {
                        it.existImage("[This week Button]", waitSeconds = 0.2)
                        withScrollLeft(scrollDurationSeconds = 0.2) {
                            it.existImage("[Images Button]")
                        }
                        it.existImage("[This week Button]")
                        withScrollLeft(scrollDurationSeconds = 0.2, scrollMaxCount = 3) {
                            it.existImage("[Images Button]")
                            withScrollRight(scrollDurationSeconds = 0.2) {
                                it.existImage("[This week Button]")
                            }
                            it.existImage("[Images Button]")
                            withScrollRight() {
                                it.existImage("[This week Button]")
                            }
                            it.existImage("[This week Button]")
                        }
                        withoutScroll {
                            it.dontExistImage("[Images Button]")
                        }
                        it.existImage("[This week Button]")
                    }
                    withScrollLeft {
                        it.existImage("[Images Button]")
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