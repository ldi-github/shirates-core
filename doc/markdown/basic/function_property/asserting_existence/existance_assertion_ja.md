# 要素が存在することの検証

これらの関数を使用して要素が存在することを確認できます。

## 関数

|   グループ    | 関数                      | 説明                           | 返却値                                             |
|:---------:|:------------------------|:-----------------------------|:------------------------------------------------|
|   exist   | exist                   | 要素が存在することを検証します（表示中の画面）      | TestElement(存在する場合)<br>TestNGException(存在しない場合) |
|   exist   | existWithScrollDown     | 要素が存在することを検証します（下方向スクロールあり）  | (上と同じ)                                          |
|   exist   | existWithScrollUp       | 要素が存在することを検証します（上方向スクロールあり）  | (上と同じ)                                          |
|   exist   | existInScanResults      | スキャン結果に要素が存在することを検証します       | (上と同じ)                                          |
| dontExist | dontExist               | 要素が存在しないことを検証します（表示中の画面）     | 空要素(存在しない場合)<br>TestNGException(存在する場合)         |
| dontExist | dontExistWithScrollDown | 要素が存在しないことを検証石ます（下方向スクロールあり） | (上と同じ)                                          |
| dontExist | dontExistWithScrollUp   | 要素が存在しないことを検証石ます（上方向スクロールあり） | (上と同じ)                                          |
| dontExist | dontExistInScanResults  | スキャン結果内に要素が存在しないことを検証します     | (上と同じ)                                          |

## 例

### ExistDontExist1.kt

(`kotlin/tutorial/basic/ExistDontExist1.kt`)

```kotlin
package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class ExistDontExist1 : UITest() {

    @Test
    @Order(10)
    fun exist_OK() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.exist("Network & internet")
                }
            }
        }
    }

    @Test
    @Order(20)
    fun exist_NG() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.exist("System")
                }
            }
        }
    }

    @Test
    @Order(30)
    fun existWithScrollDown_OK() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.existWithScrollDown("System")
                }
            }
        }
    }

    @Test
    @Order(40)
    fun existWithScrollDown_NG() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.existWithScrollDown("Network business")
                }
            }
        }
    }

    @Test
    @Order(50)
    fun existWithScrollUp_OK() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                        .flickBottomToTop()
                }.expectation {
                    it.existWithScrollUp("Network & internet")
                }
            }
        }
    }

    @Test
    @Order(60)
    fun existWithScrollUp_NG() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                        .flickBottomToTop()
                }.expectation {
                    it.existWithScrollUp("Network business")
                }
            }
        }
    }

    @Test
    @Order(70)
    fun existInScanResult_OK() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    describe("Scans elements with scrolling down.")
                        .scanElements()
                }.expectation {
                    describe("Asserts that expected elements exist in scan results.")
                        .existInScanResults("Network & internet")
                        .existInScanResults("Storage")
                        .existInScanResults("System")
                }
            }
        }
    }

    @Test
    @Order(80)
    fun existInScanResult_NG() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.condition {
                    describe("Scans elements with scrolling down.")
                        .scanElements()
                }.expectation {
                    it.existInScanResults("Network & internet")
                        .existInScanResults("Storage")
                        .existInScanResults("System")
                        .existInScanResults("Network business")
                }
            }
        }
    }

    @Test
    @Order(90)
    fun dontExist_OK() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.dontExist("System")
                }
            }
        }
    }

    @Test
    @Order(100)
    fun dontExist_NG() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.dontExist("Network & internet")
                }
            }
        }
    }

    @Test
    @Order(110)
    fun dontExistWithScrollDown_OK() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.dontExistWithScrollDown("Network business")
                }
            }
        }
    }

    @Test
    @Order(120)
    fun dontExistWithScrollDown_NG() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.dontExistWithScrollDown("System")
                }
            }
        }
    }

    @Test
    @Order(130)
    fun dontExistWithScrollUp_OK() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.condition {
                    it.flickBottomToTop()
                }.expectation {
                    it.dontExistWithScrollUp("Network business")
                }
            }
        }
    }

    @Test
    @Order(140)
    fun dontExistWithScrollUp_NG() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.condition {
                    it.flickBottomToTop()
                }.expectation {
                    it.dontExistWithScrollUp("System")
                }
            }
        }
    }

    @Test
    @Order(150)
    fun dontExistInScanResults_OK() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.condition {
                    it.scanElements()
                }.expectation {
                    it.dontExistInScanResults("Switch")
                        .dontExistInScanResults("PS5")
                        .dontExistInScanResults("XBOX")
                }
            }
        }
    }

    @Test
    @Order(160)
    fun dontExistInScanResults_NG() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.condition {
                    it.scanElements()
                }.expectation {
                    it.dontExistInScanResults("Switch")
                        .dontExistInScanResults("PS5")
                        .dontExistInScanResults("XBOX")
                        .dontExistInScanResults("Network & internet")
                }
            }
        }
    }

}
```

### Link

- [index](../../../index_ja.md)


