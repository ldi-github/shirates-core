# 画面が表示されていることの検証

これらの関数を使用して画面が表示されていることを検証することができます。

## 関数

| 関数         | 説明                             |
|:-----------|--------------------------------|
| screenIs   | 指定した画面が表示されていることを検証します         |
| isScreen   | 指定した画面が表示されている場合にtrueを返します     |
| screenIsOf | 指定した画面の何れかが表示されていることを検証します     |
| isScreenOf | 指定した画面の何れかが表示されている場合にtrueを返します |

## 画面イメージテンプレート

これらの関数を使用する前に画面イメージテンプレートを準備してください。

![](_images/screen_image_templates_ja.png)

参照: [ScreenClassifierを使用する (Vision)](../../text_and_image_recognition/using_screen_classifier_ja.md)

## サンプルコード

[サンプルの入手](../../../getting_samples_ja.md)

### ScreenIsAndIsScreen1.kt

(`kotlin/tutorial/basic/ScreenIsAndIsScreen1.kt`)

画像による画面判定を行います。

```kotlin
    @Test
    @Order(10)
    fun screenIs_OK() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android設定トップ画面]")
                }.expectation {
                    it.screenIs("[Android設定トップ画面]")
                }
            }
        }
    }

    @Test
    @Order(20)
    fun screenIs_NG() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android設定トップ画面]")
                }.expectation {
                    it.screenIs("[システム画面]")
                }
            }
        }
    }

    @Test
    @Order(30)
    fun isScreen_ifTrue() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android設定トップ画面]")
                }.expectation {
                    it.isScreen("[Android設定トップ画面]")
                        .ifTrue("[Android設定トップ画面]の場合") {
                            OK("これは[Android設定トップ画面]です")
                        }
                }
            }
        }
    }

    @Test
    @Order(40)
    fun isScreen_ifFalse() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android設定トップ画面]")
                }.expectation {
                    it.isScreen("[システム画面]")
                        .ifFalse("[システム画面]ではない場合") {
                            OK("これは[システム画面]ではありません")
                        }
                }
            }
        }
    }
```

### ScreenIsAndIsScreen2.kt

(`src/test/kotlin/tutorial/basic/ScreenIsAndIsScreen2.kt`)

screenIs関数で画面判定後に呼び出される検証ロジック（verifyFunc）を指定できます。<br>

```kotlin
    @Test
    @Order(10)
    fun screenIs_with_verifyFunc_OK() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android設定トップ画面]")
                }.expectation {
                    it.screenIs("[Android設定トップ画面]") {   // OK
                        exist("設定")         // OK（ログは出力されない）
                        exist("設定を検索")    // OK（ログは出力されない）
                    }
                }
            }
        }
    }

    @Test
    @Order(20)
    fun screenIs_with_verifyFunc_NG() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android設定トップ画面]")
                }.expectation {
                    it.screenIs("[Android設定トップ画面]") {   // OK
                        exist("システム")   // NG（ログは出力される）
                    }
                }
            }
        }
    }
```

verifyFuncによる検証結果のOKのログは出力されません。<br>
verifyFuncによる検証結果のNGのログは出力されます。

![](_images/screen_verification_with_verify_func_ja.png)

### ScreenIsOfAndIsScreenOf1.kt

(`kotlin/tutorial/basic/ScreenIsOfAndIsScreenOf1.kt`)

画像による画面判定を行います。複数の画面名の候補を指定することができます。

```kotlin
    @Test
    @Order(10)
    fun screenIsOf_OK() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android設定トップ画面]")
                }.expectation {
                    it.screenIsOf("[Android設定トップ画面]")
                        .screenIsOf("[Android設定トップ画面]", "[ネットワークとインターネット画面]", "[システム画面]")
                }
            }
        }
    }

    @Test
    @Order(20)
    fun screenIsOf_NG() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android設定トップ画面]")
                }.expectation {
                    it.screenIsOf("[ネットワークとインターネット画面]", "[システム画面]")
                }
            }
        }
    }

    @Test
    @Order(30)
    fun isScreenOf_true() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android設定トップ画面]")
                }.expectation {
                    it.isScreenOf("[Android設定トップ画面]")
                        .ifTrue {
                            OK("This is [Android設定トップ画面]")
                        }
                    it.isScreenOf("[Android設定トップ画面]", "[ネットワークとインターネット画面]", "[システム画面]")
                        .ifTrue {
                            OK("これは[Android設定トップ画面],[ネットワークとインターネット画面],[システム画面]のいずれかです")
                        }
                }
            }
        }
    }

    @Test
    @Order(40)
    fun isScreenOf_false() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android設定トップ画面]")
                }.expectation {
                    it.isScreenOf("[ネットワークとインターネット画面]", "[システム画面]")
                        .ifFalse {
                            OK("これは[ネットワークとインターネット画面],[システム画面]のいずれでもありません")
                        }
                }
            }
        }
    }
```

### Link

- [index](../../../../index_ja.md)

