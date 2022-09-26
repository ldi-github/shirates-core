# Widget

**Widget** is abstract user interface element.

Built-in widget information is as follows.

| widget | corresponding (Android)                                                          | corresponding (iOS)                                         |
|:-------|:---------------------------------------------------------------------------------|:------------------------------------------------------------|
| label  | android.widget.TextView                                                          | XCUIElementTypeStaticText                                   |
| input  | android.widget.EditText                                                          | XCUIElementTypeTextField<br/>XCUIElementTypeSecureTextField |
| image  | android.widget.ImageView                                                         | XCUIElementTypeImage                                        |
| button | android.widget.Button<br/>android.widget.ImageButton<br/>android.widget.CheckBox | XCUIElementTypeButton                                       |
| switch | android.widget.Switch                                                            | XCUIElementTypeSwitch                                       |
| widget | (all of the above)                                                               | (all of the above)                                          |

<br>

You can edit this information in `element_category.properties`resource file (not recommended).

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

- [index](../../../index.md)

