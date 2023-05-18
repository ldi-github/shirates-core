# 相対セレクター(XMLベース)

## 相対セレクター

| 相対セレクター     | 説明         |
|:------------|:-----------|
| :parent     | 親要素        |
| :child      | 子要素        |
| :sibling    | 兄弟要素       |
| :ancestor   | 祖先の要素      |
| :descendant | 子孫の要素      |
| :next       | 次の要素       |
| :previous   | 前の要素       |
| :nextLabel  | 次のlabel要素  |
| :preLabel   | 前のlabel要素  |
| :nextInput  | 次のinput要素  |
| :preInput   | 前のinput要素  |
| :nextImage  | 次ののimage要素 |
| :preImage   | 前のimage要素  |
| :nextButton | 次のbutton要素 |
| :preButton  | 前のbutton要素 |
| :nextSwitch | 次のswitch要素 |
| :preSwitch  | 前のswitch要素 |

## 使用例

| 例                       | 説明                                                                              |
|:------------------------|:--------------------------------------------------------------------------------|
| `<.class1>:child(1)`    | 型が`class1`である最初の要素を選択し、最初の子要素を選択する                                              |
| `<#id1>:next`           | resource-idが"id1"である最初の要素を選択し、次の要素を選択する                                         |
| `<text1>:previous`      | textが"text1"である最初の要素を選択し、前の要素を選択する                                              |
| `<@access1>:next:next`  | accessibility(content-desc または name) が "access1"である最初の要素を選択し、次の要素を選択し、次の要素を選択する |
| `<@access1>:next(2)`    | `<@access1>:next:next`と同じ                                                       |
| `<text1>:next(.class1)` | textが"text1"である最初の要素を選択し、型が"class1"である次の要素を選択する                                 |
| `<text1>:nextInput`     | textが"text1"である最初の要素を選択し、 次のinput要素を選択する                                        |

### Link

- [相対セレクター (方向ベース)](relative_selector_direction_ja.md)

- [相対セレクター (ウィジェットフローベース)](relative_selector_flow_ja.md)

- [相対セレクター](relative_selector_ja.md)


- [index](../../../index_ja.md)