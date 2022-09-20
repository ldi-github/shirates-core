package shirates.core.unittest.configuration

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import shirates.core.configuration.getNicknameText
import shirates.core.configuration.isValidNickname
import shirates.core.exception.TestDriverException
import shirates.core.testcode.UnitTest

class StringNicknameExtensionTest : UnitTest() {

    @Test
    fun isValidNickname() {

        // Act, Assert
        assertThat("[title]".isValidNickname()).isTrue()
        assertThat("[title".isValidNickname()).isFalse()
        assertThat("title".isValidNickname()).isFalse()
        assertThat("[title]:next".isValidNickname()).isFalse()
        assertThat("[title]:[title2]".isValidNickname()).isTrue()
    }

    @Test
    fun getNicknameText() {

        // Act, Assert
        assertThat("[title]".getNicknameText()).isEqualTo("title")

        // Act, Assert
        assertThatThrownBy {
            "title".getNicknameText()
        }.isInstanceOf(TestDriverException::class.java)
            .hasMessage("This is not nickname. (this=title)")
    }
}