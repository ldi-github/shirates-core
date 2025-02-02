package shirates.core.unittest.utility.misc

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.testcode.UnitTest
import shirates.core.utility.label
import shirates.core.utility.secondLabel
import shirates.core.utility.toDate
import java.time.Duration

class DurationExtensionTest : UnitTest() {

    @Test
    fun label() {

        run {
            // Arrange
            val t1 = "2022/01/01 00:00:00.000".toDate().time
            val t2 = "2022/01/01 12:34:56.789".toDate().time
            val duration = Duration.ofMillis(t2 - t1)
            // Act, Assert
            assertThat(duration.label).isEqualTo("12:34:56.789")
        }

        run {
            // Arrange
            val t1 = "2022/01/01 00:00:00.000".toDate().time
            val t2 = "2022/01/02 12:34:56.789".toDate().time
            val duration = Duration.ofMillis(t2 - t1)
            // Act, Assert
            assertThat(duration.label).isEqualTo("36:34:56.789")
        }
    }

    @Test
    fun secondLabel() {

        run {
            // Arrange
            val duration = Duration.ofMillis(59876)
            // Act, Assert
            assertThat(duration.secondLabel).isEqualTo("59.876 sec")
        }

        run {
            // Arrange
            val duration = Duration.ofMillis(123)
            // Act, Assert
            assertThat(duration.secondLabel).isEqualTo("0.123 sec")
        }
    }
}