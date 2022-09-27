# tapAppIcon

You can tap app icon and launch app using **tapAppIcon** function.

## function

| function   | description                                |
|:-----------|:-------------------------------------------|
| tapAppIcon | Tap app icon on home or app launcher menu. |

## Example

### TapAppIcon1.kt

(`kotlin/tutorial/basic/TapAppIcon1.kt`)

```kotlin
@Test
fun tapAppIcon() {

    scenario {
        case(1) {
            action {
                it.tapAppIcon("Chrome")
            }.expectation {
                it.appIs("[Chrome]")
            }
        }
        case(2) {
            action {
                it.tapAppIcon("Play Store")
            }.expectation {
                it.appIs("[Play Store]")
            }
        }
    }
}
```

### Link

- [index](../../../index.md)
