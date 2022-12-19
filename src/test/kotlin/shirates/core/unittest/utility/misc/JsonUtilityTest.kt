package shirates.core.unittest.utility.misc

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.testcode.UnitTest
import shirates.core.utility.misc.JsonUtility

class JsonUtilityTest : UnitTest() {

    @Test
    fun getJSONObjectFromFile() {

        // Act
        val jso = JsonUtility.getJSONObjectFromFile("unitTestConfig/android/androidSettings/dataset/data.json")

        run {
            val dataSet = jso.getJSONObject("[dataset1]")
            assertThat(dataSet.getString("key1-1")).isEqualTo("value1-1")
            assertThat(dataSet.getString("key1-2")).isEqualTo("value1-2")
        }

        run {
            val dataSet = jso.getJSONObject("[dataset2]")
            assertThat(dataSet.getString("key2-1")).isEqualTo("value2-1")
            assertThat(dataSet.getString("key2-2")).isEqualTo("value2-2")
        }
    }

}