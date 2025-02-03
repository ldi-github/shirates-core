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
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.exist("Network & internet")
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
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.exist("System")
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
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.existWithScrollDown("System")
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
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.existWithScrollDown("Network business")
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
                    it.macro("[Android Settings Top Screen]")
                        .flickAndGoDown()
                }.expectation {
                    it.existWithScrollUp("Network & internet")
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
                    it.macro("[Android Settings Top Screen]")
                        .flickAndGoDown()
                }.expectation {
                    it.existWithScrollUp("Network business")
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
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.dontExist("System")
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
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.dontExist("Network & internet")
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
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    withScrollDown {
                        it.dontExist("Network business")
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
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    withScrollDown {
                        it.dontExist("System")
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
                    it.macro("[Android Settings Top Screen]")
                        .flickAndGoDown()
                }.expectation {
                    withScrollUp {
                        it.dontExist("Network business")
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
                    it.macro("[Android Settings Top Screen]")
                        .flickAndGoDown()
                }.expectation {
                    withScrollUp {
                        it.dontExist("System")
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
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    withScrollDown {
                        it.existWithoutScroll("Network & internet")    // OK
                        it.existWithoutScroll("System")    // NG
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
                    it.macro("[Android Settings Top Screen]")
                        .flickAndGoDown()
                }.expectation {
                    withScrollUp {
                        it.dontExistWithoutScroll("Display")    // OK
                        it.dontExistWithoutScroll("System")    // NG
                    }
                }
            }
        }
    }
```

### Link

- [index](../../../../index_ja.md)


