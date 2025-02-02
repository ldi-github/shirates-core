# canDetect

これらの関数を使用して要素が選択可能かどうかを調べることができます。関数はtrueまたはfalseを返します。

## 関数

| 関数                         | 説明                                                                      |
|:---------------------------|:------------------------------------------------------------------------|
| canSelect                  | 現在の画面においてselectorにマッチする最初の要素を検索してtrue/falseを返します（withScroll使用時はスクロールあり） |
| canSelectWithoutScroll     | selectorにマッチする最初の要素を検索してtrue/falseを返します(スクロールなし)                        |
| canSelectWithScrollDown    | selectorにマッチする最初の要素を検索してtrue/falseを返します(下方向スクロールあり)                     |
| canSelectWithScrollUp      | selectorにマッチする最初の要素を検索してtrue/falseを返します(上方向スクロールあり)                     |
| canSelectWithScrollRight   | selectorにマッチする最初の要素を検索してtrue/falseを返します(右方向スクロールあり)                     |
| canSelectWithScrollLeft    | selectorにマッチする最初の要素を検索してtrue/falseを返します(左方向スクロールあり)                     |
| canSelectAllWithScrollDown | 指定した全てのselectorについてselect可能であるかどうかをtrue/falseで返します(下方向スクロールあり)          |
| canSelectAllWithScrollUp   | 指定した全てのselectorについてselect可能であるかどうかをtrue/falseで返します(上方向スクロールあり)          |
| canSelectInScanResults     | スキャン結果内でselectorにマッチする最初の要素を検索してtrue/falseを返します                         |
| canSelectAllInScanResults  | 指定した全てのselectorについてselect可能であるかどうかをtrue/falseで返します                      |

## 例1: canSelect

### CanSelect1.kt

(`kotlin/tutorial/basic/CanSelect1.kt`)

```kotlin
    @Test
    @Order(10)
    fun canSelect() {

        scenario {
            case(1) {
                action {
                    it.canSelect("Settings", log = true)
                }
            }
            case(2) {
                action {
                    it.canSelectWithScrollDown("System", log = true)
                }
            }
            case(3) {
                action {
                    it.canSelectWithScrollUp("Settings", log = true)
                }
            }
            case(4) {
                action {
                    it.canSelectAllWithScrollDown("Settings", "System", log = true)
                }
            }
            case(5) {
                action {
                    it.canSelectAllWithScrollUp("Settings", "System", log = true)
                }
            }
        }
    }
```

## 例2: canSelectInScanElements

### CanSelect1.kt

(`kotlin/tutorial/basic/CanSelect1.kt`)

```kotlin
    @Test
    @Order(20)
    fun canSelectInScanElements() {

        scenario {
            case(1) {
                condition {
                    it.scanElements()
                }.action {
                    it.canSelectInScanResults("Settings", log = true)
                    it.canSelectInScanResults("Accessibility", log = true)
                    it.canSelectInScanResults("System", log = true)
                    it.canSelectInScanResults("Foo", log = true)
                }
            }
            case(2) {
                action {
                    it.canSelectAllInScanResults("Settings", "Accessibility", "System", log = true)
                    it.canSelectAllInScanResults("Settings", "Accessibility", "Foo", log = true)
                }
            }
        }
    }
```

### Link

- [index](../../../index_ja.md)
