# tap

You can tap an element on the screen using these functions.

## Functions

| function          | description                                                   |
|:------------------|:--------------------------------------------------------------|
| tap               | Tap an element that matches the selector in current screen.   |
| tapWithScrollDown | Tap an element that matches the selector with scrolling down. |
| tapWithScrollUp   | Tap an element that matches the selector with scrolling up.   |

## Example

### Tap1.kt

```kotlin
@Test
fun tap() {

    scenario {
        case(1) {
            action {
                it.tap("Network & internet")
                    .tap("Internet")
                it.tap("@Navigate up")
                    .tap("@Navigate up")
            }
        }
        case(2) {
            action {
                it.tapWithScrollDown("Display")
                    .tapWithScrollDown("Colors")
                it.tap("@Navigate up")
                    .tap("@Navigate up")
            }
        }
    }
}
```

### TestLog(simple)

```
116	2022/06/06 23:24:11.977	{tap}	[SCENARIO]			()	tap()	0	scenario	tap	tap()		NOTIMPL
117	2022/06/06 23:24:11.978	{tap-1}	[CASE]			()	(1)	0	case	tap-1	1		NOTIMPL
118	2022/06/06 23:24:11.978	{tap-1}	[ACTION]			()	action	0	action	118.png			NONE
119	2022/06/06 23:24:12.427	{tap-1}	[operate]			(tap)	Tap <Network & internet>	1	tap	119.png			NONE
124	2022/06/06 23:24:18.109	{tap-1}	[operate]			(tap)	Tap <Internet>	1	tap	124.png			NONE
129	2022/06/06 23:24:24.022	{tap-1}	[operate]			(tap)	Tap <@Navigate up>	1	tap	129.png			NONE
134	2022/06/06 23:24:26.520	{tap-1}	[operate]			(tap)	Tap <@Navigate up>	1	tap	134.png			NONE
139	2022/06/06 23:24:32.555	{tap-1}	[NOTIMPL]			()	No test result found. Use assertion function in expectation block.	0	notImpl				NOTIMPL

140	2022/06/06 23:24:32.555	{tap-2}	[CASE]			()	(2)	0	case	tap-2	2		NOTIMPL
141	2022/06/06 23:24:32.555	{tap-2}	[ACTION]			()	action	0	action				NONE
142	2022/06/06 23:24:32.556	{tap-2}	[operate]			(tapWithScrollDown)	Tap <Display> (scroll down)	1	tapWithScrollDown	142.png			NONE
149	2022/06/06 23:24:40.528	{tap-2}	[screenshot]			(tapWithScrollDown)	Tap <Display> (scroll down)	1	screenshot	149.png			NONE
150	2022/06/06 23:24:40.746	{tap-2}	[operate]			(tapWithScrollDown)	Tap <Colors> (scroll down)	1	tapWithScrollDown	150.png			NONE
157	2022/06/06 23:24:48.853	{tap-2}	[screenshot]			(tapWithScrollDown)	Tap <Colors> (scroll down)	1	screenshot	157.png			NONE
158	2022/06/06 23:24:49.492	{tap-2}	[operate]			(tap)	Tap <@Navigate up>	1	tap	158.png			NONE
163	2022/06/06 23:24:51.423	{tap-2}	[operate]			(tap)	Tap <@Navigate up>	1	tap	163.png			NONE
168	2022/06/06 23:24:57.281	{tap-2}	[NOTIMPL]			()	No test result found. Use assertion function in expectation block.	0	notImpl				NOTIMPL
```

### Link

- [index](../../../index.md)
