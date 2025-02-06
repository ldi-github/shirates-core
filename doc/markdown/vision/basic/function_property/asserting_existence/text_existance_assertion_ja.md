# テキストが存在することの検証

これらの関数を使用してテキストが存在することを確認できます。

## 関数

|   グループ    | 関数                     | 説明                                        | 返却値                                             |
|:---------:|:-----------------------|:------------------------------------------|:------------------------------------------------|
|   exist   | exist                  | 要素が存在することを検証します（`withScroll`使用時はスクロールあり）  | TestElement(存在する場合)<br>TestNGException(存在しない場合) |
|   exist   | existWithScrollDown    | 要素が存在することを検証します（下方向スクロールあり）               | (同上)                                            |
|   exist   | existWithScrollUp      | 要素が存在することを検証します（上方向スクロールあり）               | (同上)                                            |
|   exist   | existWithoutScroll     | 要素が存在することを検証します（スクロールなし）                  | (同上)                                            |
| dontExist | dontExist              | 要素が存在しないことを検証します（表示中の画面）                  | 空要素(存在しない場合)<br>TestNGException(存在する場合)         |
| dontExist | dontExistWithoutScroll | 要素が存在しないことを検証します（`withScroll`使用時はスクロールあり） | (同上)                                            |

### サンプルコード

[サンプルの入手](../../../getting_samples_ja.md)

### ExistDontExist1.kt

(`src/test/kotlin/tutorial/basic/ExistDontExist1.kt`)

```kotlin
    @Test
    @Order(10)
    fun exist_OK() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android設定トップ画面]")
                }.expectation {
                    it.exist("ネットワークとインターネット")
                }
            }
        }
    }

    @Test
    @Order(20)
    fun exist_NG() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android設定トップ画面]")
                }.expectation {
                    it.exist("システム")
                }
            }
        }
    }

    @Test
    @Order(30)
    fun existWithScrollDown_OK() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android設定トップ画面]")
                }.expectation {
                    it.existWithScrollDown("システム")
                }
            }
        }
    }

    @Test
    @Order(40)
    fun existWithScrollDown_NG() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android設定トップ画面]")
                }.expectation {
                    it.existWithScrollDown("ネットワークビジネス")
                }
            }
        }
    }

    @Test
    @Order(50)
    fun existWithScrollUp_OK() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android設定トップ画面]")
                        .flickAndGoDown()
                }.expectation {
                    it.existWithScrollUp("ネットワークとインターネット")
                }
            }
        }
    }

    @Test
    @Order(60)
    fun existWithScrollUp_NG() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android設定トップ画面]")
                        .flickAndGoDown()
                }.expectation {
                    it.existWithScrollUp("ネットワークビジネス")
                }
            }
        }
    }

    @Test
    @Order(70)
    fun dontExist_OK() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android設定トップ画面]")
                }.expectation {
                    it.dontExist("システム")
                }
            }
        }
    }

    @Test
    @Order(80)
    fun dontExist_NG() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android設定トップ画面]")
                }.expectation {
                    it.dontExist("ネットワークとインターネット")
                }
            }
        }
    }

    @Test
    @Order(90)
    fun withScrollDown_dontExist_OK() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android設定トップ画面]")
                }.expectation {
                    withScrollDown {
                        it.dontExist("ネットワークビジネス")
                    }
                }
            }
        }
    }

    @Test
    @Order(100)
    fun withScrollDown_dontExist_NG() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android設定トップ画面]")
                }.expectation {
                    withScrollDown {
                        it.dontExist("システム")
                    }
                }
            }
        }
    }

    @Test
    @Order(110)
    fun dontExistWithScrollUp_OK() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android設定トップ画面]")
                        .flickAndGoDown()
                }.expectation {
                    withScrollUp {
                        it.dontExist("ネットワークビジネス")
                    }
                }
            }
        }
    }

    @Test
    @Order(120)
    fun dontExistWithScrollUp_NG() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android設定トップ画面]")
                        .flickAndGoDown()
                }.expectation {
                    withScrollUp {
                        it.dontExist("システム")
                    }
                }
            }
        }
    }

    @Test
    @Order(130)
    fun withScrollDown_existWithoutScroll_NG() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android設定トップ画面]")
                }.expectation {
                    withScrollDown {
                        it.existWithoutScroll("ネットワークとインターネット")    // OK
                        it.existWithoutScroll("システム")    // NG
                    }
                }
            }
        }
    }

    @Test
    @Order(140)
    fun withScrollUp_dontExistWithoutScroll_NG() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android設定トップ画面]")
                        .flickAndGoDown()
                }.expectation {
                    withScrollUp {
                        it.dontExistWithoutScroll("ディスプレイ")    // OK
                        it.dontExistWithoutScroll("システム")    // NG
                    }
                }
            }
        }
    }
```

### Link

- [index](../../../../index_ja.md)


