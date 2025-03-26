# scroll (Vision)

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
| withScrollDown    | select element with scrolling down      |
| withScrollUp      | select element with scrolling up        |
| withScrollRight   | select element with scrolling right     |
| withScrollLeft    | select element with scrolling left      |
| withoutScroll     | select element without scrolling        |

## Sample code

[Getting samples](../../../getting_samples.md)

### Scroll1.kt

(`kotlin/tutorial/basic/Scroll1.kt`)

```kotlin
    @Test
    @Order(10)
    fun scrollDown_scrollUp() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    it
                        .scrollDown()
                        .scrollDown()
                        .scrollUp()
                        .scrollUp()
                }
            }
            case(2) {
                action {
                    it
                        .scrollDown(scrollDurationSeconds = 5.0, startMarginRatio = 0.1)
                        .scrollDown(scrollDurationSeconds = 3.0, startMarginRatio = 0.3)
                        .scrollUp(scrollDurationSeconds = 5.0, startMarginRatio = 0.1)
                        .scrollUp(scrollDurationSeconds = 3.0, startMarginRatio = 0.3)
                }
            }
        }
    }

    @Test
    @Order(20)
    fun scrollToBottom_scrollToTop() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    it.scrollToBottom(repeat = 2)
                }.expectation {
                    it.exist("Tips & support")
                }
            }
            case(2) {
                action {
                    it.scrollToTop(repeat = 2)
                }.expectation {
                    it.exist("Settings")
                }
            }
        }
    }

    @Test
    @Order(30)
    fun withScroll() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    withScrollDown {
                        it
                            .detect("Notifications").textIs("Notifications")
                            .detect("Accessibility").textIs("Accessibility")
                            .detect("Tips & support").textIs("Tips & support")
                    }
                    withScrollUp {
                        it
                            .detect("Accessibility").textIs("Accessibility")
                            .detect("Notifications").textIs("Notifications")
                    }
                }
            }
            case(2) {
                expectation {
                    withScrollDown {
                        it
                            .exist("Notifications")
                            .exist("Accessibility")
                            .exist("Tips & support")
                    }
                    withScrollUp {
                        it
                            .exist("Tips & support")
                            .exist("Accessibility")
                            .exist("Notifications")
                    }
                }
            }
            case(3) {
                action {
                    withScrollDown {
                        it.tap("Accessibility")
                    }
                }.expectation {
                    it.screenIs("[Accessibility Screen]")
                }
            }
            case(4) {
                action {
                    it.pressBack()
                    withScrollUp {
                        it.tap("Network & internet")
                    }
                }.expectation {
                    it.screenIs("[Network & internet Screen]")
                }
            }
        }
    }
```

### Link

- [index](../../../../index.md)

