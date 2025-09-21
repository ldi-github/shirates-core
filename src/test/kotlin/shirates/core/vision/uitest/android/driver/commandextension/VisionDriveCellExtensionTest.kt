// ToDo: Delete
//package shirates.core.vision.uitest.android.driver.commandextension
//
//import org.junit.jupiter.api.Order
//import org.junit.jupiter.api.Test
//import shirates.core.configuration.Testrun
//import shirates.core.vision.driver.commandextension.*
//import shirates.core.vision.testcode.VisionTest
//
//@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
//class VisionDriveCellExtensionTest : VisionTest() {
//
//    @Test
//    @Order(10)
//    fun cell_onCellOf() {
//
//        scenario {
//            case(1) {
//                condition {
//                    it.macro("[Alarms Screen]")
//                }.expectation {
//                    it.detect("8:30AM")
//                        .cell()
//                        .onThisElementRegion {
//                            it.exist("Mon, Tue, Wed, Thu, Fri")
//                            it.dontExist("Sun, Sat")
//                        }
//                    it.detect("9:00AM")
//                        .cell()
//                        .onThisElementRegion {
//                            it.dontExist("Mon, Tue, Wed, Thu, Fri")
//                            it.exist("Sun, Sat")
//                        }
//                }
//            }
//            case(2) {
//                expectation {
//                    it.detect("8:30AM").onCell {
//                        it.exist("Mon, Tue, Wed, Thu, Fri")
//                        it.dontExist("Sun, Sat")
//                    }
//                    it.detect("9:00AM").onCell {
//                        it.dontExist("Mon, Tue, Wed, Thu, Fri")
//                        it.exist("Sun, Sat")
//                    }
//                }
//            }
//            case(3) {
//                expectation {
//                    it.onCellOf("8:30AM") {
//                        it.exist("Mon, Tue, Wed, Thu, Fri")
//                        it.dontExist("Sun, Sat")
//                    }
//                    it.onCellOf("9:00AM") {
//                        it.dontExist("Mon, Tue, Wed, Thu, Fri")
//                        it.exist("Sun, Sat")
//                    }
//                }
//            }
//            case(4) {
//                expectation {
//                    it.onCellOfWithScrollDown("8:30AM") {
//                        it.exist("Mon, Tue, Wed, Thu, Fri")
//                        it.dontExist("Sun, Sat")
//                    }
//                    it.onCellOfWithScrollDown("9:00AM") {
//                        it.dontExist("Mon, Tue, Wed, Thu, Fri")
//                        it.exist("Sun, Sat")
//                    }
//                }
//            }
//        }
//    }
//
//}