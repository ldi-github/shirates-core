# text属性の検証

これらの関数を使用して要素の`text`または`label`または`value`の属性を検証することができます。

## 関数

| 関数                    |
|:----------------------|
| textIs                |
| textIsNot             |
| textStartsWith        |
| textStartsWithNot     |
| textContains          |
| textContainsNot       |
| textEndsWith          |
| textEndsWithNot       |
| textMatches           |
| textMatchesNot        |
| textMatchesDateFormat |
| textIsEmpty           |
| textIsNotEmpty        |

### 注意

`text` 属性はAndroid用です。

`label`属性と`value`属性はiOS用です。 `value`属性は`label`属性が空文字の場合に適用されます。

![](../../_images/xml_data_text_label_value.png)

## 例

### AssertingAttribute1.kt

(`kotlin/tutorial/basic/AssertingAttribute1.kt`)

```kotlin
@Test
@Order(10)
fun textAssertion_OK() {

    scenario {
        case(1) {
            condition {
                it.macro("[Android Settings Top Screen]")
            }.action {
                it.select("Network & internet", log = true)
            }.expectation {
                it
                    .textIs("Network & internet")
                    .textIsNot("Notifications")

                    .textStartsWith("Network &")
                    .textStartsWithNot("Connected")

                    .textContains("work & int")
                    .textContainsNot("device")

                    .textEndsWith("& internet")
                    .textEndsWithNot("devices")

                    .textMatches("^Net.*")
                    .textMatchesNot("^Connected.*")

                    .textIsNotEmpty()
            }
        }
        case(2) {
            action {
                it.select("#account_avatar", log = true)
            }.expectation {
                it.textIsEmpty()
            }
        }
    }
}

@Test
@Order(20)
fun textAssertion_NG() {

    scenario {
        case(1) {
            condition {
                it.macro("[Android Settings Top Screen]")
            }.action {
                it.select("Network & internet", log = true)
            }.expectation {
                it.textIs("Connected devices")
            }
        }
    }
}
```

### Link

- [index](../../../index_ja.md)

