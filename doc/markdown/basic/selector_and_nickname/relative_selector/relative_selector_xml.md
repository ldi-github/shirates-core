# Relative selector(XML based)

## Relative selectors

| relative selector | description             |
|:------------------|:------------------------|
| :parent           | parent element          |
| :child            | child element           |
| :sibling          | sibling element         |
| :ancestor         | ancestor element        |
| :descendant       | descendant element      |
| :next             | next element            |
| :previous         | previous element        |
| :nextLabel        | next label element      |
| :preLabel         | previous label element  |
| :nextInput        | next input element      |
| :preInput         | previous input element  |
| :nextImage        | next image element      |
| :preImage         | previous image element  |
| :nextButton       | next button element     |
| :preButton        | previous button element |
| :nextSwitch       | next switch element     |
| :preSwitch        | previous switch element |

## Relative selector examples

| example                 | description                                                                                                                         |
|:------------------------|:------------------------------------------------------------------------------------------------------------------------------------|
| `<.class1>:child(1)`    | Select the first element that type is class1, then select the first child.                                                          |
| `<#id1>:next`           | Select the first element that resource-id is "id1", then select next element.                                                       |
| `<text1>:previous`      | Select the first element that text is "text1", then select previous element.                                                        |
| `<@access1>:next:next`  | Select the first element that accessibility(content-desc or name) is "access1", then select next element, then select next element. |
| `<@access1>:next(2)`    | This is equivalent to `<@access1>:next:next`                                                                                        |
| `<text1>:next(.class1)` | Select the first element that text is "text1", then select next element that class is "class1"                                      |
| `<text1>:nextInput`     | Select the first element that text is "text1", then select next input element.                                                      |

### Link

- [Relative selector (Direction based)](relative_selector_direction.md)

- [Relative selector (Widget flow based)](relative_selector_flow.md)

- [Relative selector](relative_selector.md)


- [index](../../../index.md)