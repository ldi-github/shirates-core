# tapAppIcon (Vision)

You can tap app icon and launch app using **tapAppIcon** function.

## function

| function   | description                                |
|:-----------|:-------------------------------------------|
| tapAppIcon | Tap app icon on home or app launcher menu. |

## Sample code

[Getting samples](../../getting_samples.md)

### TapAppIcon1.kt

(`src/test/kotlin/tutorial/basic/TapAppIcon1.kt`)

```kotlin
    @Test
    fun tapAppIcon() {

        scenario {
            case(1) {
                action {
                    it.launchApp("Chrome")
                }.expectation {
                    it.appIs("[Chrome]")
                }
            }
            case(2) {
                action {
                    it.launchApp("Play Store")
                }.expectation {
                    it.appIs("[Play Store]")
                }
            }
        }
    }
```

### Link

- [index](../../../../index.md)
