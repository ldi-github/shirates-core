package shirates.core.unittest.exception

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.exception.*

class ExceptionTest {

    @Test
    fun branceException() {

        // Arrange
        val cause = Exception()
        val e = BranchException(message = "message", cause = cause)
        // Act, Assert
        assertThat(e.message).isEqualTo("message")
        assertThat(e.cause).isEqualTo(cause)
    }

    @Test
    fun caseSkipException() {

        // Arrange
        val cause = Exception()
        val e = CaseSkipException(message = "message", cause = cause)
        // Act, Assert
        assertThat(e.message).isEqualTo("message")
        assertThat(e.cause).isEqualTo(cause)
    }

    @Test
    fun scenarioSkipException() {

        // Arrange
        val cause = Exception()
        val e = ScenarioSkipException(message = "message", cause = cause)
        // Act, Assert
        assertThat(e.message).isEqualTo("message")
        assertThat(e.cause).isEqualTo(cause)
    }

    @Test
    fun testConfigException() {

        // Arrange
        val cause = Exception()
        val e = TestConfigException(message = "message", cause = cause)
        // Act, Assert
        assertThat(e.message).isEqualTo("message")
        assertThat(e.cause).isEqualTo(cause)
    }

    @Test
    fun testDriverException() {

        // Arrange
        val cause = Exception()
        val e = TestDriverException(message = "message", cause = cause)
        // Act, Assert
        assertThat(e.message).isEqualTo("message")
        assertThat(e.cause).isEqualTo(cause)
    }

    @Test
    fun testEnvironmentException() {

        // Arrange
        val cause = Exception()
        val e = TestEnvironmentException(message = "message", cause = cause)
        // Act, Assert
        assertThat(e.message).isEqualTo("message")
        assertThat(e.cause).isEqualTo(cause)
    }

    @Test
    fun testNGException() {

        // Arrange
        val cause = Exception()
        val e = TestNGException(message = "message", cause = cause)
        // Act, Assert
        assertThat(e.message).isEqualTo("message")
        assertThat(e.cause).isEqualTo(cause)
    }

}