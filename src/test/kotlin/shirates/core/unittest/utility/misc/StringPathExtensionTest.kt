package shirates.core.unittest.utility.misc

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import shirates.core.driver.TestMode
import shirates.core.logging.TestLog
import shirates.core.testcode.UnitTest
import shirates.core.utility.*
import java.nio.file.Path

class StringPathExtensionTest : UnitTest() {

    @Test
    fun replacePathSeparators() {

        run {
            // Arrange
            val expected = if (TestMode.isRunningOnWindows) "\\a\\b\\c" else "/a/b/c"
            // Actual
            val actual = "\\a\\b\\c".replacePathSeparators()
            // Assert
            assertThat(actual).isEqualTo(expected)
        }
        run {
            // Arrange
            val expected = if (TestMode.isRunningOnWindows) "\\a\\b\\c" else "/a/b/c"
            // Actual
            val actual = "/a/b/c".replacePathSeparators()
            // Assert
            assertThat(actual).isEqualTo(expected)
        }
    }

    @Test
    fun replaceUserVars() {

        val userHome = System.getProperty("user.home")

        run {
            // Arrange
            val target = "{USER_HOME}/Downloads".replacePathSeparators()
            val expected = target.replace("{USER_HOME}", userHome).replacePathSeparators()
            run {
                // Act
                val actual = target.replaceUserHome().replacePathSeparators()
                // Assert
                assertThat(actual).isEqualTo(expected)
            }
            run {
                // Act
                val actual = target.replaceUserVars().replacePathSeparators()
                // Assert
                assertThat(actual).isEqualTo(expected)
            }
        }
        run {
            // Arrange
            val target = "{DOWNLOADS}/dir1".replacePathSeparators()
            val expected = target.replace("{DOWNLOADS}", "$userHome/Downloads").replacePathSeparators()
            run {
                // Act
                val actual = target.replaceDownloads().replacePathSeparators()
                // Assert
                assertThat(actual).isEqualTo(expected)
            }
            run {
                // Act
                val actual = target.replaceUserVars().replacePathSeparators()
                // Assert
                assertThat(actual).isEqualTo(expected)
            }
        }
        run {
            // Arrange
            val target = "{TEST_RESULTS}/dir1".replacePathSeparators()
            val expected = target.replace("{TEST_RESULTS}", "$userHome/Downloads/TestResults").replacePathSeparators()
            run {
                // Act
                val actual = target.replaceTestResults().replacePathSeparators()
                // Assert
                assertThat(actual).isEqualTo(expected)
            }
            run {
                // Act
                val actual = target.replaceUserVars().replacePathSeparators()
                // Assert
                assertThat(actual).isEqualTo(expected)
            }
        }
        run {
            // Arrange
            val target = "{DIRECTORY_FOR_TEST_CONFIG}/dir1".replacePathSeparators()
            val expected = target.replace("{DIRECTORY_FOR_TEST_CONFIG}", TestLog.directoryForTestConfig.toString())
                .replacePathSeparators()
            run {
                // Act
                val actual = target.replaceDirectoryForTestConfig().replacePathSeparators()
                // Assert
                assertThat(actual).isEqualTo(expected)
            }
            run {
                // Act
                val actual = target.replaceUserVars().replacePathSeparators()
                // Assert
                assertThat(actual).isEqualTo(expected)
            }
        }
        run {
            // Arrange
            val target = "{DIRECTORY_FOR_LOG}/dir1".replacePathSeparators()
            val expected =
                target.replace("{DIRECTORY_FOR_LOG}", TestLog.directoryForLog.toString()).replacePathSeparators()
            run {
                // Act
                val actual = target.replaceDirectoryForLog().replacePathSeparators()
                // Assert
                assertThat(actual).isEqualTo(expected)
            }
            run {
                // Act
                val actual = target.replaceUserVars().replacePathSeparators()
                // Assert
                assertThat(actual).isEqualTo(expected)
            }
        }
    }

    @Test
    fun toPath() {

        run {
            // Arrange
            val expected = shirates.core.UserVar.project
            // Act
            val actual = "".toPath()
            // Assert
            assertThat(expected).isEqualTo(actual)
        }
    }

    @Test
    fun toRelativePath() {

        run {
            // Arrange
            val str = "${shirates.core.UserVar.PROJECT}/dir1/subDir1"
            val expected = Path.of("dir1/subDir1")
            // Act
            val actual = str.toRelativePath()
            // Assert
            assertThat(actual).isEqualTo(expected)
        }
        run {
            // Arrange
            val str = "/Users/foo/bar/dir1/subDir1"
            // Act, Assert
            assertThatThrownBy {
                str.toRelativePath()
            }.isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("Failed to convert the string to relative path from project root. (string=\"$str\", projectRoot=${shirates.core.UserVar.PROJECT})")
        }
    }

    @Test
    fun escapeFileName() {

        run {
            // Act
            val actual = "\\/:*?\\<>|CRLF\r\nCR\nLF\n.png".escapeFileName()
            // Assert
            assertThat(actual).isEqualTo("______CRLF_CR_LF_.png")
        }

        run {
            // Arrange
            var longName300 = ""
            for (i in 1..30) {
                longName300 += "1234567890"
            }
            // Act
            val actual = longName300.escapeFileName()
            // Assert
            assertThat(actual.length).isEqualTo(100)
        }

    }

}