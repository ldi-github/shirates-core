package shirates.core.uitest.android.driver.branchextension

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.branchextension.*
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest
import shirates.helper.ImageSetupHelper

@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
class TestDriveBranchExtensionTest2 : UITest() {

    @Test
    @Order(0)
    fun setupImage() {

        scenario {
            ImageSetupHelper.setupImageAndroidSettingsTopScreen()
        }
    }

    @Test
    @Order(10)
    fun ifImageExist_ifImageExistNot() {

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
                    ifImageExistCalled.thisIsFalse("ifImageExistCalled=$ifImageExistCalled")
                    ifImageExistNotCalled.thisIsTrue("ifImageExistNotCalled=$ifImageExistNotCalled")
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
                    ifImageExistCalled.thisIsTrue("ifImageExistCalled=$ifImageExistCalled")
                    ifImageExistNotCalled.thisIsFalse("ifImageExistNotCalled=$ifImageExistNotCalled")
                }
            }
        }
    }

    @Test
    @Order(20)
    fun ifImageIs_ifImageIsNot() {

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
                            describe("ifImageIsNot('[Connected devices Icon].png') called")
                            ifImageIsNotCalled = true     // called
                        }
                }.expectation {
                    ifImageIsCalled.thisIsFalse("ifImageIsCalled=$ifImageIsCalled")
                    ifImageIsNotCalled.thisIsTrue("ifImageIsNotCalled=$ifImageIsNotCalled")
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
                    ifImageIsCalled.thisIsTrue("ifImageIsCalled=$ifImageIsCalled")
                    ifImageIsNotCalled.thisIsFalse("ifImageIsNotCalled=$ifImageIsNotCalled")
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
                    ifImageIsCalled.thisIsTrue("ifImageIsCalled=$ifImageIsCalled")
                    ifElseCalled.thisIsFalse("ifElseCalled=$ifElseCalled")
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
                    ifImageIsCalled.thisIsFalse("ifImageIsCalled=$ifImageIsCalled")
                    ifElseCalled.thisIsTrue("ifElseCalled=$ifElseCalled")
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
                    ifImageIsCalled.thisIsFalse("ifImageIsCalled=$ifImageIsCalled")
                    notCalled.thisIsTrue("notCalled=$notCalled")
                }
            }
        }
    }

    @Test
    @Order(30)
    fun imageMatchResult_ifTrue_ifFalse() {

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