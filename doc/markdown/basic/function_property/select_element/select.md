# select

You can select an element using these functions.

[Selector expression](../../selector_and_nickname/selector_expression.md) is accepted as argument.

The function returns `TestElement` object.

## Functions

| function             | description                                                     |
|:---------------------|:----------------------------------------------------------------|
| select               | Finds an element that matches the selector in current screen.   |
| selectWithScrollDown | Finds an element that matches the selector with scrolling down. |
| selectWithScrollUp   | Finds an element that matches the selector with scrolling up.   |
| selectInScanResults  | Finds an element that matches the selector in scan results.     |

## Example 1: select

### Select1.kt

```kotlin
@Test
@Order(10)
fun select() {

    scenario {
        case(1) {
            action {
                it.select("Settings", log = true)
                output(it)
            }
        }
        case(2) {
            action {
                it.selectWithScrollDown("System", log = true)
                output(it)
            }
        }
        case(3) {
            action {
                it.selectWithScrollUp("Settings", log = true)
                output(it)
            }
        }
    }
}
```

#### Note

`log = true` is specified for demonstration. This should not be specified in production code. Default is false.

### TestLog(simple)

```
116	2022/06/06 23:07:37.945	{select}	[SCENARIO]			()	select()	0	scenario	select	select()	10	NOTIMPL
117	2022/06/06 23:07:37.945	{select-1}	[CASE]			()	(1)	0	case	select-1	1		NOTIMPL
118	2022/06/06 23:07:37.945	{select-1}	[ACTION]			()	action	0	action				NONE
119	2022/06/06 23:07:37.947	{select-1}	[select]			(select)	select <Settings>	1	select	<Settings>			NONE
120	2022/06/06 23:07:37.952	{select-1}	[output]			(output)	<android.widget.TextView index='1' class='android.widget.TextView' resource-id='com.android.settings:id/homepage_title' text='Settings' content-desc='' checked='false' focusable='false' focused='false' selected='false' scrollable='false' bounds=[66,352][428,484]>	1	output	120.png			NONE
121	2022/06/06 23:07:38.389	{select-1}	[NOTIMPL]			()	No test result found. Use assertion function in expectation block.	0	notImpl				NOTIMPL

122	2022/06/06 23:07:38.389	{select-2}	[CASE]			()	(2)	0	case	select-2	2		NOTIMPL
123	2022/06/06 23:07:38.389	{select-2}	[ACTION]			()	action	0	action				NONE
124	2022/06/06 23:07:38.390	{select-2}	[select]			(selectWithScrollDown)	selectWithScrollDown <System>	1	selectWithScrollDown	<System>			NONE
125	2022/06/06 23:07:38.403	{select-2}	[operate]			(selectWithScrollDown)	Scroll down	3	scrollDown	125.png			NONE
132	2022/06/06 23:07:41.868	{select-2}	[operate]			(selectWithScrollDown)	Scroll down	3	scrollDown	132.png			NONE
139	2022/06/06 23:07:45.091	{select-2}	[operate]			(selectWithScrollDown)	Scroll down	3	scrollDown	139.png			NONE
144	2022/06/06 23:07:48.265	{select-2}	[output]			(output)	<android.widget.TextView index='0' class='android.widget.TextView' resource-id='android:id/title' text='System' content-desc='' checked='false' focusable='false' focused='false' selected='false' scrollable='false' bounds=[198,1487][380,1561]>	1	output	<android.widget.TextView index='0' class='android.widget.TextView' resource-id='android:id/title' text='System' content-desc='' checked='false' focusable='false' focused='false' selected='false' scrollable='false' bounds=[198,1487][380,1561]>			NONE
145	2022/06/06 23:07:48.265	{select-2}	[NOTIMPL]			()	No test result found. Use assertion function in expectation block.	0	notImpl				NOTIMPL

146	2022/06/06 23:07:48.266	{select-3}	[CASE]			()	(3)	0	case	select-3	3		NOTIMPL
147	2022/06/06 23:07:48.267	{select-3}	[ACTION]			()	action	0	action				NONE
148	2022/06/06 23:07:48.267	{select-3}	[select]			(selectWithScrollUp)	selectWithScrollUp <Settings>	1	selectWithScrollUp	<Settings>			NONE
149	2022/06/06 23:07:48.268	{select-3}	[operate]			(selectWithScrollUp)	Scroll up	3	scrollUp	149.png			NONE
156	2022/06/06 23:07:51.546	{select-3}	[operate]			(selectWithScrollUp)	Scroll up	3	scrollUp	156.png			NONE
161	2022/06/06 23:07:54.624	{select-3}	[output]			(output)	<android.widget.TextView index='1' class='android.widget.TextView' resource-id='com.android.settings:id/homepage_title' text='Settings' content-desc='' checked='false' focusable='false' focused='false' selected='false' scrollable='false' bounds=[66,352][428,484]>	1	output	<android.widget.TextView index='1' class='android.widget.TextView' resource-id='com.android.settings:id/homepage_title' text='Settings' content-desc='' checked='false' focusable='false' focused='false' selected='false' scrollable='false' bounds=[66,352][428,484]>			NONE
162	2022/06/06 23:07:54.624	{select-3}	[NOTIMPL]			()	No test result found. Use assertion function in expectation block.	0	notImpl				NOTIMPL
```

## Example 2: scanElements

### Select1.kt

```kotlin
@Test
@Order(20)
fun selectInScanElements() {

    scenario {
        case(1) {
            condition {
                it.scanElements()
            }.action {
                it.selectInScanResults("Settings", log = true)
                it.selectInScanResults("Accessibility", log = true)
                it.selectInScanResults("System", log = true)
            }
        }
    }
}
```

#### Note

`log = true` is specified for demonstration. This should not be specified in production code. Default is false.

### TestLog(simple)

```
116	2022/06/06 23:11:06.872	{selectInScanElements}	[SCENARIO]			()	selectInScanElements()	0	scenario	selectInScanElements	selectInScanElements()	20	NOTIMPL
117	2022/06/06 23:11:06.873	{selectInScanElements-1}	[CASE]			()	(1)	0	case	selectInScanElements-1	1		NOTIMPL
118	2022/06/06 23:11:06.873	{selectInScanElements-1}	[CONDITION]			()	condition	0	condition	118.png			NONE
119	2022/06/06 23:11:07.325	{selectInScanElements-1}	[operate]			(scanElements)	Scan elements (direction=Down)	1	scanElements	119.png			NONE
132	2022/06/06 23:11:15.527	{selectInScanElements-1}	[ACTION]			()	action	0	action				NONE
133	2022/06/06 23:11:15.528	{selectInScanElements-1}	[select]			(selectInScanResults)	selectInScanResults <Settings>	1	selectInScanResults	<Settings>			NONE
134	2022/06/06 23:11:15.528	{selectInScanElements-1}	[select]			(selectInScanResults)	selectInScanResults <Accessibility>	1	selectInScanResults	<Accessibility>			NONE
135	2022/06/06 23:11:15.528	{selectInScanElements-1}	[select]			(selectInScanResults)	selectInScanResults <System>	1	selectInScanResults	<System>			NONE
136	2022/06/06 23:11:15.529	{selectInScanElements-1}	[NOTIMPL]			()	No test result found. Use assertion function in expectation block.	0	notImpl				NOTIMPL
```

### Link

- [index](../../../index.md)
