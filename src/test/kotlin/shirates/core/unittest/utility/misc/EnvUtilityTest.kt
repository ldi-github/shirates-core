package shirates.core.unittest.utility.misc

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.testcode.UnitTest
import shirates.core.utility.misc.EnvUtility

class EnvUtilityTest : UnitTest() {

    @Test
    fun reset_setEnvForTesting() {

        // Arrange
        val systemEnv = System.getenv()
        // Act
        EnvUtility.reset()
        // Assert
        assertThat(EnvUtility.getEnv().count()).isEqualTo(System.getenv().count())

        // Arrange
        EnvUtility.setEnvForTesting(name = "name1", value = "value1")
        assertThat(EnvUtility.getEnv().count()).isEqualTo(systemEnv.count() + 1)
        // Act
        EnvUtility.reset()
        // Assert
        assertThat(EnvUtility.getEnv().count()).isEqualTo(System.getenv().count())
    }

    @Test
    fun getEnv() {

        // Arrange
        val systemEnv = System.getenv()
        // Act
        val envMap = EnvUtility.getEnv()
        // Assert
        assertThat(envMap.count()).isEqualTo(systemEnv.count())
        for (e in systemEnv) {
            assertThat(envMap[e.key]).isEqualTo(e.value)
        }
    }

    @Test
    fun getEnvValue() {

        run {
            // Arrange
            val systemEnv = System.getenv()
            // Act, Assert
            for (e in systemEnv) {
                assertThat(EnvUtility.getEnvValue(e.key)).isEqualTo(e.value)
            }
        }
        run {
            // Act, Assert
            assertThat(EnvUtility.getEnvValue("name1")).isEqualTo(null)
        }
        run {
            // Arrange
            EnvUtility.setEnvForTesting("name1", "value1")
            // Act, Assert
            assertThat(EnvUtility.getEnvValue("name1")).isEqualTo("value1")
        }
        run {
            // Arrange
            EnvUtility.setEnvForTesting("name2", "value2")
            // Act, Assert
            assertThat(EnvUtility.getEnvValue("name1")).isEqualTo("value1")
            assertThat(EnvUtility.getEnvValue("name2")).isEqualTo("value2")
        }
    }
}