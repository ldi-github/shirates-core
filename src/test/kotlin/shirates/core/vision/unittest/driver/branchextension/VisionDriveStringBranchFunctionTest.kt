package shirates.core.vision.unittest.driver.branchextension

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.vision.driver.branchextension.*


class VisionDriveStringBranchFunctionTest {

    @Test
    fun ifStringIs_true() {

        // Arrange
        var onTrueCalled = false

        // Act
        "Fujiyama".ifStringIs("Fujiyama") {
            onTrueCalled = true
        }

        // Assert
        assertThat(onTrueCalled).isTrue()

    }

    @Test
    fun ifStringIs_false() {

        // Arrange
        var onTrueCalled = false

        // Act
        "Fujuyama".ifStringIs("Sukiyaki") {
            onTrueCalled = true
        }

        // Assert
        assertThat(onTrueCalled).isFalse()
    }

    @Test
    fun ifStringIsNot_true() {

        // Arrange
        var onTrueCalled = false

        // Act
        "Fujiyama".ifStringIsNot("Fujiyama") {
            onTrueCalled = false
        }

        // Assert
        assertThat(onTrueCalled).isFalse()

    }

    @Test
    fun ifStringIsNot_false() {

        // Arrange
        var onTrueCalled = false

        // Act
        "Fujuyama".ifStringIsNot("Sukiyaki") {
            onTrueCalled = true
        }

        // Assert
        assertThat(onTrueCalled).isTrue()
    }

    @Test
    fun ifStartsWith_true() {

        // Arrange
        var onTrueCalled = false

        // Act
        "Fujiyama".ifStartsWith("Fuji") {
            onTrueCalled = true
        }

        // Assert
        assertThat(onTrueCalled).isTrue()
    }

    @Test
    fun ifStartsWith_false() {

        // Arrange
        var onTrueCalled = false

        // Act
        "Fujuyama".ifStartsWith("Suki") {
            onTrueCalled = true
        }

        // Assert
        assertThat(onTrueCalled).isFalse()
    }

    @Test
    fun ifContains_true() {

        // Arrange
        var onTrueCalled = false

        // Act
        "Fujiyama".ifContains("ujiyam") {
            onTrueCalled = true
        }

        // Assert
        assertThat(onTrueCalled).isTrue()
    }

    @Test
    fun ifContains_false() {

        // Arrange
        var onTrueCalled = false

        // Act
        "Fujiyama".ifContains("ukiya") {
            onTrueCalled = true
        }

        // Assert
        assertThat(onTrueCalled).isFalse()
    }

    @Test
    fun ifEndsWith_true() {

        // Arrange
        var onTrueCalled = false

        // Act
        "Fujiyama".ifEndsWith("yama") {
            onTrueCalled = true
        }

        // Assert
        assertThat(onTrueCalled).isTrue()
    }

    @Test
    fun ifEndsWith_false() {

        // Arrange
        var onTrueCalled = false

        // Act
        "Fujiyama".ifEndsWith("yaki") {
            onTrueCalled = true
        }

        // Assert
        assertThat(onTrueCalled).isFalse()
    }

    @Test
    fun ifMatches_true() {

        // Arrange
        var onTrueCalled = false

        // Act
        "Fujiyama".ifMatches("^F.*a$") {
            onTrueCalled = true
        }

        // Assert
        assertThat(onTrueCalled).isTrue()
    }

    @Test
    fun ifMatches_false() {

        // Arrange
        var onTrueCalled = false

        // Act
        "Fujiyama".ifMatches("^S.*i$") {
            onTrueCalled = true
        }

        // Assert
        assertThat(onTrueCalled).isFalse()
    }
}