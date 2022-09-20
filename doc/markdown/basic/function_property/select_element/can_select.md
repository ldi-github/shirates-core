# canSelect

You can know if you can select element or not using these functions returns true or false.

## Functions

| function                   | description                                                                              |
|:---------------------------|:-----------------------------------------------------------------------------------------|
| canSelect                  | Finds an element that matches the selector in current screen and returns true/false.     |
| canSelectWithScrollDown    | Finds an element that matches the selector with scrolling down and returns true/false.   |
| canSelectWithScrollUp      | Finds an element that matches the selector with scrolling up and returns true/false.     |
| canSelectAllWithScrollDown | Finds all elements that matches the selector with scrolling down and returns true/false. |
| canSelectAllWithScrollUp   | Finds all elements that matches the selector with scrolling up and returns true/false.   |
| canSelectInScanResults     | Finds an element that matches the selector in scan results and returns true/false.       |
| canSelectAllInScanResults  | Finds all elements that matches the selector in scan results and returns true/false.     |

## Example 1: canSelect

### CanSelect1.kt

```kotlin
@Test
@Order(10)
fun canSelect() {

    scenario {
        case(1) {
            action {
                it.canSelect("Settings", log = true)
            }
        }
        case(2) {
            action {
                it.canSelectWithScrollDown("System", log = true)
            }
        }
        case(3) {
            action {
                it.canSelectWithScrollUp("Settings", log = true)
            }
        }
        case(4) {
            action {
                it.canSelectAllWithScrollDown("Settings", "System", log = true)
            }
        }
        case(5) {
            action {
                it.canSelectAllWithScrollUp("Settings", "System", log = true)
            }
        }
    }
}
```

#### Note

`log = true` is specified for demonstration. This should not be specified in production code. Default is false.

### TestLog(simple)

```
116	2022/06/06 23:17:17.314	{canSelect}	[SCENARIO]			()	canSelect()	0	scenario	canSelect	canSelect()	10	NOTIMPL
117	2022/06/06 23:17:17.314	{canSelect-1}	[CASE]			()	(1)	0	case	canSelect-1	1		NOTIMPL
118	2022/06/06 23:17:17.314	{canSelect-1}	[ACTION]			()	action	0	action				NONE
119	2022/06/06 23:17:17.315	{canSelect-1}	[boolean]			(canSelect)	canSelect <Settings> (result=true)	1	canSelect	119.png			NONE
120	2022/06/06 23:17:17.750	{canSelect-1}	[NOTIMPL]			()	No test result found. Use assertion function in expectation block.	0	notImpl				NOTIMPL

121	2022/06/06 23:17:17.750	{canSelect-2}	[CASE]			()	(2)	0	case	canSelect-2	2		NOTIMPL
122	2022/06/06 23:17:17.750	{canSelect-2}	[ACTION]			()	action	0	action				NONE
123	2022/06/06 23:17:17.752	{canSelect-2}	[boolean]			(canSelectWithScrollDown)	canSelectWithScrollDown <System> (result=true)	1	canSelectWithScrollDown	<System>			NONE
124	2022/06/06 23:17:17.761	{canSelect-2}	[operate]			(canSelectWithScrollDown)	Scroll down	2	scrollDown	124.png			NONE
131	2022/06/06 23:17:21.232	{canSelect-2}	[operate]			(canSelectWithScrollDown)	Scroll down	2	scrollDown	131.png			NONE
138	2022/06/06 23:17:24.428	{canSelect-2}	[operate]			(canSelectWithScrollDown)	Scroll down	2	scrollDown	138.png			NONE
143	2022/06/06 23:17:27.494	{canSelect-2}	[NOTIMPL]			()	No test result found. Use assertion function in expectation block.	0	notImpl				NOTIMPL

144	2022/06/06 23:17:27.494	{canSelect-3}	[CASE]			()	(3)	0	case	canSelect-3	3		NOTIMPL
145	2022/06/06 23:17:27.495	{canSelect-3}	[ACTION]			()	action	0	action				NONE
146	2022/06/06 23:17:27.495	{canSelect-3}	[boolean]			(canSelectWithScrollUp)	canSelectWithScrollUp <Settings> (result=true)	1	canSelectWithScrollUp	<Settings>			NONE
147	2022/06/06 23:17:27.496	{canSelect-3}	[operate]			(canSelectWithScrollUp)	Scroll up	2	scrollUp	147.png			NONE
154	2022/06/06 23:17:30.760	{canSelect-3}	[operate]			(canSelectWithScrollUp)	Scroll up	2	scrollUp	154.png			NONE
159	2022/06/06 23:17:33.972	{canSelect-3}	[NOTIMPL]			()	No test result found. Use assertion function in expectation block.	0	notImpl				NOTIMPL

160	2022/06/06 23:17:33.973	{canSelect-4}	[CASE]			()	(4)	0	case	canSelect-4	4		NOTIMPL
161	2022/06/06 23:17:33.973	{canSelect-4}	[ACTION]			()	action	0	action				NONE
162	2022/06/06 23:17:33.974	{canSelect-4}	[boolean]			(canSelectAllWithScrollDown)	canSelectAllWithScrollDown <Settings>, <System> (result=true)	1	canSelectAllWithScrollDown	<Settings>, <System>			NONE
163	2022/06/06 23:17:33.974	{canSelect-4}	[boolean]			(canSelectAllWithScrollDown)	canSelectWithScrollDown <Settings> (result=true)	2	canSelectWithScrollDown	<Settings>			NONE
164	2022/06/06 23:17:33.974	{canSelect-4}	[boolean]			(canSelectAllWithScrollDown)	canSelectWithScrollDown <System> (result=true)	2	canSelectWithScrollDown	<System>			NONE
165	2022/06/06 23:17:33.975	{canSelect-4}	[operate]			(canSelectAllWithScrollDown)	Scroll down	3	scrollDown	165.png			NONE
172	2022/06/06 23:17:37.349	{canSelect-4}	[operate]			(canSelectAllWithScrollDown)	Scroll down	3	scrollDown	172.png			NONE
179	2022/06/06 23:17:40.567	{canSelect-4}	[operate]			(canSelectAllWithScrollDown)	Scroll down	3	scrollDown	179.png			NONE
184	2022/06/06 23:17:43.658	{canSelect-4}	[NOTIMPL]			()	No test result found. Use assertion function in expectation block.	0	notImpl				NOTIMPL

185	2022/06/06 23:17:43.659	{canSelect-5}	[CASE]			()	(5)	0	case	canSelect-5	5		NOTIMPL
186	2022/06/06 23:17:43.659	{canSelect-5}	[ACTION]			()	action	0	action				NONE
187	2022/06/06 23:17:43.660	{canSelect-5}	[boolean]			(canSelectAllWithScrollUp)	canSelectAllWithScrollUp <Settings>, <System> (result=false)	1	canSelectAllWithScrollUp	<Settings>, <System>			NONE
188	2022/06/06 23:17:43.660	{canSelect-5}	[operate]			(canSelectAllWithScrollUp)	Scroll up	3	scrollUp	188.png			NONE
195	2022/06/06 23:17:46.998	{canSelect-5}	[operate]			(canSelectAllWithScrollUp)	Scroll up	3	scrollUp	195.png			NONE
200	2022/06/06 23:17:50.051	{canSelect-5}	[operate]			(canSelectAllWithScrollUp)	Scroll up	3	scrollUp	scrollUp			NONE
205	2022/06/06 23:17:52.334	{canSelect-5}	[NOTIMPL]			()	No test result found. Use assertion function in expectation block.	0	notImpl				NOTIMPL
```

## Example 2: canSelectInScanElements

### CanSelect1.kt

```kotlin
@Test
@Order(20)
fun canSelectInScanElements() {

    scenario {
        case(1) {
            condition {
                it.scanElements()
            }.action {
                it.canSelectInScanResults("Settings", log = true)
                it.canSelectInScanResults("Accessibility", log = true)
                it.canSelectInScanResults("System", log = true)
                it.canSelectInScanResults("Foo", log = true)
            }
        }
        case(2) {
            action {
                it.canSelectAllInScanResults("Settings", "Accessibility", "System", log = true)
                it.canSelectAllInScanResults("Settings", "Accessibility", "Foo", log = true)
            }
        }
    }
}
```

#### Note

`log = true` is specified for demonstration. This should not be specified in production code. Default is false.

### Link

- [index](../../../index.md)
