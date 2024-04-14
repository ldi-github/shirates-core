# titleセレクター

**titleセレクター** は画面を識別するためのスペシャルセレクターです。

`Network & internet`画面を Appium Inspector で見てみましょう。

![Optimizing end of scroll](../../../basic/_images/title_selector.png)

この画面には`<@Network & internet>`という画面タイトルの要素を内部に含む`<#app_bar>`というヘッダ要素があります。

このようにタイトル要素がヘッダ要素内に含まれるという階層関係を考慮すると、タイトル要素は以下のように選択することができます。

```
<#app_bar>:descendant(@Network & internet)
```

上記の代わりに **titleセレクター**を使用して`<@Network & internet>`を選択することができます。

```
~title=Network & internet
```

**titleセレクター** は実行時に以下のように展開されます。

```
<#action_bar||#toolbar||#app_bar>:descendant(Network & internet||@Network & internet)
```

展開のルールはテストフレームワークに組み込まれています。

このルールはtestrunファイルでオーバーライドできます。

```properties
android.titleSelector=<#action_bar||#toolbar||#app_bar>:descendant(${title}||@${title})
ios.titleSelector=<.XCUIElementTypeNavigationBar>:descendant(.XCUIElementTypeStaticText&&${title})
```

### Link

- [webTitleセレクター](webtitle_selector_ja.md)


- [index](../../../index_ja.md)

