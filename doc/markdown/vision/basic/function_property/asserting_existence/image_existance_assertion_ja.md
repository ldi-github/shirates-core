# 画像が存在することを検証する (Vision)

これらの関数を使用して画像の存在を検証することができます。

## 関数

|   グループ    | 関数                          | 説明                                                                                           | return value                                      |
|:---------:|:----------------------------|:---------------------------------------------------------------------------------------------|:--------------------------------------------------|
|   exist   | existImage                  | 画像が現在の画面に存在することを検証します。<br> `withScroll`関数内で使用するとスクロールが発生します。                                 | VisionElement(存在する場合)<br>TestNGException(存在しない場合) |
|   exist   | existImageWithScrollDown    | 画像が存在することを検証します（下方向スクロールあり）                                                                  | (同上)                                              |
|   exist   | existImageWithScrollUp      | 画像が存在することを検証します（上方向スクロールあり）                                                                  | (同上)                                              |
|   exist   | existImageWithoutScroll     | 画像が存在することを検証します（スクロールなし）                                                                     | (同上)                                              |
| dontExist | dontExistImage              | 画像が現在の画面に存在しないことを検証します。 <br> `withScroll`関数内で使用するとスクロールが発生します。<br>TestNGException(if exists) | 空要素（存在しない場合）<br>TestNGException（存在する場合）　          |
| dontExist | dontExistImageWithoutScroll | 画像が存在しないことを検証します。（スクロールなし）                                                                   | (同上)                                              |

### サンプルコード

[サンプルの入手](../../../getting_samples_ja.md)

### ExistImageDontExistImage1.kt

(`src/test/kotlin/tutorial/basic/ExistImageDontExistImage1.kt`)

```kotlin
    @Test
    @Order(10)
    fun existImage_OK() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android設定トップ画面]")
                }.expectation {
                    it.existImage("[アプリアイコン]")
                }
            }
        }
    }

    @Test
    @Order(20)
    fun withScrollDown_existImage_OK() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android設定トップ画面]")
                }.expectation {
                    withScrollDown {
                        it.existImage("[システムアイコン]")
                    }
                }
            }
        }
    }

    @Test
    @Order(30)
    fun withScrollDown_existImage_NG() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android設定トップ画面]")
                        .existImage("[ネットワークとインターネットアイコン]")
                        .scrollDown()
                }.expectation {
                    withScrollDown {
                        it.existImage("[ネットワークとインターネットアイコン]")
                    }
                }
            }
        }
    }

    @Test
    @Order(40)
    fun withScrollDown_existImageWithoutScroll_NG() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android設定トップ画面]")
                }.expectation {
                    withScrollDown {
                        it.existImageWithoutScroll("[ネットワークとインターネットアイコン]")     // OK
                        it.existImageWithoutScroll("[システムアイコン]")     // NG
                    }
                }
            }
        }
    }

    @Test
    @Order(40)
    fun dontExistImage_OK() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android設定トップ画面]")
                }.expectation {
                    it.dontExistImage("[システムアイコン]")
                }
            }
        }
    }

    @Test
    @Order(40)
    fun withScrollDown_dontExistImage_OK() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android設定トップ画面]")
                }.expectation {
                    withScrollDown {
                        it.dontExistImage("[VPNアイコン]")
                    }
                }
            }
        }
    }

    @Test
    @Order(50)
    fun withScrollDown_dontExistImage_NG() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android設定トップ画面]")
                }.expectation {
                    withScrollDown {
                        it.dontExistImage("[システムアイコン]")
                    }
                }
            }
        }
    }

    @Test
    @Order(60)
    fun withScrollDown_dontExistImageWithoutScroll_NG() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android設定トップ画面]")
                }.expectation {
                    withScrollDown {
                        it.dontExistWithoutScroll("ユーザー補助")    // OK
                        it.dontExistWithoutScroll("通知")    // NG
                    }
                }
            }
        }
    }
```

### Link

- [index](../../../../index_ja.md)


