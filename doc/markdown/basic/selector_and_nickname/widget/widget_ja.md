# ウィジェット

**ウィジェット** はユーザーインターフェースにおける抽象的な要素です。

組み込みウィジェットには以下のものがあります。

| ウィジェット | 対応する要素 (Android)                                                                 | 対応する要素 (iOS)                                                |
|:-------|:---------------------------------------------------------------------------------|:------------------------------------------------------------|
| label  | android.widget.TextView                                                          | XCUIElementTypeStaticText                                   |
| input  | android.widget.EditText                                                          | XCUIElementTypeTextField<br/>XCUIElementTypeSecureTextField |
| image  | android.widget.ImageView                                                         | XCUIElementTypeImage                                        |
| button | android.widget.Button<br/>android.widget.ImageButton<br/>android.widget.CheckBox | XCUIElementTypeButton                                       |
| switch | android.widget.Switch                                                            | XCUIElementTypeSwitch                                       |
| widget | (all of the above)                                                               | (all of the above)                                          |

<br>

`element_category.properties`リソースファイルで定義情報を編集できます（非推奨です）。

```properties
#Android
android.labelTypes=android.widget.TextView
android.inputTypes=android.widget.EditText
android.imageTypes=android.widget.ImageView
android.buttonTypes=android.widget.Button|android.widget.ImageButton|android.widget.CheckBox
android.switchTypes=android.widget.Switch
#iOS
ios.labelTypes=XCUIElementTypeStaticText
ios.inputTypes=XCUIElementTypeTextField|XCUIElementTypeSecureTextField
ios.imageTypes=XCUIElementTypeImage
ios.buttonTypes=XCUIElementTypeButton
ios.switchTypes=XCUIElementTypeSwitch
```

### Link

- [index](../../../index_ja.md)

