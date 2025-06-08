package shirates.core.vision.uitest.ios.driver.commandextension

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.vision.driver.commandextension.*
import shirates.core.vision.testcode.VisionTest

@Testrun("unitTestConfig/ios/iOSSettings/testrun.properties")
class VisionDriveRowExtensionTest : VisionTest() {

    @Test
    @Order(10)
    fun row_onRowOf() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                }.expectation {
                    it.detect("Apple Account")
                        .row()
                        .onThisElementRegion {
                            it.exist("Sign in to access*")
                            it.dontExist("General")
                        }
                    it.detect("General")
                        .row()
                        .onThisElementRegion {
                            it.dontExist("Sign in to access*")
                            it.exist("General")
                        }
                }
            }
            case(2) {
                expectation {
                    it.detect("Apple Account").onRow {
                        it.exist("Sign in to access*")
                        it.dontExist("General")
                    }
                    it.detect("General").onRow {
                        it.dontExist("Sign in to access*")
                        it.exist("General")
                    }
                }
            }
            case(3) {
                expectation {
                    it.onRowOf("Apple Account") {
                        it.exist("Sign in to access*")
                        it.dontExist("General")
                    }
                    it.onRowOf("General") {
                        it.dontExist("Sign in to access*")
                        it.exist("General")
                    }
                }
            }
            case(4) {
                expectation {
                    it.onRowOfWithScrollDown("iCloud") {
                        it.exist("iCloud")
                        it.dontExist("Wallet")
                    }
                    it.onRowOfWithScrollDown("Wallet") {
                        it.dontExist("iCloud")
                        it.exist("Wallet")
                    }
                }
            }
        }
    }

}