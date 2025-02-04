# tapAppIcon

**tapAppIcon** 関数を使用するとアプリアイコンをタップしてアプリを起動できます。

## 関数

| 関数         | 説明                                   |
|:-----------|:-------------------------------------|
| tapAppIcon | ホーム画面またはアプリのランチャーメニューのアプリアイコンをタップします |

## 例

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
                    it.tapAppIcon("Play Store")
                }.expectation {
                    it.appIs("[Play Store]")
                }
            }
        }
    }
```

### Link

- [index](../../../../index_ja.md)
