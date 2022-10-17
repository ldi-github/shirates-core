package shirates.core.uitest.android.driver.branchextension

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.TestMode
import shirates.core.driver.TestMode.hasOsaifuKeitai
import shirates.core.driver.branchextension.*
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest
import shirates.core.utility.host.HostOSUtility

@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
class TestDriveBranchExtensionTest : UITest() {

    @Test
    @Order(10)
    fun android_ios() {

        var androidCalled = false
        var iosCalled = false

        scenario {
            case(1) {
                condition {
                    androidCalled = false
                    iosCalled = false
                }.action {
                    android {
                        describe("android called")
                        androidCalled = true
                    }.ios {
                        describe("ios called")
                        iosCalled = true
                    }
                }.expectation {
                    androidCalled.thisIsTrue("androidCalled=$androidCalled")
                    iosCalled.thisIsFalse("iosCalled=$iosCalled")
                }
            }
        }
    }

    @Test
    @Order(20)
    fun emulator_realDevice() {

        var emulatorCalled = false
        var realDeviceCalled = false
        fun clear() {
            emulatorCalled = false
            realDeviceCalled = false
        }

        scenario {
            emulator {
                case(1) {
                    condition {
                        clear()
                    }.action {
                        emulator {
                            describe("emulator called")
                            emulatorCalled = true
                        }.realDevice {
                            describe("realDevice called")
                            realDeviceCalled = true
                        }
                    }.expectation {
                        emulatorCalled.thisIsTrue("emulatorCalled=$emulatorCalled")
                        realDeviceCalled.thisIsFalse("realDeviceCalled=$realDeviceCalled")
                    }
                }
            }
            realDevice {
                case(1) {
                    condition {
                        clear()
                    }.action {
                        emulator {
                            describe("emulator called")
                            emulatorCalled = true
                        }.realDevice {
                            describe("realDevice called")
                            realDeviceCalled = true
                        }
                    }.expectation {
                        emulatorCalled.thisIsFalse("emulatorCalled=$emulatorCalled")
                        realDeviceCalled.thisIsTrue("realDeviceCalled=$realDeviceCalled")
                    }
                }
            }
        }
    }

    @Test
    @Order(30)
    fun arm64_intel() {

        var arm64Called = false
        var intelCalled = false
        fun clear() {
            arm64Called = false
            intelCalled = false
        }

        HostOSUtility.isArm64
        HostOSUtility.isMacOS

        scenario {
            TestMode.runAsArm64 {
                case(1) {
                    condition {
                        clear()
                    }.action {
                        arm64 {
                            describe("arm64 called")
                            arm64Called = true
                        }
                        intel {
                            describe("intel called")
                            intelCalled = true
                        }
                    }.expectation {
                        arm64Called.thisIsTrue("arm64Called=$arm64Called")
                        intelCalled.thisIsFalse("intelCalled=$intelCalled")
                    }
                }
            }
            TestMode.runAsIntel {
                case(2) {
                    condition {
                        clear()
                    }.action {
                        arm64 {
                            describe("arm64 called")
                            arm64Called = true
                        }
                        intel {
                            describe("intel called")
                            intelCalled = true
                        }
                    }.expectation {
                        arm64Called.thisIsFalse("arm64Called=$arm64Called")
                        intelCalled.thisIsTrue("intelCalled=$intelCalled")
                    }
                }
            }
        }
    }

    @Test
    @Order(40)
    fun osaifuKeitai_osaifuKeitaiNot() {

        var osaifuKeitaiCalled = false
        var osaifuKeitaiNot_Called = false
        fun clear() {
            osaifuKeitaiCalled = false
            osaifuKeitaiNot_Called = false
        }

        scenario {
            if (hasOsaifuKeitai) {
                case(1) {
                    condition {
                        clear()
                    }.action {
                        osaifuKeitai {
                            describe("osaifuKeitai called")
                            osaifuKeitaiCalled = true
                        }
                        osaifuKeitaiNot {
                            describe("osaifuKeitaiNot called")
                            osaifuKeitaiNot_Called = true
                        }
                    }.expectation {
                        osaifuKeitaiCalled.thisIsTrue("osaifuKeitaiCalled=$osaifuKeitaiCalled")
                        osaifuKeitaiNot_Called.thisIsFalse("osaifuKeitaiNot_Called=$osaifuKeitaiNot_Called")
                    }
                }
            } else if (hasOsaifuKeitai.not()) {
                case(1) {
                    condition {
                        clear()
                    }.action {
                        osaifuKeitai {
                            describe("osaifuKeitai called")
                            osaifuKeitaiCalled = true
                        }
                        osaifuKeitaiNot {
                            describe("osaifuKeitaiNot called")
                            osaifuKeitaiNot_Called = true
                        }
                    }.expectation {
                        osaifuKeitaiCalled.thisIsFalse("osaifuKeitaiCalled=$osaifuKeitaiCalled")
                        osaifuKeitaiNot_Called.thisIsTrue("osaifuKeitaiNot_Called=$osaifuKeitaiNot_Called")
                    }
                }
            }
        }
    }

    @Test
    @Order(50)
    fun stub_stubNot() {

        var stubCalled = false
        var stubNotCalled = false
        fun clear() {
            stubCalled = false
            stubNotCalled = false
        }

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                    clear()
                }.action {
                    stub {
                        describe("stub called")
                        stubCalled = true
                    }
                    stubNot {
                        describe("stubNot called")
                        stubNotCalled = true
                    }
                }.expectation {
                    stubCalled.thisIsFalse("stubCalled=$stubCalled")
                    stubNotCalled.thisIsTrue("stubNotCalled=$stubNotCalled")
                }
            }
            case(2) {
                condition {
                    clear()
                }.action {
                    TestMode.runAsStub {
                        stub {
                            describe("stub called")
                            stubCalled = true
                        }
                        stubNot {
                            describe("stubNot called")
                            stubNotCalled = true
                        }
                    }
                }.expectation {
                    stubCalled.thisIsTrue("stubCalled=$stubCalled")
                    stubNotCalled.thisIsFalse("stubNotCalled=$stubNotCalled")
                }
            }
        }
    }

    @Test
    @Order(60)
    fun testRuntimeOnly() {

        var testRuntimeOnlyCalled = false
        var testRuntimeOnlyNotCalled = false
        fun clear() {
            testRuntimeOnlyCalled = false
            testRuntimeOnlyNotCalled = false
        }

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                    clear()
                }.action {
                    testRuntimeOnly {
                        describe("testRuntimeOnly called")
                        testRuntimeOnlyCalled = true
                    }.not {
                        describe("testRuntimeOnlyNot called")
                        testRuntimeOnlyNotCalled = true
                    }
                }.expectation {
                    testRuntimeOnlyCalled.thisIsTrue("testRuntimeOnlyCalled=$testRuntimeOnlyCalled")
                    testRuntimeOnlyNotCalled.thisIsFalse("testRuntimeOnlyNotCalled=$testRuntimeOnlyNotCalled")
                }
            }
            case(2) {
                condition {
                    clear()
                }.action {
                    TestMode.runAsNoLoadRunMode {
                        testRuntimeOnly {
                            describe("testRuntimeOnly called")
                            testRuntimeOnlyCalled = true
                        }.not {
                            describe("testRuntimeOnlyNot called")
                            testRuntimeOnlyNotCalled = true
                        }
                    }
                }.expectation {
                    testRuntimeOnlyCalled.thisIsFalse("testRuntimeOnlyCalled=$testRuntimeOnlyCalled")
                    testRuntimeOnlyNotCalled.thisIsTrue("testRuntimeOnlyNotCalled=$testRuntimeOnlyNotCalled")
                }
            }
        }

    }

    @Test
    @Order(70)
    fun ifCanSelect_ifCanSelectNot() {

        var ifCanSelectCalled = false
        var ifCanSelectNotCalled = false
        fun clear() {
            ifCanSelectCalled = false
            ifCanSelectNotCalled = false
        }

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                    clear()
                }.action {
                    ifCanSelect("no exist") {
                        describe("ifCanSelect('no exist') called")
                        ifCanSelectCalled = true     // never called
                    }
                    ifCanSelectNot("no exist") {
                        describe("ifCanSelectNot('no exist') called")
                        ifCanSelectNotCalled = true     // called
                    }
                }.expectation {
                    ifCanSelectCalled.thisIsFalse("ifCanSelectCalled=$ifCanSelectCalled")
                    ifCanSelectNotCalled.thisIsTrue("ifCanSelectNotCalled=$ifCanSelectNotCalled")
                }
            }
            case(2) {
                condition {
                    clear()
                }.action {
                    ifCanSelect("Connected devices") {
                        describe("ifCanSelect('Connected devices') called")
                        ifCanSelectCalled = true
                    }
                    ifCanSelectNot("Connected devices") {
                        describe("ifCanSelectNot('Connected devices') called")
                        ifCanSelectCalled = true
                    }
                }.expectation {
                    ifCanSelectCalled.thisIsTrue("ifCanSelectCalled=$ifCanSelectCalled")
                    ifCanSelectNotCalled.thisIsFalse("ifCanSelectNotCalled=$ifCanSelectNotCalled")
                }
            }
        }
    }

    @Test
    @Order(80)
    fun ifTrue_ifFalse() {

        var ifTrueCalled = false
        var ifFalseCalled = false
        fun clear() {
            ifTrueCalled = false
            ifFalseCalled = false
        }

        scenario {
            case(1) {
                condition {
                    clear()
                }.action {
                    it.ifTrue(true) {
                        describe("ifTrue(true) called")
                        ifTrueCalled = true
                    }
                    it.ifFalse(true) {
                        describe("ifFalse(true) called")
                        ifFalseCalled = true
                    }
                }.expectation {
                    ifTrueCalled.thisIsTrue("ifTrueCalled=$ifTrueCalled")
                    ifFalseCalled.thisIsFalse("ifFalseCalled=$ifFalseCalled")
                }
            }
            case(2) {
                condition {
                    clear()
                }.action {
                    it.ifTrue(false) {
                        describe("ifTrue(false) called")
                        ifTrueCalled = true
                    }
                    it.ifFalse(false) {
                        describe("ifFalse(false) called")
                        ifFalseCalled = true
                    }
                }.expectation {
                    ifTrueCalled.thisIsFalse("ifTrueCalled=$ifTrueCalled")
                    ifFalseCalled.thisIsTrue("ifFalseCalled=$ifFalseCalled")
                }
            }
        }
    }

    @Test
    @Order(90)
    fun onScreen() {

        var androidSettingsScreenCalled = false
        var networkAndInternetScreenCalled = false
        var notCalled = false
        var ifElseCalled = false
        fun clear() {
            androidSettingsScreenCalled = false
            networkAndInternetScreenCalled = false
            notCalled = false
            ifElseCalled = false
        }

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                    clear()
                }.action {
                    onScreen("[Android Settings Top Screen]") {
                        describe("onScreen([Android Settings Top Screen]) called")
                        androidSettingsScreenCalled = true
                    }.not {
                        describe("onScreen([Android Settings Top Screen]).not called")
                        notCalled = true
                    }
                }.expectation {
                    androidSettingsScreenCalled.thisIsTrue("androidSettingsScreenCalled=$androidSettingsScreenCalled")
                    notCalled.thisIsFalse("notCalled=$notCalled")
                    ifElseCalled.thisIsFalse("ifElseCalled=$ifElseCalled")
                }
            }
            case(2) {
                condition {
                    it.tap("[Network & internet]")
                        .screenIs("[Network & internet Screen]")
                    clear()
                }.action {
                    onScreen("[Android Settings Top Screen]") {
                        describe("onScreen([Android Settings Top Screen]) called")
                        androidSettingsScreenCalled = true
                    }.not {
                        describe("onScreen([Android Settings Top Screen]).not called")
                        notCalled = true
                    }
                }.expectation {
                    androidSettingsScreenCalled.thisIsFalse("androidSettingsScreenCalled=$androidSettingsScreenCalled")
                    networkAndInternetScreenCalled.thisIsFalse("networkAndInternetScreenCalled=$networkAndInternetScreenCalled")
                    notCalled.thisIsTrue("notCalled=$notCalled")
                    ifElseCalled.thisIsFalse("ifElseCalled=$ifElseCalled")
                }
            }
            case(3) {
                condition {
                    clear()
                }.action {
                    onScreen("[Android Settings Top Screen]") {
                        describe("onScreen([Android Settings Top Screen]) called")
                        androidSettingsScreenCalled = true
                    }.onScreen("[Network & internet Screen]") {
                        describe("onScreen([Network & internet Screen]) called")
                        networkAndInternetScreenCalled = true
                    }.not {
                        describe("onScreen([Android Settings Top Screen]).not called")
                        notCalled = true
                    }.ifElse {
                        describe("onScreen([Android Settings Top Screen]).ifElse called")
                        ifElseCalled = true
                    }
                }.expectation {
                    androidSettingsScreenCalled.thisIsFalse("androidSettingsScreenCalled=$androidSettingsScreenCalled")
                    networkAndInternetScreenCalled.thisIsTrue("networkAndInternetScreenCalled=$networkAndInternetScreenCalled")
                    notCalled.thisIsFalse("notCalled=$notCalled")
                    ifElseCalled.thisIsFalse("ifElseCalled=$ifElseCalled")
                }
            }
            case(4) {
                condition {
                    clear()
                }.action {
                    onScreen("[About emulated device Screen]") {

                    }.ifElse {
                        ifElseCalled = true
                    }
                }.expectation {
                    androidSettingsScreenCalled.thisIsFalse("androidSettingsScreenCalled=$androidSettingsScreenCalled")
                    networkAndInternetScreenCalled.thisIsFalse("networkAndInternetScreenCalled=$networkAndInternetScreenCalled")
                    notCalled.thisIsFalse("notCalled=$notCalled")
                    ifElseCalled.thisIsTrue("ifElseCalled=$ifElseCalled")
                }
            }
        }
    }
}