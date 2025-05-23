# canDetect (Vision)

これらの関数を使用して要素を取得できるかどうかを調べることができます。関数はtrueまたはfalseを返します。

## 関数

| 関数                       | 説明                                                                               |
|:-------------------------|:---------------------------------------------------------------------------------|
| canDetect                | 現在の画面内でセレクターにマッチする最初の要素を取得し、trueまたはfalseを返します。 `withScroll`関数内で使用するとスクロールが発生します。 |
| canDetectWithScrollDown  | セレクターにマッチする最初の要素を取得し、true または falseを返します。（下方向スクロールあり）                            |
| canDetectWithScrollUp    | セレクターにマッチする最初の要素を取得し、true または falseを返します。（上方向スクロールあり）                            |
| canDetectWithScrollRight | セレクターにマッチする最初の要素を取得し、true または falseを返します。（右方向スクロールあり）                            |
| canDetectWithScrollLeft  | セレクターにマッチする最初の要素を取得し、true または falseを返します。（左方向スクロールあり）                            |
| canDetectWithoutScroll   | セレクターにマッチする最初の要素を取得し、true または falseを返します。（スクロールなし）                               |
| canDetectAll             | セレクターにマッチする全ての要素を取得し、true または falseを返します。 `withScroll`関数内で使用するとスクロールが発生します。      |

## 主要な引数

| 引数              | 説明                                                                       |
|:----------------|:-------------------------------------------------------------------------|
| expression      | [セレクター式](../../selector_and_nickname/selector_expression_ja.md)          |
| language        | [AI-OCRの言語](../../switching_environment/switching_ai_ocr_language_ja.md) |
| last            | true: 最後の要素を取得します<br>false: 最初の要素を取得します(デフォルト)                           |
| looseMatch      | true: テキスト検出にルースマッチングを適用します(デフォルト)<br>false: ルースマッチングを適用しません             |
| autoImageFilter | true: 画像フィルターを自動適用してAI-OCRの認識精度を向上させます<br>false: 画像フィルターを適用しません(デフォルト)   |

## サンプルコード

[サンプルの入手](../../../getting_samples_ja.md)

### CanDetect1.kt

(`src/test/kotlin/tutorial/basic/CanDetect1.kt`)

```kotlin
    @Test
    @Order(10)
    fun canDetect() {

        scenario {
            case(1) {
                expectation {
                    it.canDetect("設定")
                        .thisIsTrue("<設定>が見つかりました")
                }
            }
            case(2) {
                expectation {
                    it.canDetectWithScrollDown("システム")
                        .thisIsTrue("<システム>が見つかりました（下方向スクロールあり）")
                }
            }
            case(3) {
                expectation {
                    it.canDetectWithScrollUp("設定")
                        .thisIsTrue("<設定>が見つかりました（上方向スクロールあり）")
                }
            }
            case(4) {
                expectation {
                    withScrollDown {
                        it.canDetectAll("設定", "システム")
                            .thisIsTrue("<設定>が見つかりました（下方向スクロールあり）")
                    }
                }
            }
            case(5) {
                expectation {
                    withScrollUp {
                        it.canDetectAll("システム", "設定")
                            .thisIsTrue("<設定>が見つかりました（上方向スクロールあり）")
                    }
                }
            }
        }
    }
```

### Link

- [index](../../../../index_ja.md)
