package shirates.core.unittest.configuration.repository

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.configuration.repository.DatasetRepositoryManager
import shirates.core.storage.App
import shirates.core.testcode.UnitTest

class AppObjectTest : UnitTest() {

    @Test
    fun getAppNameOfPackageName() {

        // Arrange
        DatasetRepositoryManager.clear()
        DatasetRepositoryManager.loadFromFile("unitTestData/testConfig/androidSettings/dataset/apps.json")
        // Act, Assert
        assertThat(App.getAppNameOfPackageName("com.android.settings")).isEqualTo("[Settings]")
        assertThat(App.getAppNameOfPackageName("com.google.android.calculator")).isEqualTo("[Calculator]")
        assertThat(App.getAppNameOfPackageName("no.exist.package")).isEqualTo("")
    }

}