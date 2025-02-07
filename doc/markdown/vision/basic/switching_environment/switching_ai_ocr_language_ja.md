# AI-OCR言語の切り替え (Vision)

propertiesファイルの`visionOCRLanguage`パラメータを設定します。

### English(default)

```properties
## Vision --------------------
#visionOCRLanguage=
```

単純にコメントアウトします。`en`がデフォルトです。

### 日本語

```properties
## Vision --------------------
visionOCRLanguage=ja
```

### サポートする言語

AI-OCRを利用できない言語を設定した場合は以下のようなエラーが出力されます。

```
shirates.core.exception.TestDriverException: zn is not supported. Supported languages: ar, ars, de, en, es, fr, it, ja, ko, pt, ru, th, uk, vi, yue, zh
```

### setOCRLanguage関数

AI-OCRの言語は`setOCRLanguage`関数を使用して動的に変更することができます。

```kotlin
it.setOCRLanguage("ja")
```

### 関連情報

[supportedRecognitionLanguages()](https://developer.apple.com/documentation/vision/vnrecognizetextrequest/supportedrecognitionlanguages())

### Link

- [index](../../../index_ja.md)
