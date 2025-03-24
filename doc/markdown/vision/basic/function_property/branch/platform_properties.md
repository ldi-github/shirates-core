# Platform property (Vision)

You can get information of the platform using these properties.

## properties

| property        | description            |
|:----------------|:-----------------------|
| platformName    | "android" or "ios"     |
| platformVersion | Major version of os    |
| isAndroid       | true on Android        |
| isiOS           | true on iOS            |
| isVirtualDevice | true on virtual device |
| isRealDevice    | true on real device    |

## Sample code

[Getting samples](../../../getting_samples.md)

### PlatformProperties1.kt

(`src/test/kotlin/tutorial/basic/PlatformProperties1.kt`)

```kotlin
    @Test
    @Order(10)
    fun platformProperties() {

        scenario {
            case(1) {
                expectation {
                    platformName
                        .thisIs("android")

                    platformVersion
                        .thisIs("14")

                    platformMajorVersion
                        .thisIs(14)

                    isAndroid
                        .thisIsTrue()

                    isiOS
                        .thisIsFalse()
                }
            }
        }
    }
```

### Link

- [index](../../../../index.md)

