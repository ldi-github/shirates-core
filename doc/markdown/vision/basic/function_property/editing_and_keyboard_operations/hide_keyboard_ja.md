# hideKeyboard (Vision)

**hideKeyboard**関数を使用するとキーボードを非表示にすることができます。

### サンプルコード

[サンプルの入手](../../../getting_samples_ja.md)

### AndroidKeyboard1.kt

(`src/test/kotlin/tutorial/basic/AndroidKeyboard1.kt`)

```kotlin
    @Test
    @Order(10)
    fun hideKeyboard() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android設定トップ画面]")
                        .isKeyboardShown.thisIsFalse("キーボードが表示されています")
                }.action {
                    it.tap("設定を検索")
                }.expectation {
                    it.keyboardIsShown()
                }
            }
            case(2) {
                action {
                    it.hideKeyboard()
                }.expectation {
                    it.keyboardIsNotShown()
                }
            }
        }
    }
```

####                           

### iOSKeyboard1.kt

(`src/test/kotlin/tutorial/basic/iOSKeyboard1.kt`)

```kotlin
    @Test
    @Order(10)
    fun hideKeyboard() {

        scenario {
            case(1) {
                condition {
                    it.pressHome()
                        .isKeyboardShown.thisIsFalse("キーボードは表示されていません")
                }.action {
                    it.swipeCenterToBottom()
                }.expectation {
                    it.keyboardIsShown()
                }
            }
            case(2) {
                action {
                    it.hideKeyboard()
                }.expectation {
                    it.keyboardIsNotShown()
                }
            }
        }
    }
```

####                

### Link

- [index](../../../../index_ja.md)
