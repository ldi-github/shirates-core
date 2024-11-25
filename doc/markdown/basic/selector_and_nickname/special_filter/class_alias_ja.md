# クラスエイリアス

**クラスエイリアス** は要素をフィルターするための特別なフィルターです。

| クラスエイリアス   | 対応するclassName (Android)                                                                                                                                                                        | 対応するtype (iOS)                                                                                                                     |
|:-----------|:-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:-----------------------------------------------------------------------------------------------------------------------------------|
| label      | android.widget.TextView                                                                                                                                                                        | XCUIElementTypeStaticText                                                                                                          |
| input      | android.widget.EditText                                                                                                                                                                        | XCUIElementTypeTextField<br/>XCUIElementTypeSecureTextField                                                                        |
| image      | android.widget.ImageView                                                                                                                                                                       | XCUIElementTypeImage                                                                                                               |
| button     | android.widget.Button<br/>android.widget.ImageButton<br/>android.widget.CheckBox<br>android.widget.RadioButton                                                                                 | XCUIElementTypeButton                                                                                                              |
| switch     | android.widget.Switch                                                                                                                                                                          | XCUIElementTypeSwitch                                                                                                              |
| widget     | (上記のすべて)                                                                                                                                                                                       | (上記のすべて)                                                                                                                           |
| scrollable | androidx.recyclerview.widget.RecyclerView<br>android.support.v7.widget.RecyclerView<br>android.widget.ScrollView<br>android.widget.HorizontalScrollView<br>androidx.viewpager.widget.ViewPager | XCUIElementTypeTable<br>XCUIElementTypeCollectionView<br>XCUIElementTypeScrollView<br>XCUIElementTypeWebView<br>XCUIElementTypeMap |

<br>

**クラスエイリアス** は各プラットフォームの実装を隠蔽して取り扱いを容易にします。

## 例

```kotlin
it.tap(".button&&Submit")
```

上記は実行時に以下のように展開されます。

### Android

```kotlin
it.tap(".(android.widget.Button|android.widget.ImageButton|android.widget.CheckBox)&&Submit")
```

### iOS

```kotlin
it.tap(".XCUIElementTypeButton&&Submit")
```

クラスエイリアスを使用すると特定のclassName(またはtype)を記述するよりも記述が簡潔になります。

### Link

- [スペシャルフィルター](../special_filter/special_filter_ja.md)


- [index](../../../index_ja.md)

