# セレクター式 (Vision)

Shiratesの **セレクター式** ビジュアル要素をフィルターするための式です。

**detect** 関数はセレクター式を受け取り、これを解析し、要素を抽出してフィルターし、結果の要素を返します。

**visionモードでは**

- テキストの情報を利用できます
- DOM要素のメタ情報(id, accessibility, classなど)を利用することはできません

このようにShirates/VisionにおいてはSelectorの機能は制限されたものとなります。

<br>

### Visionモード

**Visionモード**では以下のセレクターが利用できます。

```kotlin
it.detect("text1")

it.detect("*text1*")

it.detect("text1||text2")
```

### Classicモード

以下のセレクターは**Classicモード**でのみ利用できます。

```kotlin
it.select("#id1")    // id filter

it.select("@accessibility1") // accessibility filter

it.select(".android.widget.ImageButton")    // class filter

it.select("xpath=//*[@resource-id='android:id/icon']")  // xpath filter

it.select("Hello&&.android.widget.TextView")    // text filter and class filter combined with "&&"(and) operator

it.select("About phone||About emulated device") // text filters combined with "||"(or) operator
```

### 注意

`&&`演算子はVisionモードではサポートされません。

<br>

### Link

- [index](../../../index_ja.md)
