package shirates.core.vision.uitest.android

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.driver.commandextension.exist
import shirates.core.driver.commandextension.existWithScrollDown
import shirates.core.vision.classicScope
import shirates.core.vision.driver.commandextension.exist
import shirates.core.vision.driver.commandextension.screenIs
import shirates.core.vision.testcode.VisionTest
import shirates.core.vision.visionScope

class VisionScopeExtensionTest : VisionTest() {

    @Test
    fun nestingScopes() {

        scenario {
            case(1) {
                condition {
                    it.screenIs("[Android Settings Top Screen]")
                }.expectation {
                    classicScope {
                        assertThat(it.javaClass.toString()).isEqualTo("class shirates.core.driver.TestDriveObject")

                        val e1 = it.existWithScrollDown("Tips & support")    // exact match
                        assertThat(e1.javaClass.toString()).isEqualTo("class shirates.core.driver.TestElement")

                        visionScope {
                            assertThat(it.javaClass.toString()).isEqualTo("class shirates.core.vision.driver.VisionDriveObject")
                            v1 = it.exist("Tips & support")  // best effort match
                            assertThat(v1.javaClass.toString()).isEqualTo("class shirates.core.vision.VisionElement")

                            classicScope {
                                assertThat(it.javaClass.toString()).isEqualTo("class shirates.core.driver.TestDriveObject")

                                val e2 = it.exist("Tips & support")  // exact match
                                assertThat(e2.javaClass.toString()).isEqualTo("class shirates.core.driver.TestElement")
                            }
                        }
                    }
                }
            }
        }

    }
}