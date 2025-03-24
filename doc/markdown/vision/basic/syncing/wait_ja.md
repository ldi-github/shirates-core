# wait (Vision)

特定の条件下では数秒待つことが必要になる場合があります。

**wait**関数を使用することができます。

引数`waitSeconds`を指定せずに使用した場合はデフォルトで`shortWaitSeconds`が適用されます。
`shortWaitSeconds`はtestrunファイルで設定することができます。

参照 [パラメーター](../../../common/parameter/parameters_ja.md)

## サンプルコード

[サンプルの入手](../../getting_samples_ja.md)

### Wait1.kt

(`src/test/kotlin/tutorial/basic/Wait1.kt`)

```kotlin
    @Test
    @Order(10)
    fun wait1() {

        scenario {
            case(1) {
                action {
                    describe("Wait for a short time.")
                        .wait()
                }
            }

            case(2) {
                action {
                    describe("Wait for 3.0 seconds.")
                        .wait(3.0)
                }
            }
        }
    }
```

### Link

- [index](../../../index_ja.md)
