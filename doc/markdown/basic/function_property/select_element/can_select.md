# canSelect

You can know whether you can select the element or not using these functions that return true or false.

## Functions

| function                   | description                                                                                    |
|:---------------------------|:-----------------------------------------------------------------------------------------------|
| canSelect                  | Finds the first element that matches the selector in current screen and returns true/false.    |
| canSelectWithScrollDown    | Finds the first element that matches the selector with scrolling down and returns true/false.  |
| canSelectWithScrollUp      | Finds the first element that matches the selector with scrolling up and returns true/false.    |
| canSelectWithScrollRight   | Finds the first element that matches the selector with scrolling right and returns true/false. |
| canSelectWithScrollLeft    | Finds the first element that matches the selector with scrolling left and returns true/false.  |
| canSelectAllWithScrollDown | Finds all elements that matches the selectors with scrolling down and returns true/false.      |
| canSelectAllWithScrollUp   | Finds all elements that matches the selectors with scrolling up and returns true/false.        |
| canSelectInScanResults     | Finds the first element that matches the selector in scan results and returns true/false.      |
| canSelectAllInScanResults  | Finds all elements that matches the selectors in scan results and returns true/false.          |

## Example 1: canSelect

### CanSelect1.kt

(`kotlin/tutorial/basic/CanSelect1.kt`)

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
lineNo	logDateTime	testCaseId	logType	os	special	group	message	level	command	subject	arg1	arg2	result
123	2022/09/27 14:08:27.186	{canSelect}	[SCENARIO]			()	canSelect()	0	scenario	canSelect	canSelect()	10	NONE
124	2022/09/27 14:08:27.186	{canSelect-1}	[CASE]			()	(1)	0	case	canSelect-1	1		NONE
125	2022/09/27 14:08:27.186	{canSelect-1}	[ACTION]			()	action	0	action				NONE
126	2022/09/27 14:08:27.188	{canSelect-1}	[boolean]			(canSelect)	canSelect <Settings> (result=true)	1	canSelect	<Settings>			NONE
127	2022/09/27 14:08:27.692	{canSelect-1}	[screenshot]			()	screenshot	0	screenshot	127.png			NONE
128	2022/09/27 14:08:27.694	{canSelect-2}	[CASE]			()	(2)	0	case	canSelect-2	2		NONE
129	2022/09/27 14:08:27.694	{canSelect-2}	[ACTION]			()	action	0	action				NONE
130	2022/09/27 14:08:27.696	{canSelect-2}	[boolean]			(canSelectWithScrollDown)	canSelectWithScrollDown <System> (result=true)	1	canSelectWithScrollDown	<System>			NONE
131	2022/09/27 14:08:27.706	{canSelect-2}	[operate]			(canSelectWithScrollDown)	Scroll down	2	scrollDown	scrollDown			NONE
136	2022/09/27 14:08:31.507	{canSelect-2}	[screenshot]			(canSelectWithScrollDown)	screenshot	2	screenshot	136.png			NONE
139	2022/09/27 14:08:31.558	{canSelect-2}	[operate]			(canSelectWithScrollDown)	Scroll down	2	scrollDown	scrollDown			NONE
144	2022/09/27 14:08:35.181	{canSelect-2}	[screenshot]			(canSelectWithScrollDown)	screenshot	2	screenshot	144.png			NONE
145	2022/09/27 14:08:35.229	{canSelect-3}	[CASE]			()	(3)	0	case	canSelect-3	3		NONE
146	2022/09/27 14:08:35.230	{canSelect-3}	[ACTION]			()	action	0	action				NONE
147	2022/09/27 14:08:35.231	{canSelect-3}	[boolean]			(canSelectWithScrollUp)	canSelectWithScrollUp <Settings> (result=true)	1	canSelectWithScrollUp	<Settings>			NONE
148	2022/09/27 14:08:35.234	{canSelect-3}	[operate]			(canSelectWithScrollUp)	Scroll up	2	scrollUp	scrollUp			NONE
153	2022/09/27 14:08:38.794	{canSelect-3}	[screenshot]			(canSelectWithScrollUp)	screenshot	2	screenshot	153.png			NONE
156	2022/09/27 14:08:38.836	{canSelect-3}	[operate]			(canSelectWithScrollUp)	Scroll up	2	scrollUp	scrollUp			NONE
161	2022/09/27 14:08:42.485	{canSelect-3}	[screenshot]			(canSelectWithScrollUp)	screenshot	2	screenshot	161.png			NONE
162	2022/09/27 14:08:42.541	{canSelect-4}	[CASE]			()	(4)	0	case	canSelect-4	4		NONE
163	2022/09/27 14:08:42.541	{canSelect-4}	[ACTION]			()	action	0	action				NONE
164	2022/09/27 14:08:42.542	{canSelect-4}	[boolean]			(canSelectAllWithScrollDown)	canSelectAllWithScrollDown <Settings>, <System> (result=true)	1	canSelectAllWithScrollDown	<Settings>, <System>			NONE
165	2022/09/27 14:08:42.543	{canSelect-4}	[boolean]			(canSelectAllWithScrollDown)	canSelectWithScrollDown <Settings> (result=true)	2	canSelectWithScrollDown	<Settings>			NONE
166	2022/09/27 14:08:42.543	{canSelect-4}	[boolean]			(canSelectAllWithScrollDown)	canSelectWithScrollDown <System> (result=true)	2	canSelectWithScrollDown	<System>			NONE
167	2022/09/27 14:08:42.544	{canSelect-4}	[operate]			(canSelectAllWithScrollDown)	Scroll down	3	scrollDown	scrollDown			NONE
172	2022/09/27 14:08:46.240	{canSelect-4}	[screenshot]			(canSelectAllWithScrollDown)	screenshot	3	screenshot	172.png			NONE
175	2022/09/27 14:08:46.279	{canSelect-4}	[operate]			(canSelectAllWithScrollDown)	Scroll down	3	scrollDown	scrollDown			NONE
180	2022/09/27 14:08:50.042	{canSelect-4}	[screenshot]			(canSelectAllWithScrollDown)	screenshot	3	screenshot	180.png			NONE
181	2022/09/27 14:08:50.077	{canSelect-5}	[CASE]			()	(5)	0	case	canSelect-5	5		NONE
182	2022/09/27 14:08:50.078	{canSelect-5}	[ACTION]			()	action	0	action				NONE
183	2022/09/27 14:08:50.083	{canSelect-5}	[boolean]			(canSelectAllWithScrollUp)	canSelectAllWithScrollUp <Settings>, <System> (result=false)	1	canSelectAllWithScrollUp	<Settings>, <System>			NONE
184	2022/09/27 14:08:50.084	{canSelect-5}	[operate]			(canSelectAllWithScrollUp)	Scroll up	3	scrollUp	scrollUp			NONE
189	2022/09/27 14:08:53.654	{canSelect-5}	[screenshot]			(canSelectAllWithScrollUp)	screenshot	3	screenshot	189.png			NONE
192	2022/09/27 14:08:53.694	{canSelect-5}	[operate]			(canSelectAllWithScrollUp)	Scroll up	3	scrollUp	scrollUp			NONE
197	2022/09/27 14:08:57.150	{canSelect-5}	[screenshot]			(canSelectAllWithScrollUp)	screenshot	3	screenshot	197.png			NONE
198	2022/09/27 14:08:57.189	{canSelect-5}	[operate]			(canSelectAllWithScrollUp)	Scroll up	3	scrollUp	scrollUp			NONE
203	2022/09/27 14:08:59.932	{canSelect-5}	[operate]			(canSelectAllWithScrollUp)	Scroll up	3	scrollUp	scrollUp			NONE
208	2022/09/27 14:09:02.462	{canSelect-5}	[warn]			()	No test result found. Use assertion function in expectation block.	0					NONE
```

## Example 2: canSelectInScanElements

### CanSelect1.kt

(`kotlin/tutorial/basic/CanSelect1.kt`)

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
