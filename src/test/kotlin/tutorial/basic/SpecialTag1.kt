package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.branchextension.specialTag
import shirates.core.driver.commandextension.output
import shirates.core.driver.testProfile
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class SpecialTag1 : UITest() {

    @Test
    @Order(10)
    fun specialTag1() {

        scenario {
            case(1) {
                expectation {
                    output("testProfile.specialTags=${testProfile.specialTags}")

                    specialTag("Tag1") {
                        OK("specialTag(\"Tag1\") called")
                    }
                    specialTag("Tag2") {
                        OK("specialTag(\"Tag2\") called")
                    }
                    specialTag("Tag3") {
                        OK("specialTag(\"Tag3\") called")
                    }
                }
            }
        }
    }

    @Test
    @Order(20)
    fun specialTag2() {

        scenario {
            case(1) {
                expectation {
                    output("testProfile.specialTags=${testProfile.specialTags}")

                    specialTag("Tag1") {
                        OK("specialTag(\"Tag1\") called")
                    }.notMatched {
                        output("Tag1 not called")
                    }
                    specialTag("Tag2") {
                        OK("specialTag(\"Tag2\") called")
                    }.notMatched {
                        output("Tag2 not called")
                    }
                }
            }
        }
    }

}