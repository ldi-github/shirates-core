# waitScreen, waitScreenOf (Vision)

これらの関数を使用して指定した画面が表示されるまで待つことができます。

表示される画面の候補が複数ある場合は **waitScreenOf**関数 を使用します。

引数`waitSeconds`を指定せずに使用した場合はデフォルトで`waitSecondsOnIsScreen`が適用されます。

参照 [パラメーター](../../../common/parameter/parameters_ja.md)

## サンプルコード

[サンプルの入手](../../getting_samples_ja.md)

## waitScreen

### WaitScreen1.kt

(`kotlin/tutorial/basic/WaitScreen1.kt`)

```kotlin
    @Test
    @Order(10)
    fun waitScreen() {

        scenario {
            case(1) {
                condition {
                    it.terminateApp()
                        .launchApp()
                }.action {
                    it.waitScreen("[Android設定トップ画面]")
                }.expectation {
                    it.screenIs("[Android設定トップ画面]")
                }
            }
        }
    }

    @Test
    @Order(20)
    fun waitScreen_ERROR() {

        scenario {
            case(1) {
                condition {
                    it.terminateApp()
                        .launchApp()
                }.action {
                    it.waitScreen("[ネットワークとインターネット画面]")
                }
            }
        }
    }
...

}
```

## waitScreenOf

### WaitScreen1.kt

(`kotlin/tutorial/basic/WaitScreen1.kt`)

```kotlin
    @Test
    @Order(30)
    fun waitScreenOf() {

        scenario {
            case(1) {
                condition {
                    it.terminateApp()
                        .launchApp()
                }.action {
                    it.waitScreenOf(
                        "[Android設定トップ画面]",
                        "[ネットワークとインターネット画面]",
                        "[接続設定画面]"
                    )
                    output("screenName=${it.screenName}")
                }.expectation {
                    it.screenIs("[Android設定トップ画面]")
                }
            }
        }
    }

    @Test
    @Order(40)
    fun waitScreenOf_ERROR() {

        scenario {
            case(1) {
                condition {
                    it.terminateApp()
                        .launchApp("Chrome")
                }.action {
                    it.waitScreenOf(
                        "[Android設定トップ画面]",
                        "[ネットワークとインターネット画面]",
                        "[接続設定画面]"
                    )
                }
            }
        }
    }
```

### Link

- [index](../../../index_ja.md)
