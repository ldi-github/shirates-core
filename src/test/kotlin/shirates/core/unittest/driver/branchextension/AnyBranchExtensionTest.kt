package shirates.core.unittest.driver.branchextension

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.driver.TestElement
import shirates.core.driver.TestMode
import shirates.core.driver.branchextension.*
import shirates.core.driver.testDrive
import shirates.core.testcode.UnitTest

class AnyBranchExtensionTest : UnitTest() {

    @Test
    fun android() {

        TestMode.runAsAndroid {

            var a = false
            var i = false
            val drivable = TestElement()

            drivable
                .android {
                    a = true
                }.ios {
                    i = true
                }
            assertThat(a).isTrue()
            assertThat(i).isFalse()
        }
    }

    @Test
    fun ios() {

        var a = false
        var i = false
        val drivable = TestElement()

        TestMode.runAsIos {
            drivable
                .android {
                    a = true
                }.ios {
                    i = true
                }
            assertThat(a).isFalse()
            assertThat(i).isTrue()
        }
    }

    @Test
    fun emulatorTest() {

        var e = false
        var s = false
        var r = false
        val drivable = TestElement()

        TestMode.runAsEmulator {
            drivable
                .emulator {
                    e = true
                }.simulator {
                    s = true
                }.realDevice {
                    r = true
                }
            assertThat(e).isTrue()
            assertThat(s).isTrue()
            assertThat(r).isFalse()
        }
    }

    @Test
    fun readDeviceTest() {

        var e = false
        var s = false
        var r = false

        TestMode.runAsRealDevice {
            testDrive
                .emulator {
                    e = true
                }.simulator {
                    s = true
                }.realDevice {
                    r = true
                }
            assertThat(e).isFalse()
            assertThat(s).isFalse()
            assertThat(r).isTrue()
        }
    }

    @Test
    fun osaifuKeitaiTest() {

        var osaifu = false
        var osaifuNot = false

        TestMode.runAsOsaifuKeitai {
            testDrive
                .osaifuKeitai {
                    osaifu = true
                }.osaifuKeitaiNot {
                    osaifuNot = true
                }
            assertThat(osaifu).isTrue()
            assertThat(osaifuNot).isFalse()
        }
    }

}