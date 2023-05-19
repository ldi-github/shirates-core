# 任意の属性の検証

これらの関数を使用して任意の属性の検証することができます。

## 関数

| 関数          |
|:------------|
| attributeIs |

## 例

### AssertingAttribute1.kt

```kotlin
@Test
@Order(90)
fun attributeAssertion_OK() {

    scenario {
        case(1) {
            condition {
                it.macro("[Android Settings Top Screen]")
            }.action {
                it.select("#account_avatar", log = true)
            }.expectation {
                it.attributeIs("package", "com.android.settings")
            }
        }
    }
}

@Test
@Order(100)
fun attributeAssertion_NG() {

    scenario {
        case(1) {
            condition {
                it.macro("[Android Settings Top Screen]")
            }.action {
                it.select("#account_avatar", log = true)
            }.expectation {
                it.attributeIs("package", "com.google.android.calculator")
            }
        }
    }
}
```

#### 注意

`log = true` はデモンストレーション用です。実際のテストコードでは指定しないでください。デフォルトはfalseです。

### Link

- [index](../../../index_ja.md)

