# スクリーンショットを構成する (Vision)

testrunファイルでスクリーンショットを構成することができます。
参照 [パラメーター](../../basic/parameter/parameters_ja.md)

```
## Screenshot --------------------
#screenshotScale=0.333333
#screenshotIntervalSeconds=0.5
#autoScreenshot=false
#onChangedOnly=false
#onCondition=false
#onAction=false
#onExpectation=false
#onExecOperateCommand=false
#onCheckCommand=false
#onScrolling=false
#manualScreenshot=false
```

## ファイルサイズの縮小

`screenshotScale` はデフォルトで 0.5 です。スクリーンショットのサイズを縮小したい場合はこのパラメーターを小さくします。

```
screenshotScale=0.333333
```

### スクリーンショットファイルサイズの例

![](../_images/screenshot_scale_and_size.png)

### スクリーンショットの明瞭さ

![](../_images/screenshot_clarity.png)

<br>

## トリガーの削減

例えばconditionとactionでスクリーンショットが必要ないならばスクリーンショットの取得を抑制することができます。

```
onCondition=false
onAction=false
```

<br>
スクロール中のスクリーンショットが不要ならばスクリーンショットの取得を抑制することができます。

```
onScrolling=false
```

### Link

- [ログ出力を構成する](configuring_log_ja.md)


- [index](../../index_ja.md)

