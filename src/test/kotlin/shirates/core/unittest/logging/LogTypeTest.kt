package shirates.core.unittest.logging

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.logging.LogType
import shirates.core.testcode.UnitTest

class LogTypeTest : UnitTest() {

    @Test
    fun AllTypes() {

        // Arrange
        val expected = mutableListOf(
            LogType.NONE,
            LogType.TRACE,
            LogType.INFO,
            LogType.WARN,
            LogType.ERROR,
            LogType.OK,
            LogType.ok,
            LogType.NG,
            LogType.MANUAL,
            LogType.SKIP,
            LogType.NOTIMPL,
            LogType.KNOWNISSUE,
            LogType.SCENARIO,
            LogType.CASE,
            LogType.CONDITION,
            LogType.ACTION,
            LogType.TARGET,
            LogType.EXPECTATION,
            LogType.SELECT,
            LogType.BOOLEAN,
            LogType.BRANCH,
            LogType.SKIP_SCENARIO,
            LogType.SKIP_CASE,
            LogType.MANUAL_SCENARIO,
            LogType.MANUAL_CASE,
            LogType.OPERATE,
            LogType.SCREENSHOT,
            LogType.CHECK,
            LogType.PROCEDURE,
            LogType.SILENT,
            LogType.WITHSCROLL,
            LogType.CAPTION,
            LogType.DESCRIBE,
            LogType.OUTPUT,
            LogType.COMMENT,
        )
        // Act
        val actual = LogType.AllTypes
        // Assert
        assertThat(actual).containsExactlyElementsOf(expected)
    }

    @Test
    fun EffectedTypes() {

        // Arrange
        val expected = mutableListOf(
            LogType.ERROR,
            LogType.OK,
            LogType.NG,
            LogType.SKIP,
            LogType.MANUAL,
            LogType.NOTIMPL,
            LogType.KNOWNISSUE
        )
        // Act
        val actual = LogType.EffectiveTypes
        // Assert
        assertThat(actual).containsExactlyElementsOf(expected)
    }

    @Test
    fun FailTypes() {

        // Arrange
        val expected = mutableListOf(
            LogType.ERROR,
            LogType.NG
        )
        // Act
        val actual = LogType.FailTypes
        // Assert
        assertThat(actual).containsExactlyElementsOf(expected)
    }

    @Test
    fun InconclusiveTypes() {

        // Arrange
        val expected = mutableListOf(
            LogType.SKIP,
            LogType.MANUAL,
            LogType.NOTIMPL,
            LogType.KNOWNISSUE
        )
        // Act
        val actual = LogType.InconclusiveTypes
        // Assert
        assertThat(actual).containsExactlyElementsOf(expected)
    }

    @Test
    fun CAEPatternTypes() {

        // Arrange
        val expected = mutableListOf<LogType>(
            LogType.CONDITION,
            LogType.ACTION,
            LogType.EXPECTATION
        )

        // Act
        val actual = LogType.CAEPatternTypes

        // Assert
        assertThat(actual).containsExactlyElementsOf(expected)
    }

}