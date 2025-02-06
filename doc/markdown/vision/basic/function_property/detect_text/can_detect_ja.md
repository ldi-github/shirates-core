# canDetect (Vision)

これらの関数を使用して要素を取得できるかどうかを調べることができます。関数はtrueまたはfalseを返します。

## 関数

| 関数                       | 説明                                                                               |
|:-------------------------|:---------------------------------------------------------------------------------|
| canDetect                | 現在の画面内でセレクターにマッチする最初の要素を取得し、trueまたはfalseを返します。 `withScroll`関数内で使用するとスクロールが発生します。 |
| canDetectWithScrollDown  | セレクターにマッチする最初の要素を取得し、true または falseを返します。（下方向スクロールあり）                            |
| canDetectWithScrollUp    | セレクターにマッチする最初の要素を取得し、true または falseを返します。（上方向スクロールあり）                            |
| canDetectWithScrollRight | セレクターにマッチする最初の要素を取得し、true または falseを返します。（右方向スクロールあり）                            |
| canDetectWithScrollLeft  | セレクターにマッチする最初の要素を取得し、true または falseを返します。（左方向スクロールあり）                            |
| canDetectWithoutScroll   | セレクターにマッチする最初の要素を取得し、true または falseを返します。（スクロールなし）                               |
| canDetectAll             | セレクターにマッチする全ての要素を取得し、true または falseを返します。 `withScroll`関数内で使用するとスクロールが発生します。      |

### サンプルコード

[サンプルの入手](../../../getting_samples_ja.md)

### CanDetect1.kt

(`src/test/kotlin/tutorial/basic/CanDetect1.kt`)

```kotlin
    @Test
    @Order(10)
    fun canDetect() {

        scenario {
            case(1) {
                expectation {
                    it.canDetect("設定")
                        .thisIsTrue("<設定>が見つかりました")
                }
            }
            case(2) {
                expectation {
                    it.canDetectWithScrollDown("システム")
                        .thisIsTrue("<システム>が見つかりました（下方向スクロールあり）")
                }
            }
            case(3) {
                expectation {
                    it.canDetectWithScrollUp("設定")
                        .thisIsTrue("<設定>が見つかりました（上方向スクロールあり）")
                }
            }
            case(4) {
                expectation {
                    withScrollDown {
                        it.canDetectAll("設定", "システム")
                            .thisIsTrue("<設定>が見つかりました（下方向スクロールあり）")
                    }
                }
            }
            case(5) {
                expectation {
                    withScrollUp {
                        it.canDetectAll("設定", "システム")
                            .thisIsTrue("<設定>が見つかりました（上方向スクロールあり）")
                    }
                }
            }
        }
    }
```

### Link

- [index](../../../../index_ja.md)
