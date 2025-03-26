# Shell (Vision)

You can execute shell command using these functions.

## functions

| function   | description                  |
|:-----------|:-----------------------------|
| shell      | Execute shell                |
| shellAsync | Execute shell asynchronously |

## Sample code

[Getting samples](../../../getting_samples.md)

### Shell1.kt

(`src/test/kotlin/tutorial/basic/Shell1.kt`)

```kotlin
    @Test
    @Order(10)
    fun shell() {

        scenario {
            case(1) {
                action {
                    if (isRunningOnWindows) {
                        s1 = it.shell("ping", "localhost").resultString
                    } else {
                        s1 = it.shell("ping", "localhost", "-c", "3").resultString
                    }
                }.expectation {
                    s1.thisContains("ping")
                }
            }
        }
    }

    @Test
    @Order(20)
    fun shellAsync() {

        var shellResult: ShellUtility.ShellResult? = null

        scenario {
            case(1) {
                action {
                    if (isRunningOnWindows) {
                        shellResult = it.shellAsync("ping", "localhost")
                    } else {
                        shellResult = it.shellAsync("ping", "localhost", "-c", "3")
                    }
                }.expectation {
                    shellResult!!.hasCompleted.thisIsFalse("hasCompleted=false")
                }
            }
            case(2) {
                expectation {
                    // resultString calls waitFor() in it
                    shellResult!!.resultString.thisStartsWith("PING localhost (127.0.0.1)")
                    shellResult!!.hasCompleted.thisIsTrue("hasCompleted=true")
                }
            }
        }
    }
```

### Note

Using shell is platform dependent.
It's recommended to use [macro function](../../routine_work/macro.md) to hide platform dependent implementation.

### Link

- [index](../../../../index.md)
