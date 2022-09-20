package shirates.core.unittest.driver.commandextension

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.driver.commandextension.memoTextAs
import shirates.core.storage.Memo
import shirates.core.testcode.UnitTest

class StringStorageExtensionTest : UnitTest() {

    @Test
    fun memoTextAs() {

        // Arrange
        Memo.clear()

        // Act
        "333m".memoTextAs("height of Tokyo Tower")
        // Assert
        assertThat(Memo.containsKey("height of Tokyo Tower")).isTrue()
        assertThat(Memo.read("height of Tokyo Tower")).isEqualTo("333m")


        // Act
        "3776m".memoTextAs("height of Mt.Fuji")
        // Assert
        assertThat(Memo.containsKey("height of Tokyo Tower")).isTrue()
        assertThat(Memo.read("height of Tokyo Tower")).isEqualTo("333m")
        assertThat(Memo.containsKey("height of Mt.Fuji")).isTrue()
        assertThat(Memo.read("height of Mt.Fuji")).isEqualTo("3776m")
    }

}