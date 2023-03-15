# select

You can select an element using these functions.

[Selector expression](../../selector_and_nickname/selector_expression.md) is accepted as argument.

The function returns `TestElement` object.

## Functions

| function              | description                                                             |
|:----------------------|:------------------------------------------------------------------------|
| select                | Finds the first element that matches the selector in current screen.    |
| selectWithScrollDown  | Finds the first element that matches the selector with scrolling down.  |
| selectWithScrollUp    | Finds the first element that matches the selector with scrolling up.    |
| selectWithScrollRight | Finds the first element that matches the selector with scrolling right. |
| selectWithScrollLeft  | Finds the first element that matches the selector with scrolling left.  |
| selectInScanResults   | Finds the first element that matches the selector in scan results.      |

## Example 1: select

### Select1.kt

(`kotlin/tutorial/basic/Select1.kt`)

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
lineNo	logDateTime	testCaseId	logType	os	special	group	message	level	command	subject	arg1	arg2	result
123	2022/09/27 14:02:35.612	{select}	[SCENARIO]			()	select()	0	scenario	select	select()	10	NONE
124	2022/09/27 14:02:35.613	{select-1}	[CASE]			()	(1)	0	case	select-1	1		NONE
125	2022/09/27 14:02:35.613	{select-1}	[ACTION]			()	action	0	action				NONE
126	2022/09/27 14:02:35.616	{select-1}	[select]			(select)	select <Settings>	1	select	<Settings>			NONE
127	2022/09/27 14:02:35.621	{select-1}	[output]			(output)	<android.widget.TextView index='1' class='android.widget.TextView' resource-id='com.android.settings:id/homepage_title' text='Settings' content-desc='' checked='false' focusable='false' focused='false' selected='false' scrollable='false' bounds=[66,352][428,484]>	1	output	<android.widget.TextView index='1' class='android.widget.TextView' resource-id='com.android.settings:id/homepage_title' text='Settings' content-desc='' checked='false' focusable='false' focused='false' selected='false' scrollable='false' bounds=[66,352][428,484]>			NONE
128	2022/09/27 14:02:36.260	{select-1}	[screenshot]			()	screenshot	0	screenshot	128.png			NONE
129	2022/09/27 14:02:36.262	{select-2}	[CASE]			()	(2)	0	case	select-2	2		NONE
130	2022/09/27 14:02:36.262	{select-2}	[ACTION]			()	action	0	action				NONE
131	2022/09/27 14:02:36.265	{select-2}	[select]			(selectWithScrollDown)	selectWithScrollDown <System>	1	selectWithScrollDown	<System>			NONE
132	2022/09/27 14:02:36.276	{select-2}	[operate]			(selectWithScrollDown)	Scroll down	2	scrollDown	scrollDown			NONE
137	2022/09/27 14:02:40.089	{select-2}	[screenshot]			(selectWithScrollDown)	screenshot	2	screenshot	137.png			NONE
140	2022/09/27 14:02:40.138	{select-2}	[operate]			(selectWithScrollDown)	Scroll down	2	scrollDown	scrollDown			NONE
145	2022/09/27 14:02:43.498	{select-2}	[screenshot]			(selectWithScrollDown)	screenshot	2	screenshot	145.png			NONE
146	2022/09/27 14:02:43.533	{select-2}	[output]			(output)	<android.widget.TextView index='0' class='android.widget.TextView' resource-id='android:id/title' text='System' content-desc='' checked='false' focusable='false' focused='false' selected='false' scrollable='false' bounds=[198,1696][380,1770]>	1	output	<android.widget.TextView index='0' class='android.widget.TextView' resource-id='android:id/title' text='System' content-desc='' checked='false' focusable='false' focused='false' selected='false' scrollable='false' bounds=[198,1696][380,1770]>			NONE
147	2022/09/27 14:02:43.534	{select-3}	[CASE]			()	(3)	0	case	select-3	3		NONE
148	2022/09/27 14:02:43.535	{select-3}	[ACTION]			()	action	0	action				NONE
149	2022/09/27 14:02:43.536	{select-3}	[select]			(selectWithScrollUp)	selectWithScrollUp <Settings>	1	selectWithScrollUp	<Settings>			NONE
150	2022/09/27 14:02:43.537	{select-3}	[operate]			(selectWithScrollUp)	Scroll up	2	scrollUp	scrollUp			NONE
155	2022/09/27 14:02:47.269	{select-3}	[screenshot]			(selectWithScrollUp)	screenshot	2	screenshot	155.png			NONE
158	2022/09/27 14:02:47.319	{select-3}	[operate]			(selectWithScrollUp)	Scroll up	2	scrollUp	scrollUp			NONE
163	2022/09/27 14:02:50.575	{select-3}	[screenshot]			(selectWithScrollUp)	screenshot	2	screenshot	163.png			NONE
164	2022/09/27 14:02:50.621	{select-3}	[output]			(output)	<android.widget.TextView index='1' class='android.widget.TextView' resource-id='com.android.settings:id/homepage_title' text='Settings' content-desc='' checked='false' focusable='false' focused='false' selected='false' scrollable='false' bounds=[66,352][428,484]>	1	output	<android.widget.TextView index='1' class='android.widget.TextView' resource-id='com.android.settings:id/homepage_title' text='Settings' content-desc='' checked='false' focusable='false' focused='false' selected='false' scrollable='false' bounds=[66,352][428,484]>			NONE
165	2022/09/27 14:02:50.622	{select-3}	[warn]			()	No test result found. Use assertion function in expectation block.	0					NONE
```

## Example 2: scanElements

### Select1.kt

(`kotlin/tutorial/basic/Select1.kt`)

```kotlin
@Test
@Order(20)
fun selectInScanElements() {

    scenario {
        case(1) {
            action {
                it.scanElements()
                    .selectInScanResults("Settings", log = true)
                    .selectInScanResults("Accessibility", log = true)
                    .selectInScanResults("System", log = true)
            }
        }
    }
}
```

#### Note

`log = true` is specified for demonstration. This should not be specified in production code. Default is false.

### TestLog(simple)

```
lineNo	logDateTime	testCaseId	logType	os	special	group	message	level	command	subject	arg1	arg2	result
123	2022/09/27 14:05:24.187	{selectInScanElements}	[SCENARIO]			()	selectInScanElements()	0	scenario	selectInScanElements	selectInScanElements()	20	NONE
124	2022/09/27 14:05:24.187	{selectInScanElements-1}	[CASE]			()	(1)	0	case	selectInScanElements-1	1		NONE
125	2022/09/27 14:05:24.188	{selectInScanElements-1}	[ACTION]			()	action	0	action				NONE
126	2022/09/27 14:05:24.689	{selectInScanElements-1}	[screenshot]			()	screenshot	0	screenshot	126.png			NONE
127	2022/09/27 14:05:24.693	{selectInScanElements-1}	[operate]			(scanElements)	Scan elements (direction=Down)	1	scanElements	scanElements			NONE
140	2022/09/27 14:05:33.054	{selectInScanElements-1}	[screenshot]			(scanElements)	screenshot	1	screenshot	140.png			NONE
141	2022/09/27 14:05:33.059	{selectInScanElements-1}	[select]			(selectInScanResults)	selectInScanResults <Settings>	1	selectInScanResults	<Settings>			NONE
142	2022/09/27 14:05:34.908	{selectInScanElements-1}	[select]			(selectInScanResults)	selectInScanResults <Accessibility>	1	selectInScanResults	<Accessibility>			NONE
143	2022/09/27 14:05:34.915	{selectInScanElements-1}	[select]			(selectInScanResults)	selectInScanResults <System>	1	selectInScanResults	<System>			NONE
144	2022/09/27 14:05:34.916	{selectInScanElements-1}	[warn]			()	No test result found. Use assertion function in expectation block.	0					NONE
```

### Link

- [index](../../../index.md)
