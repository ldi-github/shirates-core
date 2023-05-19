# sendKeys

**sendKeys**関数を使用するとinputウィジェットにテキストを入力することができます。

## 例

### AndroidSendKeys1.kt

(`kotlin/tutorial/basic/AndroidSendKeys1.kt`)

```kotlin
@Test
@Order(10)
fun sendKeys() {

    scenario {
        case(1) {
            condition {
                it.macro("[Android Settings Search Screen]")
            }.action {
                it.sendKeys("clock")
            }.expectation {
                it.textIs("clock")
            }
        }
    }
}
```

### iOSSendKeys1.kt

(`kotlin/tutorial/basic/iOSSendKeys1.kt`)

```kotlin
@Test
@Order(10)
fun sendKeys() {

    scenario {
        case(1) {
            condition {
                it.pressHome()
                    .swipeCenterToBottom()
                    .tap("[SpotlightSearchField]")
                    .clearInput()
            }.action {
                it.sendKeys("safari")
            }.expectation {
                it.textIs("safari")
            }
        }
    }
}
```

### Link

- [index](../../../index_ja.md)

