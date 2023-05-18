# 相対セレクター(方向ベース)

## 相対セレクター

| 相対セレクター      | 説明             |
|:-------------|:---------------|
| :right       | 右方向に存在するウィジェット |
| :rightInput  | 右方向に存在するinput  |
| :rightLabel  | 右方向に存在するlabel  |
| :rightImage  | 右方向に存在するimage  |
| :rightButton | 右方向に存在するbutton |
| :rightSwitch | 右方向に存在するswitch |
| :below       | 下方向に存在するウィジェット |
| :belowInput  | 下方向に存在するinput  |
| :belowLabel  | 下方向に存在するlabel  |
| :belowImage  | 下方向に存在するimage  |
| :belowButton | 下方向に存在するbutton |
| :belowSwitch | 下方向に存在するswitch |
| :left        | 左方向に存在するウィジェット |
| :leftInput   | 左方向に存在するinput  |
| :leftLabel   | 左方向に存在するlabel  |
| :leftImage   | 左方向に存在するimage  |
| :leftButton  | 左方向に存在するbutton |
| :leftSwitch  | 左方向に存在するswitch |
| :above       | 上方向に存在するウィジェット |
| :aboveInput  | 上方向に存在するinput  |
| :aboveLabel  | 上方向に存在するlabel  |
| :aboveImage  | 上方向に存在するimage  |
| :aboveButton | 上方向に存在するbutton |
| :aboveSwitch | 上方向に存在するswitch |

## 使用例

| example                     | description                                                                                            |
|:----------------------------|:-------------------------------------------------------------------------------------------------------|
| `<text1>:right`             | textが"text1"である最初の要素を選択し、右方向に存在する最初のウィジェットを選択する                                                        |
| `<text1>:right(2)`          | textが"text1"である最初の要素を選択し、右方向に存在する2番目のウィジェットを選択する。これは`<text1>:right(pos=2)` または `<text1>:right([2])`と同じ |
| `<text1>:rightSwitch`       | textが"text1"である最初の要素を選択し、右方向に存在する2番目のswitchを選択する                                                       |
| `<text1>:right(text2)`      | textが"text1"である最初の要素を選択し、右方向に存在するtextが"text2"である最初のウィジェットを選択する                                         |
| `<text1>:right:belowButton` | textが"text1"である最初の要素を選択し、右方向に存在する最初のウィジェットを選択し、下方向に存在する最初のbuttonを選択する                                  |

<br>

## 方向(Direction)

right, below, left, above

![direction](../../_images/direction_4way.png)

<br>

### rightセレクターの使用方法 (Android)

TextView1を基準とした場合

![direction right android 1](../../_images/direction_right_android.png)

<br>

### rightセレクターの使用方法 (iOS)

StaticText1を基準とした場合

![direction right ios 1](../../_images/direction_right_ios.png)

### 探索範囲

`right`セレクターは基準となる要素のtopとbottomの範囲を右方向をに探索します。

![search range](../../_images/direction_search_range.png)

### Link

- [相対セレクター (ウィジェットフローベース)](relative_selector_flow_ja.md)

- [相対セレクター (XMLベース)](relative_selector_xml_ja.md)


- [相対セレクター](relative_selector_ja.md)
