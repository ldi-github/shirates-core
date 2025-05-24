package shirates.core.vision.uitest.android.driver.commandextension

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.vision.driver.commandextension.*
import shirates.core.vision.testcode.VisionTest

@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
class VisionDriveRegionExtensionTest : VisionTest() {

    @Test
    @Order(10)
    fun settingWorkingRegion() {

        scenario {
            case(1) {
                condition {
                    it.screenIs("[Android Settings Top Screen]")
                }.expectation {
                    onTopRegion(topRate = 0.35) {
                        it.exist("Search settings")
                        it.dontExist("Storage")
                    }
                }
            }
            case(2) {
                expectation {
                    onBottomRegion {
                        it.dontExist("Search settings")
                        it.exist("Storage")
                    }
                }
            }
            case(3) {
                expectation {
                    onMiddleRegion(upperRate = 0.1, lowerRate = 0.2) {
                        it.dontExist("Search settings")
                        it.exist("Apps")
                        it.exist("Notifications")
                    }
                }
            }
            case(4) {
                expectation {
                    val v = detect("Search settings")
                    val r = v.rect
                    onRegion(left = r.left, top = r.top, right = r.right, bottom = r.bottom) {
                        it.exist("Search settings")
                    }
                }
            }
            case(5) {
                expectation {
                    onUpperHalfRegion {
                        it.exist("Search settings")
                        it.dontExist("Storage")
                    }
                }
            }
            case(6) {
                expectation {
                    onLowerHalfRegion {
                        it.dontExist("Search settings")
                        it.exist("Storage")
                    }
                }
            }
            case(7) {
                expectation {
                    onLeftHalfRegion {
                        it.existImage("[Network & internet Icon]")
                    }
                }
            }
            case(8) {
                expectation {
                    onRightHalfRegion {
                        it.dontExistImage("[Network & internet Icon]")
                    }
                }
            }
        }
    }

    @Test
    @Order(20)
    fun cell_cellOf() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Alarm Screen]")
                }.expectation {
                    it.detect("8:30AM")
                        .cell()
                        .onThisElementRegion {
                            it.exist("Mon, Tue, Wed, Thu, Fri")
                            it.dontExist("Sun, Sat")
                        }
                    it.detect("9:00AM")
                        .cell()
                        .onThisElementRegion {
                            it.dontExist("Mon, Tue, Wed, Thu, Fri")
                            it.exist("Sun, Sat")
                        }
                }
            }
            case(2) {
                expectation {
                    it.cellOf("8:30AM") {
                        it.exist("Mon, Tue, Wed, Thu, Fri")
                        it.dontExist("Sun, Sat")
                    }
                    it.cellOf("9:00AM") {
                        it.dontExist("Mon, Tue, Wed, Thu, Fri")
                        it.exist("Sun, Sat")
                    }
                }
            }
        }
    }

}