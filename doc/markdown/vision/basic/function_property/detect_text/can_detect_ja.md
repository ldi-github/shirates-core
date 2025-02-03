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
                    it.canDetect("Settings")
                        .thisIsTrue("<Settings> found.")
                }
            }
            case(2) {
                expectation {
                    it.canDetectWithScrollDown("System")
                        .thisIsTrue("<System> found with scroll down.")
                }
            }
            case(3) {
                expectation {
                    it.canDetectWithScrollUp("Settings")
                        .thisIsTrue("<Settings> found with scroll up.")
                }
            }
            case(4) {
                expectation {
                    withScrollDown {
                        it.canDetectAll("Settings", "System")
                            .thisIsTrue("<Settings> found with scroll down.")
                    }
                }
            }
            case(5) {
                expectation {
                    withScrollUp {
                        it.canDetectAll("Settings", "System")
                            .thisIsTrue("<Settings> found with scroll up.")
                    }
                }
            }
        }
    }
```

### Link

- [index](../../../../index_ja.md)
