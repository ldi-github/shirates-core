//package shirates.core.vision.testcode.ios.basic.driver
//
//import org.assertj.core.api.Assertions.assertThat
//import org.junit.jupiter.api.Test
//import shirates.core.configuration.Testrun
//import shirates.core.driver.TestMode
//import shirates.core.driver.imageProfile
//import shirates.core.driver.viewBounds
//import shirates.core.testcode.UITest
//import shirates.core.vision.testcode.VisionTest
//
//@Testrun("unitTestConfig/ios/iOSSettings/testrun.properties")
//class TestDrivePropertyExtensionTest : VisionTest() {
//
//    @Test
//    fun imageProfile() {
//
//        scenario {
//            case(1) {
//                val b = viewBounds
//                assertThat(imageProfile).isEqualTo("${TestMode.platformAnnotation}_${b.width}x${b.height}")
//            }
//        }
//    }
//
//}