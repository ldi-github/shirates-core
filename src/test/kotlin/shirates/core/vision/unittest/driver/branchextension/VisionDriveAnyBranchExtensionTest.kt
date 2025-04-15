package shirates.core.vision.unittest.driver.branchextension

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.driver.TestMode
import shirates.core.driver.vision
import shirates.core.testcode.UnitTest
import shirates.core.vision.VisionElement
import shirates.core.vision.driver.branchextension.*


class VisionDriveAnyBranchExtensionTest : UnitTest() {

    @Test
    fun android() {

        TestMode.runAsAndroid {

            var a = false
            var i = false
            val drivable = VisionElement()

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
        val drivable = VisionElement()

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
    fun virtualDeviceTest() {

        var e = false
        var s = false
        var v = false
        var r = false

        fun reset() {
            e = false
            s = false
            v = false
            r = false
        }

        TestMode.runAsVirtualDevice {
            TestMode.runAsAndroid {
                reset()
                vision
                    .emulator {
                        e = true
                    }.simulator {
                        s = true
                    }.virtualDevice {
                        v = true
                    }.realDevice {
                        r = true
                    }
                assertThat(e).isTrue()
                assertThat(s).isFalse()
                assertThat(v).isTrue()
                assertThat(r).isFalse()
            }
            TestMode.runAsIos {
                reset()
                vision
                    .emulator {
                        e = true
                    }.simulator {
                        s = true
                    }.virtualDevice {
                        v = true
                    }.realDevice {
                        r = true
                    }
                assertThat(e).isFalse()
                assertThat(s).isTrue()
                assertThat(v).isTrue()
                assertThat(r).isFalse()
            }
        }

    }

    @Test
    fun realDeviceTest() {

        var e = false
        var s = false
        var v = false
        var r = false

        fun reset() {
            e = false
            s = false
            v = false
            r = false
        }

        TestMode.runAsRealDevice {
            TestMode.runAsAndroid {
                reset()
                vision
                    .emulator {
                        e = true
                    }.simulator {
                        s = true
                    }.virtualDevice {
                        v = true
                    }.realDevice {
                        r = true
                    }
                assertThat(e).isFalse()
                assertThat(s).isFalse()
                assertThat(v).isFalse()
                assertThat(r).isTrue()
            }
            TestMode.runAsIos {
                reset()
                vision
                    .emulator {
                        e = true
                    }.simulator {
                        s = true
                    }.virtualDevice {
                        v = true
                    }.realDevice {
                        r = true
                    }
                assertThat(e).isFalse()
                assertThat(s).isFalse()
                assertThat(v).isFalse()
                assertThat(r).isTrue()
            }
        }
    }

    @Test
    fun osaifuKeitaiTest() {

        var osaifu = false
        var osaifuNot = false

        TestMode.runAsOsaifuKeitai {
            vision
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