# tap

これらの関数を使用して画面上の要素をタップすることができます。

## 関数

| 関数                 | 説明                                     |
|:-------------------|:---------------------------------------|
| tap(expression)    | selectorにマッチする最初の要素をタップします（現在の画面）      |
| tap(x, y)          | (x, y)座標をタップします                        |
| tapWithoutScroll   | selectorにマッチする最初の要素をタップします（スクロールなし）    |
| tapWithScrollDown  | selectorにマッチする最初の要素をタップします（下方向スクロールあり） |
| tapWithScrollUp    | selectorにマッチする最初の要素をタップします（上方向スクロールあり） |
| tapWithScrollRight | selectorにマッチする最初の要素をタップします（右方向スクロールあり） |
| tapWithScrollLeft  | selectorにマッチする最初の要素をタップします（左方向スクロールあり） |
| tapCenterOfScreen  | 画面の中心をタップします                           |
| tapCenterOf        | 要素の中心をタップします                           |

## 例

### Tap1.kt

(`kotlin/tutorial/basic/Tap1.kt`)

```kotlin
package tutorial.basic

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.screenIs
import shirates.core.driver.commandextension.select
import shirates.core.driver.commandextension.tap
import shirates.core.driver.commandextension.tapWithScrollDown
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class Tap1 : UITest() {

    @Test
    fun tap() {

        scenario {
            case(1) {
                action {
                    it.tap("Network & internet")
                        .tap("Internet")
                    it.tap("@Navigate up")
                        .tap("@Navigate up")
                }
            }
            case(2) {
                action {
                    it.tapWithScrollDown("Display")
                        .tapWithScrollDown("Colors")
                    it.tap("@Navigate up")
                        .tap("@Navigate up")
                }
            }
        }
    }

    @Test
    fun tapByCoordinates() {

        scenario {
            case(1) {
                action {
                    val e = select("Network & internet")
                    it.tap(x = e.bounds.centerX, y = e.bounds.centerY)
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
