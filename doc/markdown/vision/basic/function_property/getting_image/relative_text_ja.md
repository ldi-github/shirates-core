# 相対テキスト (Vision)

これらの関数を使用してテキストを相対的に取得することができます。

## 関数

| 関数        | 説明               |
|:----------|:-----------------|
| rightText | 要素の右側のテキストを取得します |
| leftText  | 要素の左側のテキストを取得します |
| belowText | 要素の下側のテキストを取得します |
| aboveText | 要素の上側のテキストを取得します |

### サンプルコード

[サンプルの入手](../../../getting_samples_ja.md)

### RelativeText1.kt

(`src/test/kotlin/tutorial/basic/RelativeText1.kt`)

```kotlin
    @Test
    @Order(10)
    fun belowText_aboveText() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Network & internet Screen]")
                }.action {
                    v1 = detect("Airplane mode")
                }.expectation {
                    v1.belowText().textIs("Hotspot & tethering")
                    v1.aboveText().textIs("T-Mobile")
                }
            }
        }

    }
```

![](_images/above_text_below_text.png)

```kotlin
    @Test
    @Order(20)
    fun rightText_leftText() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Files Top Screen]")
                }.action {
                    v1 = detect("Audio")
                }.expectation {
                    v1.rightText().textIs("Videos")
                    v1.leftText().textIs("Images")
                }
            }
        }
    }
```

![](_images/right_text_left_text.png)

### Link

- [index](../../../../index_ja.md)
