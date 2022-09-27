# Press keys

You can press key using these functions.

## functions (Android)

| function    |
|:------------|
| pressBack   |
| pressHome   |
| pressSearch |
| pressTab    |

## functions (iOS)

| function     |
|:-------------|
| pressBack    |
| pressHome    |
| pressEnter   |

## pressBack (Android)

#### AndroidPressKey1.kt

(`kotlin/tutorial/basic/AndroidPressKey1.kt`)

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
```

## pressHome (Android)

#### AndroidPressKey1.kt

(`kotlin/tutorial/basic/AndroidPressKey1.kt`)

```kotlin
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
                it.screenIs("[Pixel Home Screen]")
            }
        }

    }
}
```

## pressSearch (Android)

#### AndroidPressKey1.kt

(`kotlin/tutorial/basic/AndroidPressKey1.kt`)

```kotlin
@Test
@Order(30)
fun pressSearch() {

    scenario {
        case(1) {
            condition {
                it.macro("[Android Settings Search Screen]")
                    .sendKeys("clock")
            }.action {
                it.pressSearch()
            }.expectation {
                it.exist("Open Clock app")
            }
        }
    }

}
```

## pressBack (iOS)

#### iOSPressKey1.kt

(`kotlin/tutorial/basic/iOSPressKey1.kt`)

```kotlin
@Test
@Order(10)
fun pressBack() {

    scenario {
        case(1) {
            condition {
                it.macro("[iOS Search Screen]")
                it.select("[SpotlightSearchField]")
                    .clearInput()
                    .sendKeys("safa")
                    .tap("Safari")
                    .wait()
                    .appIs("[Safari]")
            }.action {
                it.pressBack()
            }.expectation {
                it.screenIs("[iOS Search Screen]")
            }
        }
    }
}
```

## pressHome (iOS)

#### iOSPressKey1.kt

(`kotlin/tutorial/basic/iOSPressKey1.kt`)

```kotlin
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
                it.exist(".XCUIElementTypePageIndicator")
                    .exist("#Safari")
            }
        }
    }
}
```

## pressEnter (iOS)

#### iOSPressKey1.kt

(`kotlin/tutorial/basic/iOSPressKey1.kt`)

```kotlin
@Test
@Order(30)
fun pressEnter() {

    scenario {
        case(1) {
            condition {
                it.pressHome()
                    .swipeCenterToBottom()
                    .tap("#SpotlightSearchField")
                    .clearInput()
                    .sendKeys("safari")
            }.action {
                it.pressEnter()
            }.expectation {
                it.appIs("[Safari]")
            }
        }
    }
}
```

### Link

- [index](../../../index.md)
