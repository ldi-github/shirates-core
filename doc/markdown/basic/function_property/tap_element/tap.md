# tap

You can tap an element on the screen using these functions.

## Functions

| function           | description                                                           |
|:-------------------|:----------------------------------------------------------------------|
| tap                | Tap the first element that matches the selector in current screen.    |
| tapWithScrollDown  | Tap the first element that matches the selector with scrolling down.  |
| tapWithScrollUp    | Tap the first element that matches the selector with scrolling up.    |
| tapWithScrollRight | Tap the first element that matches the selector with scrolling right. |
| tapWithScrollLeft  | Tap the first element that matches the selector with scrolling left.  |

## Example

### Tap1.kt

(`kotlin/tutorial/basic/Tap1.kt`)

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
lineNo	logDateTime	testCaseId	logType	os	special	group	message	level	command	subject	arg1	arg2	result
123	2022/09/27 14:28:05.376	{tap}	[SCENARIO]			()	tap()	0	scenario	tap	tap()		NONE
124	2022/09/27 14:28:05.376	{tap-1}	[CASE]			()	(1)	0	case	tap-1	1		NONE
125	2022/09/27 14:28:05.377	{tap-1}	[ACTION]			()	action	0	action				NONE
126	2022/09/27 14:28:05.902	{tap-1}	[screenshot]			()	screenshot	0	screenshot	126.png			NONE
127	2022/09/27 14:28:05.904	{tap-1}	[operate]			(tap)	Tap <Network & internet>	1	tap	<Network & internet>			NONE
132	2022/09/27 14:28:10.536	{tap-1}	[screenshot]			(tap)	screenshot	1	screenshot	132.png			NONE
133	2022/09/27 14:28:10.537	{tap-1}	[operate]			(tap)	Tap <Internet>	1	tap	<Internet>			NONE
138	2022/09/27 14:28:15.062	{tap-1}	[screenshot]			(tap)	screenshot	1	screenshot	138.png			NONE
139	2022/09/27 14:28:15.063	{tap-1}	[operate]			(tap)	Tap <@Navigate up>	1	tap	<@Navigate up>			NONE
144	2022/09/27 14:28:17.502	{tap-1}	[screenshot]			(tap)	screenshot	1	screenshot	144.png			NONE
145	2022/09/27 14:28:17.503	{tap-1}	[operate]			(tap)	Tap <@Navigate up>	1	tap	<@Navigate up>			NONE
150	2022/09/27 14:28:21.760	{tap-1}	[screenshot]			(tap)	screenshot	1	screenshot	150.png			NONE
151	2022/09/27 14:28:21.762	{tap-2}	[CASE]			()	(2)	0	case	tap-2	2		NONE
152	2022/09/27 14:28:21.763	{tap-2}	[ACTION]			()	action	0	action				NONE
153	2022/09/27 14:28:21.764	{tap-2}	[operate]			(tapWithScrollDown)	Tap <Display> (scroll down)	1	tapWithScrollDown	tapWithScrollDown			NONE
156	2022/09/27 14:28:24.823	{tap-2}	[screenshot]			(tapWithScrollDown)	screenshot	1	screenshot	156.png			NONE
161	2022/09/27 14:28:29.292	{tap-2}	[screenshot]			(tapWithScrollDown)	screenshot	1	screenshot	161.png			NONE
162	2022/09/27 14:28:29.294	{tap-2}	[operate]			(tapWithScrollDown)	Tap <Colors> (scroll down)	1	tapWithScrollDown	tapWithScrollDown			NONE
165	2022/09/27 14:28:32.452	{tap-2}	[screenshot]			(tapWithScrollDown)	screenshot	1	screenshot	165.png			NONE
170	2022/09/27 14:28:37.063	{tap-2}	[screenshot]			(tapWithScrollDown)	screenshot	1	screenshot	170.png			NONE
171	2022/09/27 14:28:37.064	{tap-2}	[operate]			(tap)	Tap <@Navigate up>	1	tap	<@Navigate up>			NONE
176	2022/09/27 14:28:39.461	{tap-2}	[screenshot]			(tap)	screenshot	1	screenshot	176.png			NONE
177	2022/09/27 14:28:39.462	{tap-2}	[operate]			(tap)	Tap <@Navigate up>	1	tap	<@Navigate up>			NONE
182	2022/09/27 14:28:43.769	{tap-2}	[screenshot]			(tap)	screenshot	1	screenshot	182.png			NONE
183	2022/09/27 14:28:43.771	{tap-2}	[warn]			()	No test result found. Use assertion function in expectation block.	0					NONE
```

### Link

- [index](../../../index.md)
