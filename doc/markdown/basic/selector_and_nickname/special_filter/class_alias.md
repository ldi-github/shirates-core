# Class alias

**class alias** is special filter for filtering elements.

| class alias | corresponding (Android)                                                                                                                                                                        | corresponding (iOS)                                                                                                                |
|:------------|:-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:-----------------------------------------------------------------------------------------------------------------------------------|
| label       | android.widget.TextView                                                                                                                                                                        | XCUIElementTypeStaticText                                                                                                          |
| input       | android.widget.EditText                                                                                                                                                                        | XCUIElementTypeTextField<br>XCUIElementTypeSecureTextField                                                                         |
| image       | android.widget.ImageView                                                                                                                                                                       | XCUIElementTypeImage                                                                                                               |
| button      | android.widget.Button<br>android.widget.ImageButton<br>android.widget.CheckBox<br>android.widget.RadioButton                                                                                   | XCUIElementTypeButton                                                                                                              |
| switch      | android.widget.Switch                                                                                                                                                                          | XCUIElementTypeSwitch                                                                                                              |
| widget      | (all of the above)                                                                                                                                                                             | (all of the above)                                                                                                                 |
| scrollable  | androidx.recyclerview.widget.RecyclerView<br>android.support.v7.widget.RecyclerView<br>android.widget.ScrollView<br>android.widget.HorizontalScrollView<br>androidx.viewpager.widget.ViewPager | XCUIElementTypeTable<br>XCUIElementTypeCollectionView<br>XCUIElementTypeScrollView<br>XCUIElementTypeWebView<br>XCUIElementTypeMap |

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

