package shirates.core.unittest.configuration

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import shirates.core.configuration.NicknameUtility
import shirates.core.driver.TestMode
import shirates.core.logging.Message.message
import shirates.core.testcode.UnitTest


class NicknameUtilityTest : UnitTest() {

    @Test
    fun getRuntimeNicknameValue() {


        TestMode.runAsAndroid {

            run {
                // Arrange, Act
                val runtime = NicknameUtility.getRuntimeNicknameValue("@a[#Android]:next, @i[#iOS]:next")
                // Assert
                assertThat(runtime).isEqualTo("[#Android]:next")
            }

            run {
                // Arrange, Act
                val runtime = NicknameUtility.getRuntimeNicknameValue("@i[#iOS]:next,@a[#Android]:next")
                // Assert
                assertThat(runtime).isEqualTo("[#Android]:next")
            }
        }

        TestMode.runAsIos {
            run {
                // Arrange, Act
                val runtime = NicknameUtility.getRuntimeNicknameValue("@a[#Android],@i[#iOS]")
                // Assert
                assertThat(runtime).isEqualTo("[#iOS]")
            }
            run {
                // Arrange, Act
                val runtime = NicknameUtility.getRuntimeNicknameValue("@i[#iOS],@a[#Android],")
                // Assert
                assertThat(runtime).isEqualTo("[#iOS]")
            }
        }
    }

    @Test
    fun validateNickname() {

        run {
            // Arrange
            val nickname = "[nickname]"
            // Act
            NicknameUtility.validateNickname(nickname)
        }

        run {
            // Arrange
            val nickname = "{nickname}"
            // Act
            NicknameUtility.validateNickname(nickname)
        }

        run {
            // Arrange
            val nickname = ""
            // Act
            assertThatThrownBy {
                NicknameUtility.validateNickname(nickname)
            }.isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage(message(id = "nicknameIsBlank"))
        }

        run {
            // Arrange
            val nickname = "[nickname"
            // Act
            assertThatThrownBy {
                NicknameUtility.validateNickname(nickname)
            }.isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage(message(id = "nicknameFormatError", subject = nickname))
        }

        run {
            // Arrange
            val nickname = "nickname]"
            // Act
            assertThatThrownBy {
                NicknameUtility.validateNickname(nickname)
            }.isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage(message(id = "nicknameFormatError", subject = nickname))
        }

        run {
            // Arrange
            val nickname = "{nickname"
            // Act
            assertThatThrownBy {
                NicknameUtility.validateNickname(nickname)
            }.isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage(message(id = "nicknameFormatError", subject = nickname))
        }

        run {
            // Arrange
            val nickname = "nickname}"
            // Act
            assertThatThrownBy {
                NicknameUtility.validateNickname(nickname)
            }.isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage(message(id = "nicknameFormatError", subject = nickname))
        }

        run {
            // Arrange
            val nickname = "[nickname}"
            // Act
            assertThatThrownBy {
                NicknameUtility.validateNickname(nickname)
            }.isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage(message(id = "nicknameFormatError", subject = nickname))
        }
    }

    @Test
    fun validateScreenName() {

        run {
            // Arrange
            val screenName = "[screenName]"
            // Act
            NicknameUtility.validateScreenName(screenName)
        }

        run {
            // Arrange
            val screenName = ""
            // Act
            assertThatThrownBy {
                NicknameUtility.validateScreenName(screenName)
            }.isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage(message(id = "screenNameIsBlank"))
        }

        run {
            // Arrange
            val screenName = "[screenName"
            // Act
            assertThatThrownBy {
                NicknameUtility.validateScreenName(screenName)
            }.isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage(message(id = "screenNameFormatError", subject = screenName))
        }

    }

    @Test
    fun getText() {

        run {
            // Arrange, Act
            val actual = NicknameUtility.getNicknameText("[text1]")
            // Assert
            assertThat(actual).isEqualTo("text1")
        }

        run {
            // Arrange, Act
            val actual = NicknameUtility.getNicknameText("{text1}")
            // Assert
            assertThat(actual).isEqualTo("text1")
        }

        run {
            // Arrange, Act
            val actual = NicknameUtility.getNicknameText("[text1}")
            // Assert
            assertThat(actual).isEqualTo("")
        }

        run {
            // Arrange, Act
            val actual = NicknameUtility.getNicknameText("{text1]")
            // Assert
            assertThat(actual).isEqualTo("")
        }

        run {
            // Arrange, Act
            val actual = NicknameUtility.getNicknameText("text1")
            // Assert
            assertThat(actual).isEqualTo("")
        }
    }

    @Test
    fun isValidNickname() {

        run {
            // Arrange, Act
            val actual = NicknameUtility.isValidNickname("[text1]")
            // Assert
            assertThat(actual).isTrue()
        }

        run {
            // Arrange, Act
            val actual = NicknameUtility.isValidNickname("{text1}")
            // Assert
            assertThat(actual).isTrue()
        }

        run {
            // Arrange, Act
            val actual = NicknameUtility.isValidNickname("{text1]")
            // Assert
            assertThat(actual).isFalse()
        }

        run {
            // Arrange, Act
            val actual = NicknameUtility.isValidNickname("[text1}")
            // Assert
            assertThat(actual).isFalse()
        }

        run {
            // Arrange, Act
            val actual = NicknameUtility.isValidNickname("text1")
            // Assert
            assertThat(actual).isFalse()
        }

        run {
            // Arrange, Act
            val actual = NicknameUtility.isValidNickname("[text1]:next")
            // Assert
            assertThat(actual).isFalse()
        }

        run {
            // Arrange, Act
            val actual = NicknameUtility.isValidNickname("[text1][:Left button]:rightButton(2):belowButton(2)")
            // Assert
            assertThat(actual).isFalse()
        }

        run {
            // Arrange, Act
            val actual = NicknameUtility.isValidNickname("[1]:rightButton(2):belowButton(2)[:Left button]")
            // Assert
            assertThat(actual).isFalse()
        }
    }

    @Test
    fun splitNicknames() {

        run {
            // Arrange
            val target = "[name1][name2]{name3}{name4}[name5][name6]"
            // Act
            val actual = NicknameUtility.splitNicknames(target)
            // Assert
            assertThat(actual.count()).isEqualTo(6)
            assertThat(actual[0]).isEqualTo("[name1]")
            assertThat(actual[1]).isEqualTo("[name2]")
            assertThat(actual[2]).isEqualTo("{name3}")
            assertThat(actual[3]).isEqualTo("{name4}")
        }

        run {
            // Arrange
            val target = "[name1][]{name3}"
            // Act
            val actual = NicknameUtility.splitNicknames(target)
            // Assert
            assertThat(actual.count()).isEqualTo(3)
            assertThat(actual[0]).isEqualTo("[name1]")
            assertThat(actual[1]).isEqualTo("[]")
            assertThat(actual[2]).isEqualTo("{name3}")
        }

    }
}