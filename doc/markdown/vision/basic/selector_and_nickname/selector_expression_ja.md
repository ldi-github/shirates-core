# セレクター式

Shiratesの **セレクター式** は画面要素をフィルターするための式です。

**select**関数はセレクター式を受け取り、解析し、要素を取得してフィルターし、結果を返します。

セレクター式は1つ以上の **filter expressions** から構成されます。

```kotlin
it.select("#id1")    // idフィルター

it.select("@accessibility1") // accessibilityフィルター

it.select(".android.widget.ImageButton")    // classフィルター

it.select("text1")  // textフィルター

it.select("xpath=//*[@resource-id='android:id/icon']")  // xpathフィルター

it.select("Hello&&.android.widget.TextView")    // textフィルターとclassフィルターを"&&"(and)演算子で結合

it.select("About phone||About emulated device") // textフィルターを"||"(or)演算子で結合
```

## フィルター式

Shiratesの **フィルター式** は画面要素をフィルターするための条件を記述するための式です。

| フィルター            | 完全形                       | 短縮形         | Androidの属性   | iOSの属性  | 備考   |
|:-----------------|:--------------------------|:------------|:-------------|:--------|:-----|
| text             | text=text1                | text1       | text         | label   |      |
| textStartsWith   | textStartsWith=text1      | text1*      | text         | label   |      |
| textContains     | textContains=text1        | \*text1*    | text         | label   |      |
| textEndsWith     | textEndsWith=text1        | *text1      | text         | label   |      |
| textMatches      | textMatches=^text$        | n/a         | text         | label   |      |
| literal          | literal=literal1          | 'literal1'  | text         | label   |      |
| id               | id=id1                    | #id1        | resource-id  | name    |      |
| access           | access=access1            | @access1    | content-desc | name    |      |
| accessStartsWith | accessStartsWith=access1  | @access1*   | content-desc | name    |      |
| accessContains   | accessContains=access1    | @\*access1* | content-desc | name    |      |
| accessEndsWith   | accessEndsWith=access1    | @*access1   | content-desc | name    |      |
| accessMatches    | accessMatches=^access1$   | n/a         | content-desc | name    |      |
| value            | value=value1              | n/a         | text         | value   |      |
| valueStartsWith  | valueStartsWith=value1    | n/a         | text         | value   |      |
| valueContains    | valueContains=value1      | n/a         | text         | value   |      |
| valueEndsWith    | valueEndsWith=value1      | n/a         | text         | value   |      |
| valueMatches     | valueMatches=^value1$     | n/a         | text         | value   |      |
| class            | class=class1              | .class1     | class        | type    |      |
| focusable        | focusable=true            | n/a         | focusable    | n/a     |      |
| scrollable       | scrollable=true           | n/a         | scrollable   | n/a     |      |
| selected         | selected=true             | n/a         | selected     | n/a     |      |
| visible          | visible=true              | n/a         | n/a          | visible | ※非推奨 |
| xpath            | xpath=//*[@text='text1']  | n/a         | (任意の属性)      | (任意の属性) |      |
| pos              | pos=2                     | [2]         | n/a          | n/a     |      |
| ignoreTypes      | ignoreTypes=Class1,Class2 | n/a         | class        | type    |      |
| image            | image=image1.png          | image1.png  | n/a          | n/a     |      |
| capturable       | capturable=??             | ??          | n/a          | n/a     |      |

※iOSのvisible属性は要素が画面に表示されているかどうかの判定には利用できないので非推奨です。

## フィルターにおける複数値の指定

フィルターは括弧と"|"(or)演算子を使用して複数の値を指定できます。

```kotlin
it.select("text=(text1|text2)") // 完全形

it.select("(text1|text2)")  // 短縮形
```

以下のセレクター式はシンプルですが上記と等価です。

```kotlin
it.select("text1||text2")   // 上記と等価なセレクター式
```

この場合、フィルターによるor表現を使用しても、セレクターによるor表現を使用しても結果は同じです。

さらに複雑な状況では"|"を使用することが必要になる場合があります。

```kotlin
it.select("(text1|text2)&&.class1||(@access1|@access2)&&.class2)")
```

## セレクター式のルール

フィルター式は単独でセレクター式として使用できます。

```
text1
```

フィルター式は"&&"(AND)演算子で結合することができます。

```
text1&&.class1&&visible=true
```

フィルター式は"||"(OR)演算子で結合することができます。

```
text1||text2||@access1
```

"&&" は  "||" より優先されます。

```
text1&&.class1||@access1
```

上記は`(text=text1 かつ class=class1) または access=access1` という意味になります。

以下のように括弧を使用することはできません（サポートしていません）。

**悪い例**

```
(text1&&.class1)||@access1
```

## 完全修飾id

Androidでは`resource-id`
はアプリのパッケージ名がプレフィックスになっています。例えば設定アプリでは"`com.android.settings`"の部分がパッケージ名です。

| 完全修飾形                              | 省略形        |
|:-----------------------------------|:-----------|
| com.android.settings:id/search_bar | search_bar |

Shiratesでは省略形で記述すると完全修飾形に自動で変換されます。可読性が高くなるので省略形の使用が推奨されます。省略形でうまく行かない場合は完全修飾形を使用します。

## セレクター式のプラットフォームアノテーション

**プラットフォームアノテーション** (@a, @i)を使用してAndroid用のセレクター式とiOS用のセレクター式を1行で記述することができます。

```
@a<.android.widget.ImageButton>,@i<.XCUIElementTypeButton>
```

```kotlin
it.select("@a<.android.widget.ImageButton>,@i<.XCUIElementTypeButton>")
```

このアノテーションを使用することで[ニックネーム](nickname/nickname_ja.md)をAndroidとiOSそれぞれに対して定義することができます。

```
"[Button1]": "@a<.android.widget.ImageButton>,@i<.XCUIElementTypeButton>"
```

## 否定フィルター

複雑な状況においては否定フィルターを使用することができます。

```
text=text1      // 通常
text!=text1     // 否定

text1       // 通常
!text1      // 否定

accessContains=text1    // 通常
accessContains!=text1   // 否定

@*text1*    // 通常
!@*text1*   // 否定
```

## 画像フィルター

画像ファイルを使用して画像マッチング（テンプレートマッチング）を行うことができます。
参照 [画像の検証](../function_property/asserting_image/image_assertion_ja.md)

```
image=image1.png    // 完全形
image1.png          // 省略形
image1.png?scale=0.5&threshold=20   // オプション付き(完全形)
image1.png?s=0.5&t=20   // オプション付き(省略形)
```

## キャプチャ可能性フィルター（メタフィルター）

アプリの実装によっては画面上に表示されているにもかかわらず、対応する要素を取得できないことがあります。
例えば画面上に表示されている画像に対応する要素がAndroidでは取得できるが、iOSでは取得できない場合があります。
この場合、キャプチャ可能性フィルターを使用することでiOSでは要素がキャプチャできないことを明示的に表現できます。

```kotlin
it.existImage("@a<.android.widget.ImageButton>,@i<??>")
```

この例ではAndroidにおいては`.android.widget.ImageButton`で要素を検索し、画像テンプレートによるマッチングを行いますが、
iOSにおいては要素検索と画像マッチングを行わず、検証結果は手動テストが必要であることを示す`COND_AUTO`になります。

### Link

- [index](../../index_ja.md)
