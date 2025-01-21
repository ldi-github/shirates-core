package shirates.core.uitest.android.basic.branch

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.branchextension.ifCanSelect
import shirates.core.driver.branchextension.ifCanSelectNot
import shirates.core.driver.commandextension.screenIs
import shirates.core.testcode.UITest

@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
class SelectBranchFunctionTest : UITest() {

    @Test
    fun ifCanSelectTest() {

        // Arrange
        it.screenIs("[Android Settings Top Screen]")

        run {
            // Arrange
            var ifCanSelectCalled = false
            var notCalled = false
            // Act
            ifCanSelect("no exist") {
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
            ifCanSelect("Connected devices") {
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
            ifCanSelect("no exist")
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
            ifCanSelectNot("no exist") {
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
            ifCanSelectNot("Connected devices") {
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