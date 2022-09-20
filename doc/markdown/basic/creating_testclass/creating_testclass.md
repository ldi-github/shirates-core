# Creating TestClass

## UITest class

1. Create a package directory `src/test/kotlin/exercise`.

   ![](../_images/creating_package.png)


2. Create a kotlin class file `TestClass1`.

   ![](../_images/creating_testclass.png)

   ![](../_images/creating_testclass_2.png)

   ![](../_images/creating_testclass_3.png)


3. Type `" : UITest()"` after class name, move mouse cursor to it, then click `import` on context menu. <br>Shortcut is
   useful. (Mac: option+Enter, Windows: Alt+Enter)

   ![](../_images/import_package.png)

```kotlin
package exercise

import shirates.core.testcode.UITest

class TestClass1 : UITest() {
}
```

## @Testrun annotation

**Testrun file** that describes test configuration information is required to initialize test session.
Put **@Testrun** annotation on the class to indicate which **testrun file** to be applied.

See also [Test Configuration template](../creating_project/test_configuration_template.md)

```kotlin
package exercise

import shirates.core.configuration.Testrun
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class TestClass1 : UITest() {
}
```

You can put `testrun.properties` file under `testConfig` directory, and this file is recognize as default.
You can omit putting `@Testrun` annotation on each Test class.

![](../_images/testrun_file_default.png)

## @Test annotation

Create a function (`testMethod1`).

```kotlin
@Testrun("testConfig/android/androidSettings/testrun.properties")
class TestClass1 : UITest() {

    fun testMethod1() {

    }
}
```

Put **@Test** annotation of **JUnit 5** (**org.junit.jupiter.api.Test**)

![](../_images/select_test_annotation.png)

```kotlin
package exercise

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class TestClass1 : UITest() {

    @Test
    fun testMethod1() {

    }
}
```

## Running test

At this point, you can run `testMethod1`.

1. Run Android 12 device.
    - You can use Android 12 AVD created in **Create AVD for demo** in [Quick Start](../../quick-start.md)
    - Or you can connect Android 12 real device to your machine and enable **USB debugging**.
1. Set IntelliJ IDEA preferences.
    - See [Enable right-click test running](../../tool_settings/right_click_test_running.md)
1. Right-click in testMethod1 and select `Debug`.

### Console

```
lineNo	logDateTime	testCaseId	logType	group	message
1	2022/06/05 09:06:31.726	{}	[-]	()	----------------------------------------------------------------------------------------------------
2	2022/06/05 09:06:31.731	{}	[-]	()	///
3	2022/06/05 09:06:31.731	{}	[-]	()	/// Shirates Test Framework 0.8.0-SNAPSHOT
4	2022/06/05 09:06:31.731	{}	[-]	()	///
5	2022/06/05 09:06:31.731	{}	[-]	()	powered by Appium (io.appium:java-client:8.1.0)
6	2022/06/05 09:06:31.732	{}	[-]	()	----------------------------------------------------------------------------------------------------
7	2022/06/05 09:06:31.732	{}	[info]	(parameter)	testClass: exercise.TestClass1
8	2022/06/05 09:06:31.732	{}	[info]	(parameter)	sheetName: TestClass1
9	2022/06/05 09:06:31.732	{}	[info]	(parameter)	logLanguage: 
10	2022/06/05 09:06:31.934	{}	[info]	()	Initializing with testRun.properties.(testConfig/android/androidSettings/testrun.properties)
ERROR StatusLogger Log4j2 could not find a logging implementation. Please add log4j-core to the classpath. Using SimpleLogger to log to the console...
11	2022/06/05 09:06:32.304	{}	[info]	()	Logging to file:////Users/wave1008/Downloads/TestResults/androidSettingsConfig/2022-06-05_090631/TestClass1/
12	2022/06/05 09:06:32.305	{}	[info]	()	Loading config.(configFile=/Users/wave1008/Downloads/Practice1/testConfig/android/androidSettings/androidSettingsConfig.json, profileName=Android 12)
13	2022/06/05 09:06:32.333	{}	[warn]	()	screens directory not found. (/Users/wave1008/Downloads/Practice1/testConfig/android/androidSettings/screens)
14	2022/06/05 09:06:32.334	{}	[warn]	()	attributes directory not found. (/Users/wave1008/Downloads/Practice1/testConfig/android/androidSettings/attributes)
15	2022/06/05 09:06:32.342	{}	[info]	()	Scanning under /Users/wave1008/Downloads/Practice1/src/test/kotlin
16	2022/06/05 09:06:32.344	{}	[info]	()	Initializing TestDriver.(profileName='Android 12')
17	2022/06/05 09:06:32.344	{}	[info]	()	noLoadRun: false
18	2022/06/05 09:06:32.344	{}	[info]	()	boundsToRectRatio: 1
19	2022/06/05 09:06:32.345	{}	[info]	()	reuseDriver: true
20	2022/06/05 09:06:32.345	{}	[info]	()	autoScreenshot: true
21	2022/06/05 09:06:32.345	{}	[info]	()	onChangedOnly: true
22	2022/06/05 09:06:32.345	{}	[info]	()	onCondition: true
23	2022/06/05 09:06:32.345	{}	[info]	()	onExpectation: true
24	2022/06/05 09:06:32.346	{}	[info]	()	onExecOperateCommand: true
25	2022/06/05 09:06:32.346	{}	[info]	()	onCheckCommand: true
26	2022/06/05 09:06:32.346	{}	[info]	()	onScrolling: true
27	2022/06/05 09:06:32.346	{}	[info]	()	manualScreenshot: true
28	2022/06/05 09:06:32.346	{}	[info]	()	retryMaxCount: 1
29	2022/06/05 09:06:32.346	{}	[info]	()	retryIntervalSeconds: 2.0
30	2022/06/05 09:06:32.347	{}	[info]	()	shortWaitSeconds: 1.5
31	2022/06/05 09:06:32.347	{}	[info]	()	waitSecondsOnIsScreen: 15.0
32	2022/06/05 09:06:32.347	{}	[info]	()	waitSecondsForAnimationComplete: 0.5
33	2022/06/05 09:06:32.347	{}	[info]	()	waitSecondsForConnectionEnabled: 8.0
34	2022/06/05 09:06:32.347	{}	[info]	()	swipeDurationSeconds: 3.0
35	2022/06/05 09:06:32.347	{}	[info]	()	flickDurationSeconds: 0.3
36	2022/06/05 09:06:32.348	{}	[info]	()	swipeMarginRatio: 0.1
37	2022/06/05 09:06:32.348	{}	[info]	()	scrollVerticalMarginRatio: 0.2
38	2022/06/05 09:06:32.348	{}	[info]	()	scrollHorizontalMarginRatio: 0.25
39	2022/06/05 09:06:32.348	{}	[info]	()	tapHoldSeconds: 0.1
40	2022/06/05 09:06:32.348	{}	[info]	()	tapAppIconMethod: auto
41	2022/06/05 09:06:32.349	{}	[info]	()	tapAppIconMacro: 
42	2022/06/05 09:06:32.349	{}	[info]	()	syncWaitSeconds: 1.8
43	2022/06/05 09:06:32.350	{}	[info]	()	appium --session-override --relaxed-security --log /Users/wave1008/Downloads/TestResults/androidSettingsConfig/2022-06-05_090631/TestClass1/appium_2022-06-05_090632350.log --port 4720
44	2022/06/05 09:06:34.487	{}	[info]	()	Appium Server started. (pid=81915, port=4720)
45	2022/06/05 09:06:34.493	{}	[info]	()	Connecting to Appium Server.(http://127.0.0.1:4720/)
SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
SLF4J: Defaulting to no-operation (NOP) logger implementation
SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.
Cleaning up unclosed ZipFile for archive /Users/wave1008/Downloads/TestResults/androidSettingsConfig/TestList_androidSettingsConfig.xlsx
46	2022/06/05 09:06:39.102	{}	[info]	()	[Health check] start
47	2022/06/05 09:06:39.104	{}	[info]	(syncCache)	Syncing (1)
48	2022/06/05 09:06:39.821	{}	[info]	(syncCache)	elapsed=0.717, syncWaitSeconds=1.8
49	2022/06/05 09:06:40.326	{}	[info]	(syncCache)	Syncing (2)
50	2022/06/05 09:06:40.423	{}	[info]	(syncCache)	Synced. (elapsed=1.319, currentScreen=?)
51	2022/06/05 09:06:40.632	{}	[info]	()	[Health check] end
52	2022/06/05 09:06:40.638	{}	[info]	()	implicitlyWaitSeconds: 5.0
53	2022/06/05 09:06:40.724	{}	[info]	()	(settings) always_finish_activities: 0
54	2022/06/05 09:06:40.753	{}	[info]	()	AppiumDriver initialized.
55	2022/06/05 09:06:40.753	{}	[info]	(parameter)	testrun: testConfig/android/androidSettings/testrun.properties
56	2022/06/05 09:06:40.753	{}	[info]	(parameter)	testConfigName: androidSettingsConfig(/Users/wave1008/Downloads/Practice1/testConfig/android/androidSettings/androidSettingsConfig.json)
57	2022/06/05 09:06:40.754	{}	[info]	(parameter)	profileName: Android 12
58	2022/06/05 09:06:40.754	{}	[info]	(parameter)	appIconName: Settings
59	2022/06/05 09:06:40.754	{}	[-]	()	(capabilities)
60	2022/06/05 09:06:40.755	{}	[info]	(parameter)	appium:newCommandTimeout: 300
61	2022/06/05 09:06:40.755	{}	[info]	(parameter)	appium:takesScreenshot: true
62	2022/06/05 09:06:40.755	{}	[info]	(parameter)	appium:warnings: {}
63	2022/06/05 09:06:40.755	{}	[info]	(parameter)	appium:deviceApiLevel: 31
64	2022/06/05 09:06:40.755	{}	[info]	(parameter)	appium:automationName: UiAutomator2
65	2022/06/05 09:06:40.755	{}	[info]	(parameter)	appium:locationContextEnabled: false
66	2022/06/05 09:06:40.756	{}	[info]	(parameter)	appium:deviceScreenSize: 1080x2220
67	2022/06/05 09:06:40.756	{}	[info]	(parameter)	appium:deviceManufacturer: Google
68	2022/06/05 09:06:40.756	{}	[info]	(parameter)	appium:pixelRatio: 2.75
69	2022/06/05 09:06:40.756	{}	[info]	(parameter)	platformName: android
70	2022/06/05 09:06:40.756	{}	[info]	(parameter)	appium:networkConnectionEnabled: true
71	2022/06/05 09:06:40.756	{}	[info]	(parameter)	appium:locale: US
72	2022/06/05 09:06:40.756	{}	[info]	(parameter)	appium:deviceScreenDensity: 440
73	2022/06/05 09:06:40.757	{}	[info]	(parameter)	appium:viewportRect: {left=0, top=66, width=1080, height=2022}
74	2022/06/05 09:06:40.757	{}	[info]	(parameter)	appium:language: en
75	2022/06/05 09:06:40.757	{}	[info]	(parameter)	appium:deviceModel: sdk_gphone64_arm64
76	2022/06/05 09:06:40.757	{}	[info]	(parameter)	appium:platformVersion: 12
77	2022/06/05 09:06:40.757	{}	[info]	(parameter)	appium:databaseEnabled: false
78	2022/06/05 09:06:40.758	{}	[info]	(parameter)	appium:deviceUDID: emulator-5554
79	2022/06/05 09:06:40.758	{}	[info]	(parameter)	appium:statBarHeight: 66
80	2022/06/05 09:06:40.758	{}	[info]	(parameter)	appium:webStorageEnabled: false
81	2022/06/05 09:06:40.758	{}	[info]	(parameter)	appium:appActivity: com.android.settings.Settings
82	2022/06/05 09:06:40.758	{}	[info]	(parameter)	appium:deviceName: emulator-5554
83	2022/06/05 09:06:40.758	{}	[info]	(parameter)	appium:javascriptEnabled: true
84	2022/06/05 09:06:40.758	{}	[info]	(parameter)	appium:appPackage: com.android.settings
85	2022/06/05 09:06:40.759	{}	[-]	()	settings
86	2022/06/05 09:06:40.827	{}	[info]	(parameter)	always_finish_activities: 0
87	2022/06/05 09:06:40.828	{}	[-]	()	(others)
88	2022/06/05 09:06:40.828	{}	[info]	(parameter)	isEmulator: true
89	2022/06/05 09:06:40.829	{}	[info]	(parameter)	hasOsaifuKeitai: false
90	2022/06/05 09:06:40.835	{}	[info]	()	Logging to file:////Users/wave1008/Downloads/TestResults/androidSettingsConfig/2022-06-05_090631/TestClass1/
Copying jar content _ReportScript.js to /Users/wave1008/Downloads/TestResults/androidSettingsConfig/2022-06-05_090631/TestClass1
Copying jar content _ReportStyle.css to /Users/wave1008/Downloads/TestResults/androidSettingsConfig/2022-06-05_090631/TestClass1
No scenario found. Outputting Spec-Report skipped.
Cleaning up unclosed ZipFile for archive /Users/wave1008/Downloads/TestResults/androidSettingsConfig/TestList_androidSettingsConfig.xlsx
91	2022/06/05 09:06:41.020	{}	[info]	()	Quitting TestDriver.
Disconnected from the target VM, address: 'localhost:52576', transport: 'socket'
Cleaning up unclosed ZipFile for archive /Users/wave1008/Downloads/TestResults/androidSettingsConfig/TestList_androidSettingsConfig.xlsx
BUILD SUCCESSFUL in 11s
2 actionable tasks: 2 executed
9:06:41: Execution finished ':test --tests "exercise.TestClass1.testMethod1"'.
```

### If you encounter error

see [Error messages](../../troubleshooting/error_warning_messages.md)

## @Order annotation

You can use **@Order** annotation of JUnit 5 to indicate execution order you want.

## @DisplayName annotation

You can use **@DisplayName** annotation of JUnit 5 to describe test scenario.

### Example

```kotlin
package exercise

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class TestClass1 : UITest() {

    @Order(1)
    @DisplayName("Test scenario C")
    @Test
    fun testScenarioC() {

    }

    @Order(2)
    @DisplayName("Test scenario B")
    @Test
    fun testScenarioB() {

    }

    @Order(3)
    @DisplayName("Test scenario A")
    @Test
    fun testScenarioA() {

    }
}
```

![](../_images/order_display_annotation.png)

## @Nested support?

No. Do not use @Nested. This limitation is of software design of Shirates test framework.

### Link

- [index](../../index.md)

