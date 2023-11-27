package shirates.core.uitest.android.driver.branchextension

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.branchextension.*
import shirates.core.driver.commandextension.*
import shirates.core.testcode.NoLoadRun
import shirates.core.testcode.UITest
import shirates.helper.ImageSetupHelper

@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
class TestDriveBranchExtensionTest2 : UITest() {

    @Test
    @Order(0)
    fun setupImage() {

        scenario {
            case(1) {
                action {
                    describe("Setting up image")
                    ImageSetupHelper.setupImageAndroidSettingsTopScreen()
                }.expectation {
                    OK()
                }
            }
        }
    }

    @NoLoadRun
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
                    it.macro("[Android Settings Top Screen]")
                    clear()
                }.action {
                    ifImageExist("[System Icon].png") {
                        describe("ifImageExist('[System Icon].png') called")
                        ifImageExistCalled = true     // never called
                    }
                    ifImageExistNot("[System Icon].png") {
                        describe("ifImageExistNot('[System Icon].png') called")
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
                    ifImageExist("[Connected devices Icon].png") {
                        describe("ifImageExist('[Connected devices Icon].png') called")
                        ifImageExistCalled = true
                    }
                    ifImageExistNot("[Connected devices Icon].png") {
                        describe("ifImageExistNot('[Connected devices Icon].png') called")
                        ifImageExistCalled = true
                    }
                }.expectation {
                    ifImageExistCalled.thisIsTrue("ifImageExistCalled is true")
                    ifImageExistNotCalled.thisIsFalse("ifImageExistNotCalled is false")
                }
            }
        }
    }

    @NoLoadRun
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
                    it.select("[Connected devices Icon]")
                        .ifImageIs("[Apps Icon].png") {
                            describe("ifImageIs('[Apps Icon].png') called")
                            ifImageIsCalled = true     // never called
                        }
                    it.select("[Connected devices Icon]")
                        .ifImageIsNot("[Apps Icon].png") {
                            describe("ifImageIsNot('[Apps Icon].png') called")
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
                    it.select("[Connected devices Icon]")
                        .ifImageIs("[Connected devices Icon].png") {
                            describe("ifImageIs('[Connected devices Icon].png') called")
                            ifImageIsCalled = true  // called
                        }
                    it.select("[Connected devices Icon]")
                        .ifImageIsNot("[Connected devices Icon].png") {
                            describe("ifImageIsNot('[Connected devices Icon].png') called")
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
                    it.select("[Connected devices Icon]")
                        .ifImageIs("[Connected devices Icon].png") {
                            describe("ifImageIs('[Connected devices Icon].png') called")
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
                    it.select("[Connected devices Icon]")
                        .ifImageIs("[App Icon].png") {
                            describe("ifImageIs('[App Icon].png') called")
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
                    it.select("[Connected devices Icon]")
                        .ifImageIs("[App Icon].png") {
                            describe("ifImageIs('[App Icon].png') called")
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

    @NoLoadRun
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
                    val imageMatchResult = it.findImage("[Apps Icon].png")
                    imageMatchResult.ifTrue {
                        OK("ifTrue called")
                    }
                    imageMatchResult.ifFalse {
                        NG()
                    }
                }
            }
            case(2) {
                expectation {
                    val imageMatchResult = it.findImage("[System Icon].png")
                    imageMatchResult.ifTrue {
                        NG()
                    }
                    imageMatchResult.ifFalse {
                        OK("ifTrue called")
                    }
                }
            }
        }
    }

}