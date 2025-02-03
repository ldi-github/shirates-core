# launchApp, terminateApp (Vision)

You can launch or terminate app using these functions.

## Functions

| function     | description                                                           |
|:-------------|:----------------------------------------------------------------------|
| launchApp    | Launch app. Accepts appName or `packageOrBundleId` or activityName    |
| terminateApp | Terminate app. Accepts appName or `packageOrBundleId` or activityName |

## Sample code

[Getting samples](../../getting_samples.md)

### LaunchApp1.kt

(`src/test/kotlin/tutorial/basic/LaunchApp1.kt`)

```kotlin
    @Test
    fun launchApp() {

        scenario {
            case(1) {
                condition {
                    it.terminateApp()   // Refers packageOrBundleId in testConfig.json
                }.action {
                    it.launchApp()  // Refers packageOrBundleId in testConfig.json
                }.expectation {
                    it.appIs("[Settings]")  // App Nickname in app.json
                    it.appIs("Settings")    // App Nickname in app.json
                    it.appIs("com.android.settings")    // package
                }
            }
            case(2) {
                condition {
                    it.terminateApp("[Chrome]")     // App Nickname in app.json
                }.action {
                    it.launchApp("[Chrome]")
                }.expectation {
                    it.appIs("[Chrome]")
                }
            }
            case(3) {
                condition {
                    it.terminateApp("com.android.chrome")   // package
                }.action {
                    it.launchApp("com.android.chrome")
                }.expectation {
                    it.appIs("com.android.chrome")
                }
            }
            case(4) {
                condition {
                    it.terminateApp("Chrome")   // App Nickname in app.json
                }.action {
                    it.launchApp("Chrome")
                }.expectation {
                    it.appIs("Chrome")
                }
            }
            case(5) {
                condition {
                    it.terminateApp("[Play Store]")     // App Nickname in app.json
                }.action {
                    it.launchApp("[Play Store]")
                }.expectation {
                    it.appIs("[Play Store]")
                }
            }
        }
    }
```

### Link

- [index](../../../../index.md)
