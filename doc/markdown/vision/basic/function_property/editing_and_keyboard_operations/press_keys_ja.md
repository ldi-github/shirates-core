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

### サンプルコード

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
                    it.macro("[Network & internet Screen]")
                }.action {
                    it.pressBack()
                }.expectation {
                    it.screenIs("[Android Settings Top Screen]")
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
                    it.macro("[Network & internet Screen]")
                }.action {
                    it.pressHome()
                }.expectation {
                    it.screenIs("[Android Home Screen]")
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
                    it.macro("[Files Top Screen]")
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
                    it.appIs("[Settings]")
                        .launchApp("Maps")
                        .appIs("[Maps]")
                }.action {
                    it.pressBack()
                }.expectation {
                    it.appIs("[Settings]")
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
                    it.macro("[iOS Settings Top Screen]")
                }.action {
                    it.pressHome()
                }.expectation {
                    it.screenIs("[iOS Home Screen]")
                }
            }
        }
    }
```

### Link

- [index](../../../../index_ja.md)
