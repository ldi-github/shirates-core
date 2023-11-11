# clearInput

**clearInput**関数を使用するとinputウィジェットの値をクリアすることができます。

## 例

### AndroidSendKeys1.kt

(`kotlin/tutorial/basic/AndroidSendKeys1.kt`)

```kotlin
    @Test
    @Order(20)
    fun clearInput() {

        scenario {
            case(1) {
                condition {
                    it.restartApp()
                        .macro("[Android Settings Search Screen]")
                        .select("[Search Box]")
                        .textIs("Search settings")
                        .sendKeys("clock")
                        .textIs("clock")
                }.action {
                    it.clearInput()
                }.expectation {
                    it.select("[Search Box]")
                        .textIs("Search settings")
                }
            }
        }
    }
```

### Link

- [index](../../../index_ja.md)

