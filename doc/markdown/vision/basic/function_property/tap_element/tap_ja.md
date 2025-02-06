# tap (Vision)

これらの関数を使用して画面上の要素をタップすることができます。

## 関数

| 関数                 | 説明                                     |
|:-------------------|:---------------------------------------|
| tap(expression)    | selectorにマッチする最初の要素をタップします（現在の画面）      |
| tap(x, y)          | (x, y)座標をタップします                        |
| tapWithScrollDown  | selectorにマッチする最初の要素をタップします（下方向スクロールあり） |
| tapWithScrollUp    | selectorにマッチする最初の要素をタップします（上方向スクロールあり） |
| tapWithScrollRight | selectorにマッチする最初の要素をタップします（右方向スクロールあり） |
| tapWithScrollLeft  | selectorにマッチする最初の要素をタップします（左方向スクロールあり） |
| tapWithoutScroll   | selectorにマッチする最初の要素をタップします（スクロールなし）    |
| tapCenterOfScreen  | 画面の中心をタップします                           |
| tapCenterOf        | 要素の中心をタップします                           |

### サンプルコード

[サンプルの入手](../../../getting_samples_ja.md)

## 例

### Tap1.kt

(`kotlin/tutorial/basic/Tap1.kt`)

```kotlin
    @Test
    fun tap() {

        scenario {
            case(1) {
                action {
                    it.tap("ネットワークとインターネット")
                        .tap("AndroidWifi")
                    it.pressBack()
                        .pressBack()
                }
            }
            case(2) {
                action {
                    it.tapWithScrollDown("ディスプレイ")
                        .tapWithScrollDown("カラー")
                    it.pressBack()
                        .pressBack()
                }
            }
        }
    }

    @Test
    fun tapByCoordinates() {

        scenario {
            case(1) {
                action {
                    val v = detect("ネットワークとインターネット")
                    it.tap(x = v.bounds.centerX, y = v.bounds.centerY)
                }.expectation {
                    it.screenIs("[ネットワークとインターネット画面]")
                }
            }
        }
    }
```

### Link

- [index](../../../../index_ja.md)
