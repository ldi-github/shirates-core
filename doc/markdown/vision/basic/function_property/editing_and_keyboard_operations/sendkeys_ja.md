# sendKeys (Vision)

**sendKeys**関数を使用するとinputウィジェットにテキストを入力することができます。

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
                    it.pressHome()
                        .swipeCenterToBottom()
                        .tap("検索")
                        .clearInput()
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
                    it.textIs("検索")
                }
            }
        }
    }
```

### Link

- [index](../../../../index_ja.md)

