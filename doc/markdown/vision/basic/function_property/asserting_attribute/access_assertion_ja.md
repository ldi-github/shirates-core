# access属性の検証

これらの関数を使用して要素の`access`属性を検証することができます。

## 関数

| 関数                  |
|:--------------------|
| accessIs            |
| accessIsNot         |
| accessStartsWith    |
| accessStartsWithNot |
| accessContains      |
| accessContainsNot   |
| accessEndsWith      |
| accessEndsWithNot   |
| accessMatches       |
| accessMatchesNot    |
| accessIsEmpty       |
| accessIsNotEmpty    |

## 例

#### AssertingAttribute1.kt

(`kotlin/tutorial/basic/AssertingAttribute1.kt`)

```kotlin
@Test
@Order(50)
fun accessAssertion_OK() {

    scenario {
        case(1) {
            condition {
                it.macro("[Network & internet Screen]")
            }.action {
                it.select("@Network & internet", log = true)
            }.expectation {
                it.accessIs("Network & internet")
                    .accessIsNot("System")
            }
        }
    }
}

@Test
@Order(60)
fun accessAssertion_NG() {

    scenario {
        case(1) {
            condition {
                it.macro("[Network & internet Screen]")
            }.action {
                it.select("@Network & internet", log = true)
            }.expectation {
                it.accessIs("Connected devices")
            }
        }
    }
}
```

### Link

- [index](../../../index_ja.md)
