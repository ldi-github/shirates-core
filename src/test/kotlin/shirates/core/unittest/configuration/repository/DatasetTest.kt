package shirates.core.unittest.configuration

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.configuration.repository.Dataset
import shirates.core.testcode.UnitTest

class DataSetTest : UnitTest() {

    @Test
    fun getValue() {

        // Arrange
        val dataset = Dataset(datasetName = "dataset1")
        // Act, Assert
        assertThat(dataset.datasetName).isEqualTo("dataset1")
        assertThat(dataset.nameValues.count()).isEqualTo(0)
        assertThat(dataset.hasAny).isFalse()


        // Arrange
        dataset.nameValues["name1"] = "value1"
        // Act, Assert
        assertThat(dataset.nameValues.count()).isEqualTo(1)
        assertThat(dataset.hasAny).isTrue()
    }

}