package shirates.core.unittest

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.testcode.UnitTest
import shirates.core.utility.toPath
import java.io.File

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