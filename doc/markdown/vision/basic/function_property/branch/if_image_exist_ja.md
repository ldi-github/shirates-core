# 分岐関数 (ifImageExist, ifImageExistNot) (Vision)

画像に対してこれらの分岐関数を使用することができます。

## 関数

| 関数              | 説明                                        |
|:----------------|:------------------------------------------|
| ifImageExist    | 指定したラベルに対応する画像が画面上に存在する場合にコードブロックが実行されます  |
| ifImageExistNot | 指定したラベルに対応する画像が画面上に存在しない場合にコードブロックが実行されます |

### サンプルコード

[サンプルの入手](../../../getting_samples_ja.md)

### IfImageExist1.kt

(`src/test/kotlin/tutorial/basic/IfImageExist1.kt`)

```kotlin
    @Test
    @Order(10)
    fun ifImageExistTest() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    ifImageExist("[Network & internet Icon]") {
                        OK("ifImageExist called")
                    }.ifElse {
                        NG()
                    }

                    ifImageExistNot("[System Icon]") {
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
                    ifImageExist("[Network & internet Icon]") {
                        NG()
                    }.ifElse {
                        OK("ifElse called")
                    }

                    ifImageExistNot("[System Icon]") {
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

- [index](../../../../index_ja.md)

