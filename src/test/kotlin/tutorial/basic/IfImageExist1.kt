package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.configuration.repository.ImageFileRepository
import shirates.core.driver.branchextension.ifImageExist
import shirates.core.driver.branchextension.ifImageExistNot
import shirates.core.driver.commandextension.macro
import shirates.core.driver.commandextension.scrollToBottom
import shirates.core.logging.TestLog
import shirates.core.testcode.UITest
import shirates.helper.TestSetupHelper

@Testrun("testConfig/android/androidSettings/testrun.properties")
class IfImageExist1 : UITest() {

    @Test
    @Order(0)
    fun setupImage() {

        TestSetupHelper.setupImageAndroidSettingsTopScreen()
    }

    @Test
    @Order(10)
    fun ifImageExistTest() {

        ImageFileRepository.setup(screenDirectory = TestLog.testResults.resolve("images"))

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    ifImageExist("[Network & internet Icon].png") {
                        OK("ifImageExist called")
                    }.ifElse {
                        NG()
                    }

                    ifImageExistNot("[System Icon].png") {
                        OK("ifImageExistNot called")
                    }.ifElse {
                        NG()
                    }
                }
            }
            case(2) {
                action {
                    it.scrollToBottom()
                }.expectation {
                    ifImageExist("[Network & internet Icon].png") {
                        NG()
                    }.ifElse {
                        OK("ifElse called")
                    }

                    ifImageExistNot("[System Icon].png") {
                        NG()
                    }.ifElse {
                        OK("ifElse called")
                    }
                }
            }
        }
    }

}