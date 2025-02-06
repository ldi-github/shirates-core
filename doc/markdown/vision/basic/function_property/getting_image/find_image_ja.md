# findImage (Vision)

これらの関数を使用すると画像を検索することができます。

## 関数

| 関数                       | 説明                                 |
|:-------------------------|:-----------------------------------|
| findImage                | 現在の画面内でテンプレート画像にマッチする画像を見つけます      |
| findImageWithScrollDown  | テンプレート画像にマッチする画像を見つけます（下方向スクロールあり） |
| findImageWithScrollUp    | テンプレート画像にマッチする画像を見つけます（上方向スクロールあり） |
| findImageWithScrollRight | テンプレート画像にマッチする画像を見つけます（右方向スクロールあり） |
| findImageWithScrollLeft  | テンプレート画像にマッチする画像を見つけます（左方向スクロールあり） |

### しきい値の調整

実際には画像が存在しているにもかかわらずエラーが発生する場合があり、以下のようなメッセージが表示されます。

```
[info]	+1727	!	()	findImage("[Location Icon]") not found. (distance:0.40028667 > threshold:0.1)
```

この場合、`threshold`引数を明示的に指定します。

```
findImageWithScrollDown("[Location Icon]", threshold = 0.5)
```

### サンプルコード

[サンプルの入手](../../../getting_samples_ja.md)

### テンプレート画像

![](_images/template_images_ja.png)

### FindImage1.kt

(`src/test/kotlin/tutorial/basic/FindImage1.kt`)

```kotlin
    @Test
    @Order(10)
    fun findImage() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android設定トップ画面]")
                }.action {
                    withScrollDown {
                        v1 = it.findImage("[ネットワークとインターネットアイコン]")
                        v2 = it.findImage("[ディスプレイアイコン]")
                    }
                }.expectation {
                    v1.isFound.thisIsTrue("[ネットワークとインターネットアイコン]が見つかりました。")
                    v2.isFound.thisIsTrue("[ディスプレイアイコン]が見つかりました。")
                }
            }
        }
    }

    @Test
    @Order(20)
    fun findImageWithScrollDown_findImageWithScrollUp() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android設定トップ画面]")
                }.action {
                    v1 = findImageWithScrollDown("[位置情報アイコン]", threshold = 0.5)
                }.expectation {
                    v1.imageIs("[位置情報アイコン]")
                }
            }
            case(2) {
                action {
                    v1 = findImageWithScrollUp("[接続設定アイコン]")
                }.expectation {
                    v1.imageIs("[接続設定アイコン]")
                }
            }
        }
    }

    @Test
    @Order(30)
    fun findImageWithScrollRight_findImageWithScrollLeft() {

        scenario {
            case(1) {
                condition {
                    it.macro("[ファイルトップ画面]")
                }.action {
                    it.onLineOf("画像") {
                        v1 = findImageWithScrollRight("[今週ボタン]")
                    }
                }.expectation {
                    v1.imageIs("[今週ボタン]")
                }
            }
            case(2) {
                action {
                    v1.onLine {
                        v2 = findImageWithScrollLeft("[音声ボタン]")
                    }
                }.expectation {
                    v2.imageIs("[音声ボタン]")
                }
            }
        }
    }
```

### Link

- [index](../../../../index_ja.md)
