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
class VisionDriveImageExtensionTest2 : VisionTest() {

    @Test
    @Order(10)
    fun findImage_canFindImage() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Files Top Screen]")
                }.expectation {
                    var imagesIcon = it.findImage("[Images Button]", segmentMarginHorizontal = 5)
                    imagesIcon.isFound.thisIsTrue()

                    imagesIcon.onLine {
                        val thisWeekIcon =
                            it.findImageWithScrollRight("[This week Button]", segmentMarginHorizontal = 5)
                        thisWeekIcon.isFound.thisIsTrue()

                        imagesIcon = it.findImageWithScrollLeft("[Images Button]", segmentMarginHorizontal = 5)
                        imagesIcon.isFound.thisIsTrue()
                    }
                }
            }
            case(2) {
                expectation {
                    it.canFindImage("[Apps Icon]", segmentMarginHorizontal = 5)
                        .thisIsFalse("canFindImage([Apps Icon]) is false")
                    it.canFindImage("[Images Button]", segmentMarginHorizontal = 5)
                        .thisIsTrue("canFIndImage([Images Button]) is true")
                }
            }
            case(3) {
                expectation {
                    withScrollRight {
                        it.findImage("[This week Button]", segmentMarginHorizontal = 5).isFound.thisIsTrue()
                        withScrollLeft {
                            it.findImage("[Images Button]", segmentMarginHorizontal = 5).isFound.thisIsTrue()
                            it.findImageWithScrollRight(
                                "[This week Button]",
                                segmentMarginHorizontal = 5,
                                scrollDurationSeconds = 0.2
                            )
                        }
                        it.findImage("[This week Button]", segmentMarginHorizontal = 5).isFound.thisIsTrue()
                    }
                    it.findImage(
                        "[Images Button]",
                        segmentMarginHorizontal = 5,
                        throwsException = false
                    ).isFound.thisIsFalse()
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
                        .existImage("[Images Button]", segmentMarginHorizontal = 5, waitSeconds = 0.2)
                        .dontExistImage("[This week Button]", segmentMarginHorizontal = 5, waitSeconds = 0.2)
                        .withScrollRight("Images", scrollDurationSeconds = 0.2) {
                            it.existImage("[This week Button]", segmentMarginHorizontal = 5)
                        }
                        .dontExistImage("[Images Button]", segmentMarginHorizontal = 5)
                        .withScrollLeft("This week", scrollDurationSeconds = 0.2) {
                            it.existImage("[Images Button]", segmentMarginHorizontal = 5)
                        }
                }
            }
            case(2) {
                expectation {
                    withScrollRight(scrollDurationSeconds = 0.2, scrollMaxCount = 3) {
                        it.existImage("[This week Button]", segmentMarginHorizontal = 5, waitSeconds = 0.2)
                        withScrollLeft(scrollDurationSeconds = 0.2) {
                            it.existImage("[Images Button]", segmentMarginHorizontal = 5)
                        }
                        it.existImage("[This week Button]", segmentMarginHorizontal = 5)
                        withScrollLeft(scrollDurationSeconds = 0.2, scrollMaxCount = 3) {
                            it.existImage("[Images Button]", segmentMarginHorizontal = 5)
                            withScrollRight(scrollDurationSeconds = 0.2) {
                                it.existImage("[This week Button]", segmentMarginHorizontal = 5)
                            }
                            it.existImage("[Images Button]", segmentMarginHorizontal = 5)
                            withScrollRight() {
                                it.existImage("[This week Button]", segmentMarginHorizontal = 5)
                            }
                            it.existImage("[This week Button]", segmentMarginHorizontal = 5)
                        }
                        withoutScroll {
                            it.dontExistImage("[Images Button]", segmentMarginHorizontal = 5)
                        }
                        it.existImage("[This week Button]", segmentMarginHorizontal = 5)
                    }
                    withScrollLeft {
                        it.existImage("[Images Button]", segmentMarginHorizontal = 5)
                    }
                }
            }
        }
    }

}