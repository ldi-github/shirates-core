# knownIssue

You can describe known issue using **knownIssue** function.

## Example

### KnownIssues1.kt

```kotlin
@Test
@Order(10)
fun knownIssue() {

    scenario {
        case(1) {
            condition {
                it.macro("[Android Settings Top Screen]")
            }.expectation {
                it.manual("Animation should be displayed on start up.")
                    .knownIssue(
                        message = "Animation is not executed smoothly.",
                        ticketUrl = "https://issues.example.com/TICKET-1234"
                    )
            }
        }
    }

}
```

**message** and **ticketUrl** is required. Set `ticketUrl = ""` if you don't need it.

### Link

- [index](../../../index.md)
