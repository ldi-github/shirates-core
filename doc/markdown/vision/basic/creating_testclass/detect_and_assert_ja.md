# テキストを検出して検証する

**AI-OCRのテキスト認識** を使用して画面内のテキストを検出し、その値を検証することができます。

## サンプルコード

[サンプルの入手](../../../vision/getting_samples_ja.md)

### DetectAndAssert1.kt

(`src/test/kotlin/tutorial/basic/DetectAndAssert1.kt`)

```kotlin
    @Test
    @Order(10)
    fun detectAndAssert1_OK() {

        scenario {
            case(1) {
                expectation {
                    it.detect("設定")
                        .textIs("設定")   // OK
                }
            }
        }
    }

    @Test
    @Order(20)
    fun detectAndAssert2_NG() {

        scenario {
            case(1) {
                expectation {
                    it.detect("設定")
                        .textIs("ネットワークとインターネット")   // NG
                }
            }
        }
    }
```

### テストを実行する

1. Androidで実行されるように`testrun.global.properties`の`os`を設定します (デフォルトは`android`
   なので単にコメントアウトします)。

```properties
## OS --------------------
os=android
```

2. テストを右クリックして`Debug`を選択します。

上の例では**detect**関数がスクリーンショットのテキストを認識し、"設定"に一致するテキストを探して`VisionElement`
として返します。
テキストの値が期待した値に一致する場合以下のような検証ログが出力されます。

```
[OK]	+5	!	(textIs)	<設定>: "設定"
```

### Link

- [index](../../../index_ja.md)
