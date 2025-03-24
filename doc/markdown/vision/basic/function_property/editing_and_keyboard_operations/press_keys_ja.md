# Press keys

これらの関数を使用するとキーを押下することができます。

## 関数 (Android)

| 関数          |
|:------------|
| pressBack   |
| pressHome   |
| pressSearch |
| pressTab    |

## 関数 (iOS)

| 関数         |
|:-----------|
| pressBack  |
| pressHome  |
| pressEnter |

## サンプルコード

[サンプルの入手](../../../getting_samples_ja.md)

## pressBack (Android)

### AndroidPressKey1.kt

(`src/test/kotlin/tutorial/basic/AndroidPressKey1.kt`)

```kotlin
    @Test
    @Order(10)
    fun pressBack() {

        scenario {
            case(1) {
                condition {
                    it.macro("[ネットワークとインターネット画面]")
                }.action {
                    it.pressBack()
                }.expectation {
                    it.screenIs("[Android設定トップ画面]")
                }
            }

        }
    }

    @Test
    @Order(20)
    fun pressHome() {

        scenario {
            case(1) {
                condition {
                    it.macro("[ネットワークとインターネット画面]")
                }.action {
                    it.pressHome()
                }.expectation {
                    it.screenIs("[Androidホーム画面]")
                }
            }

        }
    }

    @Test
    @Order(30)
    fun pressTab() {

        scenario {
            case(1) {
                condition {
                    it.macro("[ファイルトップ画面]")
                }.action {
                    it.pressTab()
                        .pressTab()
                        .pressTab()
                        .pressTab()
                        .pressTab()
                        .pressTab()
                        .pressTab()
                        .pressTab()
                }
            }
        }

    }
```

### iOSPressKey1.kt

(`src/test/kotlin/tutorial/basic/iOSPressKey1.kt`)

```kotlin
    @Test
    @Order(10)
    fun pressBack() {

        scenario {
            case(1) {
                condition {
                    it.appIs("[設定]")
                        .launchApp("[マップ]")
                        .appIs("[マップ]")
                }.action {
                    it.pressBack()
                }.expectation {
                    it.appIs("[設定]")
                }
            }
        }
    }

    @Test
    @Order(20)
    fun pressHome() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS設定トップ画面]")
                }.action {
                    it.pressHome()
                }.expectation {
                    it.screenIs("[iOSホーム画面]")
                }
            }
        }
    }
```

### Link

- [index](../../../../index_ja.md)
