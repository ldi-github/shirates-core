# Relative selector(Direction based)

## Relative selectors

| relative selector | description                       |
|:------------------|:----------------------------------|
| :right            | element in right direction        |
| :rightInput       | input element in right direction  |
| :rightLabel       | label element in right direction  |
| :rightImage       | image element in right direction  |
| :rightButton      | button element in right direction |
| :rightSwitch      | switch element in right direction |
| :below            | element in below direction        |
| :belowInput       | input element in below direction  |
| :belowLabel       | label element in below direction  |
| :belowImage       | image element in below direction  |
| :belowButton      | button element in below direction |
| :belowSwitch      | switch element in below direction |
| :left             | element in left direction         |
| :leftInput        | input element in left direction   |
| :leftLabel        | label element in left direction   |
| :leftImage        | image element in left direction   |
| :leftButton       | button element in left direction  |
| :leftSwitch       | switch element in left direction  |
| :above            | element in above direction        |
| :aboveInput       | input element in above direction  |
| :aboveLabel       | label element in above direction  |
| :aboveImage       | image element in above direction  |
| :aboveButton      | button element in above direction |
| :aboveSwitch      | switch element in above direction |

## Selector command examples

| example                     | description                                                                                                                                                            |
|:----------------------------|:-----------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `<text1>:right`             | Select the first element that text is "text1", then select the first element in right direction.                                                                       |
| `<text1>:right(2)`          | Select the first element that text is "text1", then select the second element in right direction. This is equivalent to `<text1>:right(pos=2)` or `<text1>:right([2])` |
| `<text1>:rightSwitch`       | Select the first element that text is "text1", then select the first switch element in right direction.                                                                |
| `<text1>:right(text2)`      | Select the first element that text is "text1", then select the first element that text is "text2" in right direction.                                                  |
| `<text1>:right:belowButton` | Select the first element that text is "text1", then select the first element in right direction, then select the 1st button element in below direction.                |

<br>

## Direction

right, below, left, above

![direction](../../_images/direction_4way.png)

<br>

### Usage of right selector (Android)

from TextView1

![direction right android 1](../../_images/direction_right_android_1.png)

![direction right android 2](../../_images/direction_right_android_2.png)

<br>

### Usage of right selector (iOS)

from StaticText1

![direction right ios 1](../../_images/direction_right_ios_1.png)

![direction right ios 2](../../_images/direction_right_ios_2.png)

### Search range

Right selector searches in right direction between the top and the bottom of base element.

![search range](../../_images/direction_search_range.png)

### Link

- [Relative selector (Widget flow based)](relative_selector_flow.md)

- [Relative selector (XML based)](relative_selector_xml.md)

- [Relative selector](relative_selector.md)
