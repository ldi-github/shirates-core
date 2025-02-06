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

### サンプルコード

[サンプルの入手](../../../getting_samples_ja.md)

### Scroll1.kt

(`kotlin/tutorial/basic/Scroll1.kt`)

```kotlin
    @Test
    @Order(10)
    fun scrollDown_scrollUp() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android設定トップ画面]")
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
                        .scrollDown(scrollDurationSeconds = 5.0, startMarginRatio = 0.1)
                        .scrollDown(scrollDurationSeconds = 3.0, startMarginRatio = 0.3)
                        .scrollUp(scrollDurationSeconds = 5.0, startMarginRatio = 0.1)
                        .scrollUp(scrollDurationSeconds = 3.0, startMarginRatio = 0.3)
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
                    it.macro("[Android設定トップ画面]")
                }.action {
                    it.scrollToBottom(repeat = 2)
                }.expectation {
                    it.exist("ヒントとサポート")
                }
            }
            case(2) {
                action {
                    it.scrollToTop(repeat = 2)
                }.expectation {
                    it.exist("設定")
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
                    it.macro("[Android設定トップ画面]")
                }.expectation {
                    withScrollDown {
                        it
                            .detect("通知").textIs("通知")
                            .detect("ユーザー補助").textIs("ユーザー補助")
                            .detect("ヒントとサポート").textIs("ヒントとサポート")
                    }
                    withScrollUp {
                        it
                            .detect("ユーザー補助").textIs("ユーザー補助")
                            .detect("通知").textIs("通知")
                    }
                }
            }
            case(2) {
                expectation {
                    withScrollDown {
                        it
                            .exist("通知")
                            .exist("ユーザー補助")
                            .exist("ヒントとサポート")
                    }
                    withScrollUp {
                        it
                            .exist("ヒントとサポート")
                            .exist("ユーザー補助")
                            .exist("通知")
                    }
                }
            }
            case(3) {
                action {
                    withScrollDown {
                        it.tap("ユーザー補助")
                    }
                }.expectation {
                    it.screenIs("[ユーザー補助画面]")
                }
            }
            case(4) {
                action {
                    it.pressBack()
                    withScrollUp {
                        it.tap("ネットワークとインターネット")
                    }
                }.expectation {
                    it.screenIs("[ネットワークとインターネット画面]")
                }
            }
        }
    }
```

### Link

- [index](../../../../index_ja.md)

