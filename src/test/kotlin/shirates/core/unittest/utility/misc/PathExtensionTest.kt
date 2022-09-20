package shirates.core.unittest.utility.misc

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.testcode.UnitTest
import shirates.core.utility.listFiles
import shirates.core.utility.replacePathSeparators
import shirates.core.utility.toRelativePath
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
        val expected = shirates.core.UserVar.project.toFile().listFiles().toList().toString()
        // Act
        val actual = shirates.core.UserVar.project.listFiles().toString()
        // Assert
        assertThat(actual).isEqualTo(expected)
    }
}