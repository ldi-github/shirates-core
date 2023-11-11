package tutorial.inaction

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.tap
import shirates.core.driver.platformMajorVersion
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class AdaptingToChangesInMessage1_ja : UITest() {

    /**
     * 注意:
     * このサンプルコードはコンセプトを説明するものです。実行することはできません。
     */

    @Test
    @Order(10)
    fun original() {

        scenario {
            case(1) {
                action {
                    it.tap("アプリの使用中のみ許可")
                }
            }
        }
    }

    @Test
    @Order(20)
    fun branch() {

        scenario {
            case(1) {
                action {
                    if (platformMajorVersion < 11) {
                        it.tap("アプリの使用中のみ許可")
                    } else {
                        it.tap("アプリの使用時のみ")
                    }
                }
            }
        }
    }

    @Test
    @Order(30)
    fun selector() {

        scenario {
            case(1) {
                action {
                    it.tap("アプリの使用中のみ許可||アプリの使用時のみ")
                }
            }
        }
    }

    @Test
    @Order(40)
    fun nickname() {

        scenario {
            case(1) {
                action {
                    it.tap("[アプリの使用時のみ]")
                }
            }
        }
    }
}