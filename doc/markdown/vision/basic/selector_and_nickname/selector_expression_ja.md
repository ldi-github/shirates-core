# セレクター式 (Vision)

Shiratesの **セレクター式** はビジュアル要素をフィルターするための式です。

**detect** 関数はセレクター式を受け取り、これを解析し、要素を抽出してフィルターし、結果の要素を返します。

**visionモードでは**

- テキストの情報を利用できます
- DOM要素のメタ情報(id, accessibility, classなど)を利用することはできません
- テキストの精度はAI-OCRに依存するため、フィルターの適用結果が期待値と異なる場合があります

このようにShirates/Visionにおいてはセレクターの機能は制限されたものとなります。

<br>

### Visionモード

**Visionモード**では以下のフィルターが利用できます。

```kotlin
it.detect("text1")  // "text1"に等しい

it.detect("*text1*")    // "text1"を含む

it.detect("text1*")     // "text1"で開始

it.detect("*text2")     // "text2"で終了

it.detect("text1*&&*text2") // "text1"で開始 かつ "text2"で終了

it.detect("text1||text2")   // "text1" または "text2"

it.detect("text1*&&*text2||text3")  // "(text1"で開始 かつ "text2"で終了) または "text3"に等しい
```

### Classicモード

以下のフィルターは**Classicモード**でのみ利用できます。

```kotlin
it.select("#id1")    // idフィルター

it.select("@accessibility1") // アクセシビリティフィルター

it.select(".android.widget.ImageButton")    // クラスフィルター

it.select("xpath=//*[@resource-id='android:id/icon']")  // xpathフィルター

it.select("Hello&&.android.widget.TextView")    // テキストフィルターとクラスフィルターを"&&"(and)演算子で結合
```

<br>

### Link

- [index](../../../index_ja.md)
