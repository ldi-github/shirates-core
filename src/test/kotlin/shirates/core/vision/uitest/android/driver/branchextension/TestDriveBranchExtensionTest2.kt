package shirates.core.vision.uitest.android.driver.branchextension

import ifImageExist
import ifImageExistNot
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.branchextension.ifTrue
import shirates.core.driver.commandextension.thisIsFalse
import shirates.core.driver.commandextension.thisIsTrue
import shirates.core.testcode.Manual
import shirates.core.vision.driver.branchextension.ifImageIs
import shirates.core.vision.driver.branchextension.ifImageIsNot
import shirates.core.vision.driver.commandextension.*
import shirates.core.vision.testcode.VisionTest

@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
class TestDriveBranchExtensionTest2 : VisionTest() {

    @Manual
    @Test
    @Order(10)
    fun s10() {

        scenarioS10()
    }

    @Test
    @Order(11)
    fun s11() {

        scenarioS10()
    }

    private fun scenarioS10() {
        var ifImageExistCalled = false
        var ifImageExistNotCalled = false
        fun clear() {
            ifImageExistCalled = false
            ifImageExistNotCalled = false
        }

        scenario {
            case(1) {
                condition {
                    it.terminateApp()
                    it.macro("[Android Settings Top Screen]")
                    clear()
                }.action {
                    ifImageExist("[System Icon]") {
                        describe("ifImageExist('[System Icon]') called")
                        ifImageExistCalled = true     // never called
                    }
                    ifImageExistNot("[System Icon]") {
                        describe("ifImageExistNot('[System Icon]') called")
                        ifImageExistNotCalled = true     // called
                    }
                }.expectation {
                    ifImageExistCalled.thisIsFalse("ifImageExistCalled is false")
                    ifImageExistNotCalled.thisIsTrue("ifImageExistNotCalled is true")
                }
            }
            case(2) {
                condition {
                    clear()
                }.action {
                    ifImageExist("[Connected devices Icon]") {
                        describe("ifImageExist('[Connected devices Icon]') called")
                        ifImageExistCalled = true
                    }
                    ifImageExistNot("[Connected devices Icon]") {
                        describe("ifImageExistNot('[Connected devices Icon]') called")
                        ifImageExistCalled = true
                    }
                }.expectation {
                    ifImageExistCalled.thisIsTrue("ifImageExistCalled is true")
                    ifImageExistNotCalled.thisIsFalse("ifImageExistNotCalled is false")
                }
            }
        }
    }

    @Manual
    @Test
    @Order(20)
    fun s20() {

        scenarioS20()
    }

    @Test
    @Order(20)
    fun s21() {

        scenarioS20()
    }

    private fun scenarioS20() {
        var ifImageIsCalled = false
        var ifImageIsNotCalled = false
        var ifElseCalled = false
        var notCalled = false
        fun clear() {
            ifImageIsCalled = false
            ifImageIsNotCalled = false
            ifElseCalled = false
            notCalled = false
        }

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                    clear()
                }.action {
                    it.detect("Connected devices")
                        .leftItem()
                        .ifImageIs("[Apps Icon]") {
                            describe("ifImageIs('[Apps Icon]') called")
                            ifImageIsCalled = true     // never called
                        }
                    it.detect("Connected devices")
                        .leftItem()
                        .ifImageIsNot("[Apps Icon]") {
                            describe("ifImageIsNot('[Apps Icon]') called")
                            ifImageIsNotCalled = true     // called
                        }
                }.expectation {
                    ifImageIsCalled.thisIsFalse("ifImageIsCalled is false")
                    ifImageIsNotCalled.thisIsTrue("ifImageIsNotCalled is true")
                }
            }
            case(2) {
                condition {
                    clear()
                }.action {
                    it.detect("Connected devices")
                        .leftItem()
                        .ifImageIs("[Connected devices Icon]") {
                            describe("ifImageIs('[Connected devices Icon]') called")
                            ifImageIsCalled = true  // called
                        }
                    it.detect("Connected devices")
                        .leftItem()
                        .ifImageIsNot("[Connected devices Icon]") {
                            describe("ifImageIsNot('[Connected devices Icon]') called")
                            ifImageIsNotCalled = true   //never called
                        }
                }.expectation {
                    ifImageIsCalled.thisIsTrue("ifImageIsCalled is true")
                    ifImageIsNotCalled.thisIsFalse("ifImageIsNotCalled is false")
                }
            }
            case(3) {
                condition {
                    clear()
                }.action {
                    it.detect("Connected devices")
                        .leftItem()
                        .ifImageIs("[Connected devices Icon]") {
                            describe("ifImageIs('[Connected devices Icon]') called")
                            ifImageIsCalled = true  // called
                        }.ifElse {
                            describe("ifElse called")
                            ifElseCalled = true   // never called
                        }
                }.expectation {
                    ifImageIsCalled.thisIsTrue("ifImageIsCalled is true")
                    ifElseCalled.thisIsFalse("ifElseCalled is false")
                }
            }
            case(4) {
                condition {
                    clear()
                }.action {
                    it.detect("Connected devices")
                        .leftItem()
                        .ifImageIs("[App Icon]") {
                            describe("ifImageIs('[App Icon]') called")
                            ifImageIsCalled = true  // never called
                        }.ifElse {
                            describe("ifElse called")
                            ifElseCalled = true   // called
                        }
                }.expectation {
                    ifImageIsCalled.thisIsFalse("ifImageIsCalled is false")
                    ifElseCalled.thisIsTrue("ifElseCalled is true")
                }
            }
            case(5) {
                condition {
                    clear()
                }.action {
                    it.detect("Connected devices")
                        .leftItem()
                        .ifImageIs("[App Icon]") {
                            describe("ifImageIs('[App Icon]') called")
                            ifImageIsCalled = true  // never called
                        }.ifElse {
                            describe("not called")
                            notCalled = true   // called
                        }
                }.expectation {
                    ifImageIsCalled.thisIsFalse("ifImageIsCalled is false")
                    notCalled.thisIsTrue("notCalled is true")
                }
            }
        }
    }

    @Manual
    @Test
    @Order(30)
    fun s30() {

        scenarioS30()
    }

    @Test
    @Order(31)
    fun s31() {

        scenarioS30()
    }

    private fun scenarioS30() {
        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.findImage("[Apps Icon]").isFound
                        .ifTrue {
                            OK("ifTrue called")
                        }.ifFalse {
                            NG()
                        }
                }
            }
            case(2) {
                expectation {
                    it.findImage("[System Icon]", throwsException = false).isFound
                        .ifTrue {
                            NG()
                        }.ifFalse {
                            OK("ifTrue called")
                        }
                }
            }
        }
    }

}