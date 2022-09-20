package shirates.core.unittest.storage

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.storage.Memo
import shirates.core.testcode.UnitTest

class MemoTest : UnitTest() {

    @Test
    fun clear() {

        // Act
        Memo.clear()
        // Assert
        assertThat(Memo.map.count()).isEqualTo(0)
        assertThat(Memo.containsKey("key1")).isFalse()
        assertThat(Memo.getFirstValue("key1")).isEqualTo("")
        assertThat(Memo.getLastValue("key1")).isEqualTo("")
        assertThat(Memo.getValueAt("key1", 1)).isEqualTo("[value not found at 1]")
        assertThat(Memo.getValueHistory("key1").count()).isEqualTo(0)
    }

    @Test
    fun write() {

        // Arrange
        Memo.clear()
        assertThat(Memo.containsKey("key1")).isFalse()
        // Act
        Memo.write(key = "key1", text = "value1")
        // Assert
        assertThat(Memo.map.count()).isEqualTo(1)
        assertThat(Memo.containsKey("key1")).isTrue()
        assertThat(Memo.getLastValue("key1")).isEqualTo("value1")
        assertThat(Memo.getFirstValue("key1")).isEqualTo("value1")
        assertThat(Memo.getValueAt("key1", 1)).isEqualTo("value1")
        assertThat(Memo.getValueAt("key1", 2)).isEqualTo("[value not found at 2]")
        assertThat(Memo.getValueHistory("key1").count()).isEqualTo(1)

        // Act
        Memo.write(key = "key1", text = "value1-2")
        // Act, Assert
        assertThat(Memo.map.count()).isEqualTo(1)
        assertThat(Memo.containsKey("key1")).isTrue()
        assertThat(Memo.getLastValue("key1")).isEqualTo("value1-2")
        assertThat(Memo.getFirstValue("key1")).isEqualTo("value1")
        assertThat(Memo.getValueAt("key1", 1)).isEqualTo("value1")
        assertThat(Memo.getValueAt("key1", 2)).isEqualTo("value1-2")
        assertThat(Memo.getValueHistory("key1").count()).isEqualTo(2)
    }

    @Test
    fun read() {

        // Arrange
        Memo.clear()
        // Act, Assert
        assertThat(Memo.read("")).isEqualTo("")
        assertThat(Memo.read("key1")).isEqualTo("")

        // Arrange
        Memo.write("key1", "value1")
        // Act, Assert
        assertThat(Memo.read("key1")).isEqualTo("value1")
        assertThat(Memo.read("key2")).isEqualTo("")

        // Arrange
        Memo.write("key2", "value2")
        // Act, Assert
        assertThat(Memo.read("key1")).isEqualTo("value1")
        assertThat(Memo.read("key2")).isEqualTo("value2")


        // Arrange
        Memo.write("key1", "value1(new)")
        // Act, Assert
        assertThat(Memo.read("key1")).isEqualTo("value1(new)")
        assertThat(Memo.read("key2")).isEqualTo("value2")
    }
}