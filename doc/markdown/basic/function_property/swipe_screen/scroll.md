# scroll

You can scroll screen using these functions.

## scroll functions

| function          | description                             |
|:------------------|:----------------------------------------|
| scrollDown        | scroll down                             |
| scrollUp          | scroll up                               |
| scrollRight       | scroll right                            |
| scrollLeft        | scroll left                             |
| scrollToBottom    | scroll to the bottom of the scroll area |
| scrollToTop       | scroll to top of the scroll area        |
| scrollToRightEdge | scroll to right edge of the scroll area |
| scrollToLeftEdge  | scroll to left edge of the scroll area  |

## Example

### Scroll1.kt

```kotlin
@Test
@Order(10)
fun scrollWithoutArgs() {

    scenario {
        case(1) {
            condition {
                it.tapWithScrollDown("Accessibility")
                    .screenIs("[Accessibility Screen]")
            }.action {
                it
                    .scrollDown()
                    .scrollDown()
                    .scrollUp()
                    .scrollUp()
                    .scrollToBottom()
                    .scrollToTop()
            }
        }
    }
}

@Test
@Order(20)
fun scrollWithArgs() {

    scenario {
        case(1) {
            action {
                it
                    .scrollDown(durationSeconds = 5.0, startMarginRatio = 0.1)
                    .scrollDown(durationSeconds = 3.0, startMarginRatio = 0.3)
                    .scrollUp(durationSeconds = 5.0, startMarginRatio = 0.1)
                    .scrollUp(durationSeconds = 3.0, startMarginRatio = 0.3)
                    .scrollToBottom(startMarginRatio = 0.5, repeat = 2)
                    .scrollToTop(startMarginRatio = 0.5, repeat = 2)
            }
        }
    }
}

@Test
@Order(30)
fun scrollToBottom() {

    scenario {
        case(1) {
            condition {
                it.macro("[Android Settings Top Screen]")
            }.action {
                it.scrollToBottom()
            }.expectation {
                it.exist("[Tips & support]")
            }
        }
        case(2) {
            action {
                it.scrollToTop()
            }.expectation {
                it.exist("Settings")
            }
        }
    }
}
```

### Link

- [index](../../../index.md)

