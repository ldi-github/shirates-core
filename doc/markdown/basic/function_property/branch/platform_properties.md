# Platform property

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

### PlatformProperties1.kt

(`kotlin/tutorial/basic/PlatformProperties1.kt`)

```kotlin
@Test
@Order(10)
fun platformProperties() {

    scenario {
        case(1) {
            expectation {
                platformName
                    .stringIs("android")

                platformVersion
                    .stringIs("12")

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

- [index](../../../index.md)

