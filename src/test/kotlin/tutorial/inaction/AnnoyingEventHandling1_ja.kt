package tutorial.inaction

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.branchextension.ifCanSelect
import shirates.core.driver.commandextension.macro
import shirates.core.driver.commandextension.screenIs
import shirates.core.driver.commandextension.tap
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class AnnoyingEventHandling1_ja : UITest() {

    /**
     * 注意:
     * このサンプルコードはコンセプトを説明するものです。実行することはできません。
     */

    /**
     * 面倒なイベントハンドリングの例1
     */
    @Test
    @Order(10)
    fun annoyingEventHandling1() {

        scenario {
            case(1) {
                condition {
                    it.macro("[画面A]")
                        .ifCanSelect("アプリの使用時のみ") {
                            it.tap()
                        }
                }.action {
                    it.tap("[ボタン1]")
                        .ifCanSelect("アプリの使用時のみ") {
                            it.tap()
                        }
                }.expectation {
                    it.screenIs("[画面B]")
                }
            }
        }
    }

    /**
     * 面倒なイベントハンドリングの例2
     */
    @Test
    @Order(20)
    fun annoyingEventHandling2() {

        scenario {
            case(1) {
                condition {
                    it.macro("[画面B]")
                        .ifCanSelect("アプリの使用時のみ") {
                            it.tap()
                        }
                }.action {
                    it.tap("[ボタン2]")
                        .ifCanSelect("アプリの使用時のみ") {
                            it.tap()
                        }
                }.expectation {
                    it.screenIs("[画面C]")
                }
            }
        }
    }

}