package shirates.core.unittest.utility.file

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import shirates.core.testcode.UnitTest
import shirates.core.utility.file.PropertiesUtility
import shirates.core.utility.toPath
import java.io.FileNotFoundException

class PropertiesUtilityTest : UnitTest() {

    @Test
    fun getProperties1() {

        run {
            // Act
            val props = PropertiesUtility.getProperties("unitTestData/resources/resource1.properties".toPath())
            // Assert
            assertThat(props.count()).isGreaterThan(0)
        }
        run {
            assertThatThrownBy {
                PropertiesUtility.getProperties("noexist".toPath())
            }.isInstanceOf(FileNotFoundException::class.java)
        }
    }

}