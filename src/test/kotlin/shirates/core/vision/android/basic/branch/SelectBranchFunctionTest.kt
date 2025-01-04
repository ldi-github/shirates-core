package shirates.core.vision.android.basic.branch

import ifCanDetect
import ifCanDetectNot
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.vision.driver.commandextension.screenIs
import shirates.core.vision.testcode.VisionTest

@Testrun("unitTestConfig/vision/android/androidSettings/testrun.properties")
class SelectBranchFunctionTest : VisionTest() {

    @Test
    fun ifCanSelectTest() {

        // Arrange
        it.screenIs("[Android Settings Top Screen]")

        run {
            // Arrange
            var ifCanSelectCalled = false
            var notCalled = false
            // Act
            ifCanDetect("no exist") {
                ifCanSelectCalled = true     // never called
            }.ifElse {
                notCalled = true
            }
            // Assert
            assertThat(ifCanSelectCalled).isFalse()
            assertThat(notCalled).isTrue()
        }
        run {
            // Arrange
            var ifCanSelectCalled = false
            var notCalled = false
            // Act
            ifCanDetect("Connected devices") {
                ifCanSelectCalled = true     // called
            }.ifElse {
                notCalled = true    // never called
            }
            // Assert
            assertThat(ifCanSelectCalled).isTrue()
            assertThat(notCalled).isFalse()
        }
        run {
            // Arrange
            var elseCalled = false
            // Act
            ifCanDetect("no exist")
                .ifElse {
                    elseCalled = true    // never called
                }
            // Assert
            assertThat(elseCalled).isTrue()
        }

    }

    @Test
    fun ifCanSelectNotTest() {

        // Arrange
        it.screenIs("[Android Settings Top Screen]")

        run {
            // Arrange
            var ifCanSelectNotCalled = false
            var notCalled = false
            // Act
            ifCanDetectNot("no exist") {
                ifCanSelectNotCalled = true     // called
            }.ifElse {
                notCalled = true    // never called
            }
            // Assert
            assertThat(ifCanSelectNotCalled).isTrue()
            assertThat(notCalled).isFalse()
        }
        run {
            // Arrange
            var ifCanSelectNotCalled = false
            var notCalled = false
            // Act
            ifCanDetectNot("Connected devices") {
                ifCanSelectNotCalled = true     // never called
            }.ifElse {
                notCalled = true    // called
            }
            // Assert
            assertThat(ifCanSelectNotCalled).isFalse()
            assertThat(notCalled).isTrue()
        }
    }

}