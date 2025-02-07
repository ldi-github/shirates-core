# ログ出力の言語 (Vision/Classic)

**logLanguage** を設定するとログ出力とレポートの言語を変更できます。

### 注意

- これはAndroid/iOSデバイスの言語設定パラメーターではありません。代わりにAppiumのcapabilityを設定する必要があります。
  参照 [デバイスの言語](device_language_ja.md)

### サポートする言語

| logLanguage | 言語      |
|:------------|---------|
| ""(empty)   | English |
| ja          | 日本語     |

## 例

### AndroidSettingsDemo.kt

(`kotlin/demo/AndroidSettingsVisionDemo.kt`)

```kotlin
    @Test
    fun airplaneModeSwitch() {

        scenario {
            case(1) {
                condition {
                    it.screenIs("[Android設定トップ画面]")
                }.action {
                    it.tap("ネットワークとインターネット")
                }.expectation {
                    it.screenIs("[ネットワークとインターネット画面]")
                }
            }
            case(2) {
                condition {
                    it.detect("機内モード")
                        .rightItem()
                        .checkIsOFF()
                }.action {
                    it.tap()
                }.expectation {
                    it.detect("機内モード")
                        .rightItem()
                        .checkIsON()
                }
            }
            case(3) {
                action {
                    it.tap()
                }.expectation {
                    it.detect("機内モード")
                        .rightItem()
                        .checkIsOFF()
                }
            }
        }
    }
```

### testrun.properties (default)

```
#logLanguage=
```

### 実行結果 (default)

![](_images/log_language_en_ja.png)

### testrun.properties (ja)

```
logLanguage=ja
```

### 実行結果 (ja)

![](_images/log_language_ja_ja.png)

### Link

- [index(Vision)](../../index_ja.md)
- [index(Classic)](../../classic/index_ja.md)

