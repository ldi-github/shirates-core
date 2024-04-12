# Class alias

**class alias** is special filter for filtering elements.

| class alias | corresponding (Android)                                                        | corresponding (iOS)                                        |
|:------------|:-------------------------------------------------------------------------------|:-----------------------------------------------------------|
| label       | android.widget.TextView                                                        | XCUIElementTypeStaticText                                  |
| input       | android.widget.EditText                                                        | XCUIElementTypeTextField<br>XCUIElementTypeSecureTextField |
| image       | android.widget.ImageView                                                       | XCUIElementTypeImage                                       |
| button      | android.widget.Button<br>android.widget.ImageButton<br>android.widget.CheckBox | XCUIElementTypeButton                                      |
| switch      | android.widget.Switch                                                          | XCUIElementTypeSwitch                                      |
| widget      | (all of the above)                                                             | (all of the above)                                         |
| scrollable  | N/A<br>(scrollable="true" is applied)                                          | XCUIElementTypeTextField<br>XCUIElementTypeSecureTextField |

<br>

**Class alias** hides implementation of element of each platform, makes it simple.

## Example

```kotlin
it.tap(".button&&Submit")
```

The above will be expanded at runtime as follows.

### Android

```kotlin
it.tap(".(android.widget.Button|android.widget.ImageButton|android.widget.CheckBox)&&Submit")
```

### iOS

```kotlin
it.tap(".XCUIElementTypeButton&&Submit")
```

Using class aliases simplifies the description rather than writing specific class names.

### Link

- [Special filter](../special_filter/special_filter.md)
- [index](../../../index.md)

