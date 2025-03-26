# clearInput (Vision)

**clearInput**関数を使用するとinputウィジェットの値をクリアすることができます。

## サンプルコード

[サンプルの入手](../../../getting_samples_ja.md)

### AndroidSendKeys1.kt

(`src/test/kotlin/tutorial/basic/AndroidSendKeys1.kt`)

```kotlin
    @Test
    @Order(10)
    fun sendKeys_clearInput() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android設定検索画面]")
                }.action {
                    it.sendKeys("時計")
                }.expectation {
                    it.textIs("時計")
                }
            }
            case(2) {
                action {
                    it.clearInput()
                }.expectation {
                    it.textIs("設定を検索")
                }
            }
        }
    }
```

### iOSSendKeys1.kt

(`src/test/kotlin/tutorial/basic/iOSSendKeys1.kt`)

```kotlin
    @Test
    @Order(10)
    fun sendKeys_clearInput() {

        scenario {
            case(1) {
                condition {
                    it.launchApp("[マップ]")
                        .waitForDisplay("マップで検索")
                        .tap()
                }.action {
                    it.sendKeys("safari")
                }.expectation {
                    it.textIs("safari")
                }
            }
            case(2) {
                action {
                    it.clearInput()
                }.expectation {
                    it.textIs("マップで検索")
                }
            }
        }
    }
```

### Link

- [index](../../../../index_ja.md)

