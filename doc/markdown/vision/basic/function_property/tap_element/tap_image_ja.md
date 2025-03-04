# tap (Vision)

これらの関数を使用して画面上の画像をタップすることができます。

## 関数

| 関数       | 説明                                 |
|:---------|:-----------------------------------|
| tapImage | 現在の画面内でテンプレート画像にマッチする画像を見つけてタップします |

### サンプルコード

[サンプルの入手](../../../getting_samples_ja.md)

## 例

### TapImage1.kt

(`kotlin/tutorial/basic/TapImage1.kt`)

```kotlin
    @Test
    fun tapImage() {

        scenario {
            case(1) {
                action {
                    it.tapImage("[ネットワークとインターネットアイコン]")
                }.expectation {
                    it.screenIs("[ネットワークとインターネット画面]")
                }
            }
            case(2) {
                condition {
                    it.pressBack()
                }.action {
                    withScrollDown {
                        it.tapImage("[システムアイコン]")
                    }
                }.expectation {
                    it.screenIs("[システム画面]")
                }
            }
        }
    }
```

### Link

- [index](../../../../index_ja.md)
