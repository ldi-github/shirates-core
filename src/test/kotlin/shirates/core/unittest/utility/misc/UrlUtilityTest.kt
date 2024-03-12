package shirates.core.unittest.utility.misc

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.testcode.UnitTest
import shirates.core.utility.misc.UrlUtility

class UrlUtilityTest : UnitTest() {

    @Test
    fun isLocal_true() {

        run {
            // Arrange, Act
            val actual = UrlUtility.isLocal("http://127.0.0.1:8080")
            // Assert
            assertThat(actual).isTrue()
        }
        run {
            // Arrange, Act
            val actual = UrlUtility.isLocal("http://localhost:8080")
            // Assert
            assertThat(actual).isTrue()
        }
        run {
            // Arrange, Act
            val actual = UrlUtility.isLocal("http://::1:8080")
            // Assert
            assertThat(actual).isTrue()
        }
    }

    @Test
    fun isLocal_false() {

        run {
            // Arrange, Act
            val actual = UrlUtility.isLocal("http://host1:8080")
            // Assert
            assertThat(actual).isFalse()
        }
        run {
            // Arrange, Act
            val actual = UrlUtility.isLocal("http://host1.example.com:8080")
            // Assert
            assertThat(actual).isFalse()
        }
    }

    @Test
    fun isRemote_false() {

        run {
            // Arrange, Act
            val actual = UrlUtility.isRemote("http://127.0.0.1:8080")
            // Assert
            assertThat(actual).isFalse()
        }
        run {
            // Arrange, Act
            val actual = UrlUtility.isRemote("http://localhost:8080")
            // Assert
            assertThat(actual).isFalse()
        }
        run {
            // Arrange, Act
            val actual = UrlUtility.isRemote("http://::1:8080")
            // Assert
            assertThat(actual).isFalse()
        }
    }

    @Test
    fun isRemote_true() {

        run {
            // Arrange, Act
            val actual = UrlUtility.isRemote("http://host1:8080")
            // Assert
            assertThat(actual).isTrue()
        }
        run {
            // Arrange, Act
            val actual = UrlUtility.isRemote("http://host1.example.com:8080")
            // Assert
            assertThat(actual).isTrue()
        }
    }
}