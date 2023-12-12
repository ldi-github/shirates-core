# scroll

これらの関数を使用してスクロールを行うことができます。

## スクロール関数

| 関数                | 説明                    |
|:------------------|:----------------------|
| scrollDown        | 下方向にスクロールします          |
| scrollUp          | 上方向にスクロールします          |
| scrollRight       | 右方向にスクロールします          |
| scrollLeft        | 左方向にスクロールします          |
| scrollToBottom    | スクロール領域の下端までスクロールします  |
| scrollToTop       | スクロール領域の上端までスクロールします  |
| scrollToRightEdge | スクロール領域の右端までスクロールします  |
| scrollToLeftEdge  | スクロール領域の左端までスクロールします  |
| withScrollDown    | 下方向にスクロールしながら要素を選択します |
| withScrollUp      | 上方向にスクロールしながら要素を選択します |
| withScrollRight   | 右方向にスクロールしながら要素を選択します |
| withScrollLeft    | 左方向にスクロールしながら要素を選択します |
| withoutScroll     | スクロールしないで要素を選択肢ます     |

## 例

### Scroll1.kt

(`kotlin/tutorial/basic/Scroll1.kt`)

```kotlin
package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class Scroll1 : UITest() {

    @Test
    @Order(10)
    fun scrollDown_scrollUp() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    it
                        .scrollDown()
                        .scrollDown()
                        .scrollUp()
                        .scrollUp()
                }
            }
            case(2) {
                action {
                    it
                        .scrollDown(durationSeconds = 5.0, startMarginRatio = 0.1)
                        .scrollDown(durationSeconds = 3.0, startMarginRatio = 0.3)
                        .scrollUp(durationSeconds = 5.0, startMarginRatio = 0.1)
                        .scrollUp(durationSeconds = 3.0, startMarginRatio = 0.3)
                }
            }
        }
    }

    @Test
    @Order(20)
    fun scrollToBottom_scrollToTop() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    it.scrollToBottom(repeat = 2)
                }.expectation {
                    it.exist("[Tips & support]")
                }
            }
            case(2) {
                action {
                    it.scrollToTop(repeat = 2)
                }.expectation {
                    it.exist("Settings")
                }
            }
        }
    }

    @Test
    @Order(30)
    fun withScroll() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    withScrollDown {
                        it
                            .select("[Notifications]").textIs("Notifications")
                            .select("[Accessibility]").textIs("Accessibility")
                            .select("[Tips & support]").textIs("Tips & support")
                    }
                    withScrollUp {
                        it
                            .select("[Accessibility]").textIs("Accessibility")
                            .select("[Notifications]").textIs("Notifications")
                    }
                }
            }
            case(2) {
                expectation {
                    withScrollDown {
                        it
                            .exist("[Notifications]")
                            .exist("[Accessibility]")
                            .exist("[Tips & support]")
                    }
                    withScrollUp {
                        it
                            .exist("[Tips & support]")
                            .exist("[Accessibility]")
                            .exist("[Notifications]")
                    }
                }
            }
            case(3) {
                action {
                    withScrollDown {
                        it.tap("[Accessibility]")
                    }
                }.expectation {
                    it.screenIs("[Accessibility Screen]")
                }
            }
            case(4) {
                action {
                    it.tap("[<-]")
                    withScrollUp {
                        it.tap("[Network & internet]")
                    }
                }.expectation {
                    it.screenIs("[Network & internet Screen]")
                }
            }
        }
    }

}
```

### Link

- [index](../../../index_ja.md)

