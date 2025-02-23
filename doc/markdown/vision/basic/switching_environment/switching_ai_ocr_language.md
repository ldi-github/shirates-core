# Switching AI-OCR language (Vision)

Set `visionOCRLanguage` parameter in properties file.

### English(default)

```properties
## Vision --------------------
#visionOCRLanguage=
```

Just comment it out. `en` is default.

### Japanese

```properties
## Vision --------------------
visionOCRLanguage=ja
```

### Supported languages

The languages supported in Vision Framework **RecognizeTextRequest** are available.<br>
If you set an unsupported language, you will see error message as follows.

```
shirates.core.exception.TestDriverException: zn is not supported. Supported languages: ar, ars, de, en, es, fr, it, ja, ko, pt, ru, th, uk, vi, yue, zh
```

### setOCRLanguage function

You can change AI-OCR language dynamically using `setOCRLanguage` function.

```kotlin
it.setOCRLanguage("ja")
```

### Reference

[supportedRecognitionLanguages()](https://developer.apple.com/documentation/vision/vnrecognizetextrequest/supportedrecognitionlanguages())

### Link

- [index](../../../index.md)
