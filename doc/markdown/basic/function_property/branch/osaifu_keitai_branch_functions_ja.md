# おサイフケータイ関数

[おサイフケータイ](https://en.wikipedia.org/wiki/Osaifu-Keitai)に関してこれらの分岐関数を使用することができます。

## 関数

| 関数              | 説明                                 |
|:----------------|:-----------------------------------|
| osaifuKeitai    | おサイフケータイが利用可能な場合にコードブロックが実行されます    |
| osaifuKeitaiNot | おサイフケータイが利用可能ではない場合にコードブロックが実行されます |

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

- [index](../../../index_ja.md)
