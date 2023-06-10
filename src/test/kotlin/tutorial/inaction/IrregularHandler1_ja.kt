package tutorial.inaction

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.TestDriverEventContext
import shirates.core.driver.branchextension.ifCanSelect
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class IrregularHandler1_ja : UITest() {

    /**
     * 注意:
     * このサンプルコードはコンセプトを説明するものです。実行することはできません。
     */

    override fun setEventHandlers(context: TestDriverEventContext) {

        context.irregularHandler = {
            ifCanSelect("アプリの使用時のみ") {
                it.tap()
            }
        }
    }

    @Test
    @Order(10)
    fun irregularHandler1() {

        scenario {
            case(1) {
                condition {
                    it.macro("[画面A]")
                }.action {
                    it.tap("[ボタン1]")
                }.expectation {
                    it.screenIs("[画面B]")
                }
            }
        }
    }

    @Test
    @Order(20)
    fun suppressHandler_useHandler() {

        scenario {
            case(1) {
                condition {
                    it.macro("[画面A]")
                }.action {
                    /**
                     * suppressHandlerブロックの中では
                     * イレギュラーハンドラーは呼び出されません
                     */
                    suppressHandler {
                        it.tap("[ボタン1]")
                    }
                }.expectation {
                    it.screenIs("[画面B]")
                }
            }
            case(2) {
                action {
                    /**
                     * suppressHandlerブロックの中では
                     * イレギュラーハンドラーは呼び出されません
                     */
                    suppressHandler {
                        it.tap("[ボタン2]")

                        /**
                         * useHandlerブロックの中では
                         * suppressHandlerブロックの中に入れ子になっていた場合でも
                         * イレギュラーハンドラーは呼び出されます
                         */
                        useHandler {
                            it.tap("[ボタン3]")
                        }
                    }
                }
            }
        }
    }

    @Test
    @Order(30)
    fun disableHandler_EnableHandler() {

        scenario {
            case(1) {
                condition {
                    it.macro("[画面A]")
                }.action {
                    disableHandler()    // イレギュラーハンドラーの呼び出しは無効になります
                    it.tap("[ボタン1]")
                    ifCanSelect("アプリの使用時のみ") {
                        it.tap()
                    }
                    enableHandler()     // イレギュラーハンドラーの呼び出しは再び有効になります
                }.expectation {
                    it.screenIs("[画面B]")
                }
            }
        }
    }
}