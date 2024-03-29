package shirates.core.unittest.driver

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.configuration.PropertiesManager
import shirates.core.driver.TestMode
import shirates.core.testcode.UnitTest
import shirates.core.utility.misc.EnvUtility

class TestModeTest : UnitTest() {

    @Test
    fun init() {

        // Act, Assert
        assertThat(TestMode.testTimeNoLoadRun).isNull()
        assertThat(TestMode.isAndroid).isTrue()
    }

    @Test
    fun clear() {

        // Arrange
        TestMode.testTimeNoLoadRun = true
        TestMode.testTimePlatformName = "IOS"
        TestMode.testTimeIsVirtualDevice = true
        TestMode.testTimeHasOsaifuKeitai = true
        TestMode.testTimeIsStub = true
        assertThat(TestMode.testTimeNoLoadRun).isTrue()
        assertThat(TestMode.testTimePlatformName).isEqualTo("IOS")
        assertThat(TestMode.testTimeIsVirtualDevice).isTrue()
        assertThat(TestMode.testTimeHasOsaifuKeitai).isTrue()
        assertThat(TestMode.testTimeIsStub).isTrue()

        // Act
        TestMode.clear()
        // Assert
        assertThat(TestMode.testTimeNoLoadRun).isNull()
        assertThat(TestMode.testTimePlatformName).isNull()
        assertThat(TestMode.testTimeIsVirtualDevice).isNull()
        assertThat(TestMode.testTimeHasOsaifuKeitai).isNull()
        assertThat(TestMode.testTimeIsStub).isNull()
    }

    @Test
    fun isAndroid() {

        run {
            // Arrange
            TestMode.testTimePlatformName = "Android"
            // Act, Assert
            assertThat(TestMode.isAndroid).isTrue()

            // Arrange
            TestMode.testTimePlatformName = "android"
            assertThat(TestMode.isAndroid).isTrue()

            // Arrange
            TestMode.testTimePlatformName = "ios"
            assertThat(TestMode.isAndroid).isFalse()

            // Arrange
            TestMode.testTimePlatformName = null
            assertThat(TestMode.isAndroid).isTrue()
        }
        run {
            // Arrange
            TestMode.testTimePlatformName = null
            EnvUtility.setEnvForTesting("SR_os", "")
            PropertiesManager.setup()
            // Act, Assert
            assertThat(TestMode.isAndroid).isTrue()

            // Arrange
            TestMode.testTimePlatformName = null
            EnvUtility.setEnvForTesting("SR_os", "iOS")
            PropertiesManager.setup()
            // Act, Assert
            println("isNoLoadRun=${TestMode.isNoLoadRun}")
            println("testTimePlatformName=${TestMode.testTimePlatformName}")
            println("PropertiesManager.os=${PropertiesManager.os}")
            assertThat(TestMode.isAndroid).isFalse()

            // Arrange
            TestMode.testTimePlatformName = null
            EnvUtility.setEnvForTesting("SR_os", "ANDROID")
            PropertiesManager.setup()
            // Act, Assert
            assertThat(TestMode.isAndroid).isTrue()

            // Arrange
            TestMode.testTimePlatformName = "ios"
            EnvUtility.setEnvForTesting("SR_os", "ANDROID")
            PropertiesManager.setup()
            // Act, Assert
            assertThat(TestMode.isAndroid).isFalse()

            // Arrange
            TestMode.testTimePlatformName = null
            EnvUtility.setEnvForTesting("SR_os", "")
            // Act, Assert
            assertThat(TestMode.isAndroid).isTrue()

            // Arrange
            TestMode.testTimeNoLoadRun = true
            TestMode.testTimePlatformName = "ios"
            // Act, Assert
            assertThat(TestMode.isAndroid).isTrue()
        }
    }

    @Test
    fun isiOS() {

        run {
            // Arrange
            TestMode.testTimePlatformName = "iOS"
            // Act, Assert
            assertThat(TestMode.isiOS).isTrue()

            // Arrange
            TestMode.testTimePlatformName = "ios"
            assertThat(TestMode.isiOS).isTrue()

            // Arrange
            TestMode.testTimePlatformName = "android"
            assertThat(TestMode.isiOS).isFalse()

            // Arrange
            TestMode.testTimePlatformName = null
            // Act, Assert
            assertThat(TestMode.isiOS).isFalse()
        }
        run {
            // Arrange
            TestMode.testTimePlatformName = null
            EnvUtility.setEnvForTesting("SR_os", "")
            PropertiesManager.setup()
            // Act, Assert
            assertThat(TestMode.isiOS).isFalse()

            // Arrange
            TestMode.testTimePlatformName = null
            EnvUtility.setEnvForTesting("SR_os", "Android")
            PropertiesManager.setup()
            // Act, Assert
            assertThat(TestMode.isiOS).isFalse()

            // Arrange
            TestMode.testTimePlatformName = null
            EnvUtility.setEnvForTesting("SR_os", "IOS")
            PropertiesManager.setup()
            // Act, Assert
            assertThat(TestMode.isiOS).isTrue()

            // Arrange
            TestMode.testTimePlatformName = "ios"
            EnvUtility.setEnvForTesting("SR_os", "IOS")
            PropertiesManager.setup()
            // Act, Assert
            assertThat(TestMode.isiOS).isTrue()

            // Arrange
            TestMode.testTimePlatformName = null
            EnvUtility.setEnvForTesting("SR_os", "")
            PropertiesManager.setup()
            // Act, Assert
            assertThat(TestMode.isiOS).isFalse()

            // Arrange
            TestMode.testTimeNoLoadRun = true
            TestMode.testTimePlatformName = "android"
            PropertiesManager.setup()
            // Act, Assert
            assertThat(TestMode.isiOS).isTrue()
        }
    }

    @Test
    fun isEmulator_isSimulator_isVirtualDevide_isRealDevice() {

        TestMode.runAsNoLoadRunMode {
            // Act, Assert
            assertThat(TestMode.isEmulator).isTrue()
            assertThat(TestMode.isSimulator).isTrue()
        }

        TestMode.runAsAndroid {
            // Arrange
            TestMode.testTimeIsVirtualDevice = true
            // Act, Assert
            assertThat(TestMode.isEmulator).isTrue()
            assertThat(TestMode.isSimulator).isFalse()
            assertThat(TestMode.isVirtualDevice).isTrue()
            assertThat(TestMode.isRealDevice).isFalse()

            // Arrange
            TestMode.testTimeIsVirtualDevice = false
            // Act, Assert
            assertThat(TestMode.isEmulator).isFalse()
            assertThat(TestMode.isSimulator).isFalse()
            assertThat(TestMode.isVirtualDevice).isFalse()
            assertThat(TestMode.isRealDevice).isTrue()

            // Arrange
            TestMode.testTimeIsVirtualDevice = null
            assertThat(TestMode.isEmulator).isFalse()
            assertThat(TestMode.isSimulator).isFalse()
            assertThat(TestMode.isVirtualDevice).isFalse()
            assertThat(TestMode.isRealDevice).isTrue()
        }
        TestMode.runAsIos {
            // Arrange
            TestMode.testTimeIsVirtualDevice = true
            // Act, Assert
            assertThat(TestMode.isEmulator).isFalse()
            assertThat(TestMode.isSimulator).isTrue()
            assertThat(TestMode.isVirtualDevice).isTrue()
            assertThat(TestMode.isRealDevice).isFalse()

            // Arrange
            TestMode.testTimeIsVirtualDevice = false
            // Act, Assert
            assertThat(TestMode.isEmulator).isFalse()
            assertThat(TestMode.isSimulator).isFalse()
            assertThat(TestMode.isVirtualDevice).isFalse()
            assertThat(TestMode.isRealDevice).isTrue()

            // Arrange
            TestMode.testTimeIsVirtualDevice = null
            // Act, Assert
            assertThat(TestMode.isEmulator).isFalse()
            assertThat(TestMode.isSimulator).isFalse()
            assertThat(TestMode.isVirtualDevice).isFalse()
            assertThat(TestMode.isRealDevice).isTrue()
        }

    }

    @Test
    fun runAs() {

        TestMode.runAsVirtualDevice {
            TestMode.runAsAndroid {
                assertThat(TestMode.isVirtualDevice).isTrue()
                assertThat(TestMode.isRealDevice).isFalse()

                assertThat(TestMode.isAndroid).isTrue()
                assertThat(TestMode.isEmulator).isTrue()

                assertThat(TestMode.isiOS).isFalse()
                assertThat(TestMode.isSimulator).isFalse()
            }
            TestMode.runAsIos {
                assertThat(TestMode.isVirtualDevice).isTrue()
                assertThat(TestMode.isRealDevice).isFalse()

                assertThat(TestMode.isAndroid).isFalse()
                assertThat(TestMode.isEmulator).isFalse()

                assertThat(TestMode.isiOS).isTrue()
                assertThat(TestMode.isSimulator).isTrue()
            }
        }
        TestMode.runAsRealDevice {
            TestMode.runAsAndroid {
                assertThat(TestMode.isVirtualDevice).isFalse()
                assertThat(TestMode.isRealDevice).isTrue()

                assertThat(TestMode.isAndroid).isTrue()
                assertThat(TestMode.isEmulator).isFalse()

                assertThat(TestMode.isiOS).isFalse()
                assertThat(TestMode.isSimulator).isFalse()
            }
            TestMode.runAsIos {
                assertThat(TestMode.isVirtualDevice).isFalse()
                assertThat(TestMode.isRealDevice).isTrue()

                assertThat(TestMode.isAndroid).isFalse()
                assertThat(TestMode.isEmulator).isFalse()

                assertThat(TestMode.isiOS).isTrue()
                assertThat(TestMode.isSimulator).isFalse()
            }
        }
    }

    @Test
    fun hasOsaifuKeitai() {

        TestMode.runAsNoLoadRunMode {
            // Act, Assert
            assertThat(TestMode.hasOsaifuKeitai).isTrue()
        }

        run {
            // Arrange
            TestMode.testTimeHasOsaifuKeitai = true
            // Act, Assert
            assertThat(TestMode.hasOsaifuKeitai).isTrue()

            // Arrange
            TestMode.testTimeHasOsaifuKeitai = false
            // Act, Assert
            assertThat(TestMode.hasOsaifuKeitai).isFalse()
        }


        TestMode.runAsAndroid {
            // Arrange
            TestMode.testTimeHasOsaifuKeitai = null
            // Act, Assert
            assertThat(TestMode.hasOsaifuKeitai).isFalse()
        }
        TestMode.runAsIos {
            // Arrange
            TestMode.testTimeHasOsaifuKeitai = null
            // Act, Assert
            assertThat(TestMode.hasOsaifuKeitai).isFalse()
        }

        TestMode.runAsOsaifuKeitai {
            // Act, Assert
            assertThat(TestMode.hasOsaifuKeitai).isTrue()
        }
        TestMode.runAsNoOsaifuKeitai {
            // Act, Assert
            assertThat(TestMode.hasOsaifuKeitai).isFalse()
        }
    }

    @Test
    fun isStub() {

        TestMode.runAsNoLoadRunMode {
            // Act, Assert
            assertThat(TestMode.isStub).isTrue()
        }

        run {
            // Arrange
            TestMode.testTimeIsStub = true
            // Act, Assert
            assertThat(TestMode.isStub).isTrue()

            // Arrange
            TestMode.testTimeIsStub = false
            // Act, Assert
            assertThat(TestMode.isStub).isFalse()

            // Arrange
            TestMode.testTimeIsStub = null
            // Act, Assert
            assertThat(TestMode.isStub).isFalse()
        }

        TestMode.runAsStub {

            // Act, Assert
            assertThat(TestMode.isStub).isTrue()
        }
    }
}