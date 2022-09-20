package experiment

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.testcode.Must
import shirates.core.testcode.Should
import shirates.core.testcode.UITest
import shirates.core.testcode.Want

@Testrun("unitTestData/testConfig/priorityConfig/testrun.skipWantTest.properties")
class PrioritySkipWantTest : UITest() {

    @Must
    @Order(1)
    @Test
    fun must() {

    }

    @Should
    @Order(2)
    @Test
    fun should() {

    }

    @Want
    @Order(3)
    @Test
    fun want() {

    }

    @Order(4)
    @Test
    fun none() {

    }
}