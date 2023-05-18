# 文字列の検証

これらの関数を使用して文字列値を検証することができます。

## 関数

| 関数                | 説明                      |
|:------------------|-------------------------|
| thisIs            | 期待値であることを検証します          |
| thisIsNot         | 期待値ではないことを検証します         |
| thisIsEmpty       | 空文字であることを検証します          |
| thisIsNotEmpty    | 空文字ではないことを検証します         |
| thisIsBlank       | 空白文字であることを検証します         |
| thisIsNotBlank    | 空白文字ではないことを検証します        |
| thisStartsWith    | 指定した文字列から開始することを検証します   |
| thisStartsWithNot | 指定した文字列から開始しないことを検証します  |
| thisContains      | 指定した文字列を含むことを検証します      |
| thisContainsNot   | 指定した文字列を含まないことを検証します    |
| thisEndsWith      | 指定した文字列で終了することを検証します    |
| thisEndsWithNot   | 指定した文字列で終了しないことを検証します   |
| thisMatches       | 指定したパターンにマッチすることを検証します  |
| thisMatchesNot    | 指定したパターンにマッチしないことを検証します |

## 例

### AssertingAnyValue1.kt

(`kotlin/tutorial/basic/AssertingAnyValue1.kt`)

```kotlin
@Test
@Order(10)
fun stringAssertion_OK() {

    scenario {
        case(1) {
            condition {
                it.macro("[Android Settings Top Screen]")
            }.expectation {
                "string1"
                    .thisIs("string1")
                    .thisIsNot("string2")

                    .thisStartsWith("s")
                    .thisStartsWithNot("t")

                    .thisContains("ring")
                    .thisContainsNot("square")

                    .thisEndsWith("ring1")
                    .thisEndsWithNot("ring2")

                    .thisMatches("^str.*")
                    .thisMatchesNot("^tex.*")
            }
        }

        case(2) {
            expectation {
                "".thisIsEmpty()
                "hoge".thisIsNotEmpty()

                " ".thisIsBlank()
                "hoge".thisIsNotBlank()
            }
        }

    }
}

@Test
@Order(20)
fun stringAssertion_NG() {

    scenario {
        case(1) {
            condition {
                it.macro("[Android Settings Top Screen]")
            }.expectation {
                "string1"
                    .thisContains("square")
            }
        }
    }
}

@Test
@Order(30)
fun booleanAssertion_OK() {

    scenario {
        case(1) {
            condition {
                it.macro("[Android Settings Top Screen]")
            }.expectation {
                true.thisIsTrue()
                false.thisIsFalse()

                true.thisIsTrue("The value is true")
                false.thisIsFalse("The value is false")
            }
        }
        case(2) {
            expectation {
                it.isApp("Settings")
                    .thisIsTrue("This app is <Settings>")
                it.isApp("Chrome")
                    .thisIsFalse("This app is not <Chrome>")
            }
        }
    }
}

@Test
@Order(40)
fun booleanAssertion_NG() {

    scenario {
        case(1) {
            condition {
                it.macro("[Android Settings Top Screen]")
            }.expectation {
                false.thisIsTrue()
            }
        }
    }
}
```

### Link

- [index](../../../index_ja.md)

