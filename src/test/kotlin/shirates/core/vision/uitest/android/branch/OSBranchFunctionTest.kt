package shirates.core.vision.uitest.android.branch

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.TestMode
import shirates.core.vision.driver.branchextension.android
import shirates.core.vision.driver.branchextension.ios
import shirates.core.vision.testcode.VisionTest

@Testrun("unitTestConfig/vision/android/androidSettings/testrun.properties")
class OSBranchFunctionTest : VisionTest() {

    @Test
    fun androidTest() {

        TestMode.runAsAndroid {

            // Arrange
            var androidCalled = false
            // Act
            android {
                androidCalled = true
            }
            // Assert
            assertThat(androidCalled).isTrue()
        }

        TestMode.runAsIos {

            // Arrange
            var androidCalled = false
            // Act
            android {
                androidCalled = true
            }
            // Assert
            assertThat(androidCalled).isFalse()
        }

    }

    @Test
    fun iosTest() {

        TestMode.runAsIos {

            // Arrange
            var iosCalled = false
            // Act
            ios {
                iosCalled = true
            }
            // Assert
            assertThat(iosCalled).isTrue()
        }

        TestMode.runAsAndroid {

            // Arrange
            var iosCalled = false
            // Act
            ios {
                iosCalled = true
            }
            // Assert
            assertThat(iosCalled).isFalse()
        }
    }

}