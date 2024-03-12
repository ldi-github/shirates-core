package shirates.core.unittest

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.UserVar
import shirates.core.testcode.UnitTest
import shirates.core.utility.toPath
import java.io.File
import java.nio.file.Path

class UserVarTest : UnitTest() {

    @Test
    fun project() {

        run {
            // Arrange, Act
            val s = File.separator
            val projectUri = System.getProperty("user.dir").replace("${s}src${s}test", "")
            val actual = shirates.core.UserVar.project
            // Assert
            assertThat(actual.toString()).isEqualTo(projectUri)
        }
        run {
            // Arrange
            val expected = System.getProperty("user.dir")
            // Act
            val actual = shirates.core.UserVar.PROJECT
            // Assert
            assertThat(actual).isEqualTo(expected)
        }
        run {
            // Arrange
            val expected = System.getProperty("user.dir").toPath()
            // Act
            val actual = shirates.core.UserVar.project
            // Assert
            assertThat(actual).isEqualTo(expected)
        }
    }

    @Test
    fun project_systemProperty() {

        val original = System.getProperty(UserVar.SHIRATES_PROJECT_DIR)
        try {
            run {
                // Arrange
                System.setProperty(UserVar.SHIRATES_PROJECT_DIR, UserVar.USER_HOME)
                // Act, Assert
                assertThat(UserVar.PROJECT).isEqualTo(UserVar.USER_HOME)
                assertThat(UserVar.project).isEqualTo(UserVar.userHome)
            }
            run {
                // Arrange
                System.setProperty(UserVar.SHIRATES_PROJECT_DIR, "/Users/wave1008/Downloads")
                val expected = Path.of("/Users/wave1008/Downloads").toAbsolutePath()
                // Act, Assert
                assertThat(UserVar.PROJECT).isEqualTo("/Users/wave1008/Downloads")
                assertThat(UserVar.project).isEqualTo(expected)
            }
        } finally {
            if (original == null) {
                System.clearProperty(UserVar.SHIRATES_PROJECT_DIR)
            } else {
                System.setProperty(UserVar.SHIRATES_PROJECT_DIR, original)
            }
        }
    }

    @Test
    fun userHome() {

        run {
            // Arrange
            val expected = System.getProperty("user.home")
            // Act
            val actual = shirates.core.UserVar.USER_HOME
            // Assert
            assertThat(actual).isEqualTo(expected)
        }
        run {
            // Arrange
            val expected = System.getProperty("user.home").toPath()
            // Act
            val actual = shirates.core.UserVar.userHome
            // Assert
            assertThat(actual).isEqualTo(expected)
        }
    }

    @Test
    fun download() {

        run {
            // Arrange
            val expected = shirates.core.UserVar.userHome.resolve("Downloads").toString()
            // Act
            val actual = shirates.core.UserVar.DOWNLOADS
            // Assert
            assertThat(actual).isEqualTo(expected)
        }
        run {
            // Arrange
            val expected = shirates.core.UserVar.userHome.resolve("Downloads")
            // Act
            val actual = shirates.core.UserVar.downloads
            // Assert
            assertThat(expected).isEqualTo(actual)
        }
    }

}