# 相対画像 (Vision)

これらの関数を使用して画像を相対的に取得することができます。

## 関数

| 関数        | 説明             |
|:----------|:---------------|
| rightItem | 要素の右側の画像を取得します |
| leftItem  | 要素の左側の画像を取得します |
| belowItem | 要素の上側の画像を取得します |
| aboveItem | 要素の下側の画像を取得します |

### サンプルコード

[サンプルの入手](../../../getting_samples_ja.md)

### RelativeItem1.kt

(`src/test/kotlin/tutorial/basic/RelativeItem1.kt`)

```kotlin
    @Test
    @Order(10)
    fun belowItem_aboveItem() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    v1 = findImage("[Connected devices Icon]")
                }.expectation {
                    v1.belowItem().imageIs("[Apps Icon]")
                    v1.aboveItem().imageIs("[Network & internet Icon]")
                }
            }
        }

    }
```

![](_images/above_item_below_item.png)

```kotlin
    @Test
    @Order(20)
    fun rightItem_leftItem() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Files Top Screen]")
                }.action {
                    v1 = findImage("[Audio Button]")
                }.expectation {
                    v1.rightItem(include = true).imageIs("[Videos Button]")
                    v1.leftItem(include = true).imageIs("[Images Button]")
                }
            }
        }
    }
```

**Note:**<br>
`include = true` combines image pieces that are overlapped into a group. `include = false` is default.

![](_images/left_item_right_item.png)

### Link

- [index](../../../../index_ja.md)
