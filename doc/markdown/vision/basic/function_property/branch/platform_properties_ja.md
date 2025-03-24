# プラットフォームプロパティ (Vision)

これらのプロパティを使用してプラットフォームの情報を取得することができます。

## プロパティ

| プロパティ           | 説明                  |
|:----------------|:--------------------|
| platformName    | "android" または "ios" |
| platformVersion | OSのメジャーバージョン        |
| isAndroid       | Androidの場合にtrue     |
| isiOS           | iOSの場合にtrue         |
| isVirtualDevice | 仮想デバイスの場合にtrue      |
| isRealDevice    | 実デバイスの場合にtrue       |

## サンプルコード

[サンプルの入手](../../../getting_samples_ja.md)

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

- [index](../../../../index_ja.md)

