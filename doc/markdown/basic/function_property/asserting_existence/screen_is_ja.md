# 画面が表示されていることの検証

これらの関数を使用して画面が表示されていることを検証することができます。

## 関数

| 関数         | 説明                             |
|:-----------|--------------------------------|
| screenIs   | 指定した画面が表示されていることを検証します         |
| isScreen   | 指定した画面が表示されている場合にtrueを返します     |
| screenIsOf | 指定した画面の何れかが表示されていることを検証します     |
| isScreenOf | 指定した画面の何れかが表示されている場合にtrueを返します |

## 画面ニックネームファイル

これらの関数を使用する前に[画面ニックネームファイル](../../selector_and_nickname/nickname/screen_nickname_ja.md)を定義しておく必要があります。

## 例

### ScreenIsAndIsScreen1.kt

(`kotlin/tutorial/basic/ScreenIsAndIsScreen1.kt`)

```kotlin
@Test
@Order(10)
fun screenIs_OK() {

    scenario {
        case(1) {
            condition {
                it.macro("[Android Settings Top Screen]")
            }.expectation {
                it.screenIs("[Android Settings Top Screen]")
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
                it.macro("[Android Settings Top Screen]")
            }.expectation {
                it.screenIs("[System Screen]")
            }
        }
    }
}

@Test
@Order(30)
fun isScreen_true() {

    scenario {
        case(1) {
            condition {
                it.macro("[Android Settings Top Screen]")
            }.expectation {
                it.isScreen("[Android Settings Top Screen]")
                    .ifTrue {
                        OK("This is [Android Settings Top Screen]")
                    }
            }
        }
    }
}

@Test
@Order(40)
fun isScreen_false() {

    scenario {
        case(1) {
            condition {
                it.macro("[Android Settings Top Screen]")
            }.expectation {
                it.isScreen("[System Screen]")
                    .ifFalse {
                        OK("This is not [System Screen]")
                    }
            }
        }
    }
}
```

## 例

### ScreenIsOfAndIsScreenOf1.kt

(`kotlin/tutorial/basic/ScreenIsOfAndIsScreenOf1.kt`)

```kotlin
@Test
@Order(10)
fun screenIsOf_OK() {

    scenario {
        case(1) {
            condition {
                it.macro("[Android Settings Top Screen]")
            }.expectation {
                it.screenIsOf("[Android Settings Top Screen]")
                    .screenIsOf("[Android Settings Top Screen]", "[Network & internet Screen]", "[System Screen]")
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
                it.macro("[Android Settings Top Screen]")
            }.expectation {
                it.screenIsOf("[Network & internet Screen]", "[System Screen]")
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
                it.macro("[Android Settings Top Screen]")
            }.expectation {
                it.isScreenOf("[Android Settings Top Screen]")
                    .ifTrue {
                        OK("This is [Android Settings Top Screen]")
                    }
                it.isScreenOf("[Android Settings Top Screen]", "[Network & internet Screen]", "[System Screen]")
                    .ifTrue {
                        OK("This is of [Android Settings Top Screen],[Network & internet Screen],[System Screen]")
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
                it.macro("[Android Settings Top Screen]")
            }.expectation {
                it.isScreenOf("[Network & internet Screen]", "[System Screen]")
                    .ifFalse {
                        OK("This is not of [Network & internet Screen],[System Screen]")
                    }
            }
        }
    }
}
```

### Link

- [index](../../../index_ja.md)

