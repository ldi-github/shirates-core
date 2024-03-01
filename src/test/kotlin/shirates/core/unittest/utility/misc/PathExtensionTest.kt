package shirates.core.unittest.utility.misc

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.testcode.UnitTest
import shirates.core.utility.*
import java.nio.file.Path

class PathExtensionTest : UnitTest() {

    @Test
    fun toRelativePath() {

        // Arrange
        val path = shirates.core.UserVar.project.resolve("dir1/subDir1")
        assertThat(path.toString()).isEqualTo("${shirates.core.UserVar.PROJECT}/dir1/subDir1".replacePathSeparators())

        run {
            // Arrange
            val expected = Path.of("dir1/subDir1")
            // Act
            val actual = path.toRelativePath()
            // Assert
            assertThat(actual).isEqualTo(expected)
        }
        run {
            // Arrange
            val expected = "${shirates.core.UserVar.PROJECT}/dir1/subDir1".replacePathSeparators()
            // Act
            val actual = path.toRelativePath().toAbsolutePath().toString()
            assertThat(actual).isEqualTo(expected)
        }
        run {
            // Arrange
            val relativePath = path.toRelativePath()
            val expected = relativePath
            // Act
            val actual = relativePath.toAbsolutePath().toRelativePath()
            // Assert
            assertThat(actual).isEqualTo(expected)
        }
    }

    @Test
    fun listFiles() {

        // Arrange
        val expected = shirates.core.UserVar.project.toFile().listFiles()?.toList().toString()
        // Act
        val actual = shirates.core.UserVar.project.listFiles().toString()
        // Assert
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun exists() {

        run {
            // Arrange
            val path = "testrun.global.properties".toPath()
            // Act
            val actual = path.exists()
            // Assert
            assertThat(actual).isTrue()
        }
        run {
            // Arrange
            val path = "noexist".toPath()
            // Act
            val actual = path.exists()
            // Assert
            assertThat(actual).isFalse()
        }
    }
}