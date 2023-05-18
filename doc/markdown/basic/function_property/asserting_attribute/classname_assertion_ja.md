# クラス名の検証

これらの関数を使用して要素の`className`属性または`type`属性を検証することができます。

## 関数

| 関数      |
|:--------|
| classIs |

## 例

### AssertingAttribute1.kt

(`kotlin/tutorial/basic/AssertingAttribute1.kt`)

```kotlin
@Test
@Order(70)
fun classNameAssertion_OK() {

    scenario {
        case(1) {
            condition {
                it.macro("[Android Settings Top Screen]")
            }.action {
                it.select("#account_avatar", log = true)
            }.expectation {
                it.classIs("android.widget.ImageView")
                    .classIsNot("android.widget.TextView")
            }
        }
    }
}

@Test
@Order(80)
fun classNameAssertion_NG() {

    scenario {
        case(1) {
            condition {
                it.macro("[Android Settings Top Screen]")
            }.action {
                it.select("#account_avatar", log = true)
            }.expectation {
                it.classIs("android.widget.TextView")
            }
        }
    }
}
```

#### 注意

`log = true` はデモンストレーション用です。実際のテストコードでは指定しないでください。デフォルトはfalseです。

### Link

- [index](../../../index_ja.md)

