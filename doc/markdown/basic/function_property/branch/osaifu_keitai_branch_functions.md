# OsaifuKeitai function

You can branch whether [Osaifu-Keitai](https://en.wikipedia.org/wiki/Osaifu-Keitai) is available or not
using these
functions.

## functions

| function        | description                                                  |
|:----------------|:-------------------------------------------------------------|
| osaifuKeitai    | This function is executed on Osaifu-Keitai is available.     |
| osaifuKeitaiNot | This function is executed on Osaifu-Keitai is not available. |

## Example

### OsaifuKeitai1.kt

```kotlin
@Test
@Order(10)
fun osaifuKeitai1() {

    scenario {
        case(1) {
            action {
                osaifuKeitai {
                    describe("Osaifu-Keitai is available")
                }
                osaifuKeitaiNot {
                    describe("Osaifu-Keitai is not available")
                }
            }
        }
    }
}
```

### Link

- [index](../../../index.md)
