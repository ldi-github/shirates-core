# tapAppIcon

**tapAppIcon** 関数を使用するとアプリアイコンをタップしてアプリを起動できます。

## 関数

| 関数         | 説明                                   |
|:-----------|:-------------------------------------|
| tapAppIcon | ホーム画面またはアプリのランチャーメニューのアプリアイコンをタップします |

## サンプルコード

[サンプルの入手](../../../getting_samples_ja.md)

### TapAppIcon1.kt

(`kotlin/tutorial/basic/TapAppIcon1.kt`)

```kotlin
    @Test
    fun tapAppIcon() {

        scenario {
            case(1) {
                action {
                    it.tapAppIcon("Chrome")
                }.expectation {
                    it.appIs("[Chrome]")
                }
            }
            case(2) {
                action {
                    it.tapAppIcon("Play ストア")
                }.expectation {
                    it.appIs("[Playストア]")
                }
            }
        }
    }
```

### Link

- [index](../../../../index_ja.md)
