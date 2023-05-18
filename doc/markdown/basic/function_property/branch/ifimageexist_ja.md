# 分岐関数 (ifImageExist, ifImageExistNot)

画像に対してこれらの分岐関数を使用することができます。

## 関数

| 関数              | 説明                             |
|:----------------|:-------------------------------|
| ifImageExist    | 画像が画面上に存在する場合にコードブロックが実行されます。  |
| ifImageExistNot | 画像が画面上に存在しない場合にコードブロックが実行されます。 |

### IfImageExist1.kt

(`kotlin/tutorial/basic/IfImageExist1.kt`)

```kotlin
@Test
@Order(0)
fun setupImage() {

    TestSetupHelper.setupImageAndroidSettingsTopScreen()
}

@Test
@Order(10)
fun ifImageExistTest() {

    ImageFileRepository.setup(screenDirectory = TestLog.testResults.resolve("images"))

    scenario {
        case(1) {
            condition {
                it.macro("[Android Settings Top Screen]")
            }.expectation {
                ifImageExist("[Network & internet Icon].png") {
                    OK("ifImageExist called")
                }.ifElse {
                    NG()
                }

                ifImageExistNot("[System Icon].png") {
                    OK("ifImageExistNot called")
                }.ifElse {
                    NG()
                }
            }
        }
        case(2) {
            action {
                it.scrollToBottom()
            }.expectation {
                ifImageExist("[Network & internet Icon].png") {
                    NG()
                }.ifElse {
                    OK("ifElse called")
                }

                ifImageExistNot("[System Icon].png") {
                    NG()
                }.ifElse {
                    OK("ifElse called")
                }
            }
        }
    }
}
```

### Link

- [index](../../../index_ja.md)

