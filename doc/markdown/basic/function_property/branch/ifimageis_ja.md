# 分岐関数 (ifImageIs, ifImageIsNot)

画像に対してこれらの分岐関数を使用することができます。

## 関数

| 関数           | 説明                                   |
|:-------------|:-------------------------------------|
| ifImageIs    | 指定した画像に要素の画像がマッチする場合にコードブロックが実行されます  |
| ifImageIsNot | 指定した画像に要素の画像がマッチしない場合にコードブロックが実行されます |

### IfImageExist1.kt

(`kotlin/tutorial/basic/IfImageIs1.kt`)

```kotlin
@Test
@Order(0)
fun setupImage() {

    TestSetupHelper.setupImageAndroidSettingsTopScreen()
}

@Test
@Order(10)
fun ifImageIsTest() {

    ImageFileRepository.setup(screenDirectory = TestLog.testResults.resolve("images"))

    scenario {
        case(1) {
            condition {
                it.macro("[Android Settings Top Screen]")
            }.expectation {
                it.select("[Network & internet Icon]")
                    .ifImageIs("[Network & internet Icon].png") {
                        OK("ifImageIs called")
                    }.ifElse {
                        NG()
                    }
                it.select("[Network & internet Icon]")
                    .ifImageIsNot("[Network & internet Icon].png") {
                        NG()
                    }.ifElse {
                        OK("ifElse called")
                    }
            }
        }
        case(2) {
            expectation {
                it.select("[Network & internet Icon]")
                    .ifImageIs("[App Icon].png") {
                        NG()
                    }.ifElse {
                        OK("ifElse called")
                    }
                it.select("[Network & internet Icon]")
                    .ifImageIsNot("[App Icon].png") {
                        OK("ifImageIsNot called")
                    }.ifElse {
                        NG()
                    }
            }
        }
    }
}
```

### Link

- [index](../../../index_ja.md)

