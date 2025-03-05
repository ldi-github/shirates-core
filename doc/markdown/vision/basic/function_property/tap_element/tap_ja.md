# tap (Vision)

これらの関数を使用して画面上の要素をタップすることができます。

## 関数

| 関数                               | 説明                                                                  |
|:---------------------------------|:--------------------------------------------------------------------|
| tap(_expression_)                | _expression_ にマッチする最初の要素をタップします。**withScroll** 関数内で使用するとスクロールが発生します |
| tap(_x, y_)                      | (_x, y_)座標をタップします                                                   |
| tapWithScrollDown(_expression_)  | _expression_ にマッチする最初の要素をタップします（下方向スクロールあり）                         |
| tapWithScrollUp(_expression_)    | _expression_ にマッチする最初の要素をタップします（上方向スクロールあり）                         |
| tapWithScrollRight(_expression_) | _expression_ にマッチする最初の要素をタップします（右方向スクロールあり）                         |
| tapWithScrollLeft(_expression_)  | _expression_ にマッチする最初の要素をタップします（左方向スクロールあり）                         |
| tapWithoutScroll(_expression_)   | _expression_ にマッチする最初の要素をタップします（スクロールなし）                            |
| tapCenterOfScreen                | 画面の中心をタップします                                                        |
| tapCenterOf(_expression_)        | 要素の中心をタップします                                                        |
| tapItemUnder(_expression_)       | _expression_ にマッチする要素の下の要素をタップします                                   |
| tapItemOver(_expression_)        | _expression_ にマッチする要素の上の要素をタップします                                   |
| tapItemRightOf(_expression_)     | _expression_ にマッチする要素の右の要素をタップします                                   |
| tapItemLeftOf(_expression_)      | _expression_ にマッチする要素の左の要素をタップします                                   |

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
