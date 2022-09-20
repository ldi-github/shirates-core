package experiment

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.output
import shirates.core.testcode.Must
import shirates.core.testcode.Should
import shirates.core.testcode.UITest
import shirates.core.testcode.Want

@Testrun("unitTestData/testConfig/priorityConfig/testrun.skipMustTest.properties")
class PrioritySkipMustTest : UITest() {

    @Must
    @Order(1)
    @Test
    fun must() {
        output("must")
    }

    @Should
    @Order(2)
    @Test
    fun should() {
        output("should")
    }

    @Want
    @Order(3)
    @Test
    fun want() {
        output("want")
    }

    @Order(4)
    @Test
    fun none() {
        output("none")
    }
}