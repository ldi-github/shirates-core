# id属性の検証 (Classic)

これらの関数を使用して要素の`id` または `name`属性を検証することができます。

## 関数

| 関数   |
|:-----|
| idIs |

## 例

### AssertingAttribute1.kt

(`kotlin/tutorial/basic/AssertingAttribute1.kt`)

```kotlin
@Test
@Order(30)
fun idAssertion_OK() {

    scenario {
        case(1) {
            condition {
                it.macro("[Android Settings Top Screen]")
            }.action {
                it.select("#account_avatar", log = true)
            }.expectation {
                it
                    .idIs("account_avatar")
                    .idIs("com.android.settings:id/account_avatar")
            }
        }
    }
}

@Test
@Order(40)
fun idAssertion_NG() {

    scenario {
        case(1) {
            condition {
                it.macro("[Android Settings Top Screen]")
            }.action {
                it.select("#account_avatar", log = true)
            }.expectation {
                it
                    // OK. expected is converted to "com.android.settings:id/account_avatar"
                    .idIs("account_avatar")

                    // OK. expected is converted to "com.android.settings:id/account_avatar"
                    .idIs("account_avatar", auto = true)

                    // NG. expected is "account_avatar"
                    .idIs("account_avatar", auto = false)
            }
        }
    }
}
```

### Link

- [index](../../../index_ja.md)

