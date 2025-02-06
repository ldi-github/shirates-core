# detect (Vision)

これらの関数を使用してテキスト要素を検出することができます。

[セレクター式](../../selector_and_nickname/selector_expression.md) を引数に取ります。

関数は`VisionElement`オブジェクトを返します。

## 関数

| 関数                    | 説明                                                              |
|:----------------------|:----------------------------------------------------------------|
| detect                | 現在の画面内でセレクターにマッチする最初の要素を取得します。`withScroll`関数内で使用するとスクロールが発生します。 |
| detectWithScrollDown  | セレクターにマッチする最初の要素を取得します。（下方向スクロールあり）                             |
| detectWithScrollUp    | セレクターにマッチする最初の要素を取得します。（上方向スクロールあり）                             |
| detectWithScrollRight | セレクターにマッチする最初の要素を取得します。（右方向スクロールあり）                             |
| detectWithScrollLeft  | セレクターにマッチする最初の要素を取得します。（左方向スクロールあり）                             |
| detectWithoutScroll   | セレクターにマッチする最初の要素を取得します。（スクロールなし）                                |

### サンプルコード

[サンプルの入手](../../../getting_samples_ja.md)

### Detect1.kt

(`kotlin/tutorial/basic/Detect1.kt`)

```kotlin
    @Test
    @Order(10)
    fun detect() {

        scenario {
            case(1) {
                action {
                    it.detect("設定を検索")
                    output(it)

                    it.detect("ネットワークとインターネット")
                    output(it)
                }
            }
        }
    }

    @Test
    @Order(20)
    fun detectWithScrollDown_detectWithScrollUp() {

        scenario {
            case(1) {
                action {
                    it.detectWithScrollDown("ヒントとサポート")
                    output(it)
                }
            }
        }
    }
```

### Link

- [index](../../../../index_ja.md)
