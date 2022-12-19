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

1. Run Android device.
    - You can use Android AVD created in **Create AVD for demo** in [Quick Start](../../quick-start.md)
    - Or you can connect Android real device to your machine and enable **USB debugging**.
1. Set IntelliJ IDEA preferences.
    - See [Enable right-click test running](../../tool_settings/right_click_test_running.md)
1. Right-click in testMethod1 and select `Debug`.

### Console

```
lineNo	logDateTime	testCaseId	logType	group	message
1	2022/12/11 01:32:37.416	{}	[-]	()	----------------------------------------------------------------------------------------------------
2	2022/12/11 01:32:37.429	{}	[-]	()	///
3	2022/12/11 01:32:37.429	{}	[-]	()	/// Shirates 2.0.0
4	2022/12/11 01:32:37.430	{}	[-]	()	///
5	2022/12/11 01:32:37.430	{}	[-]	()	powered by Appium (io.appium:java-client:8.1.1)
6	2022/12/11 01:32:37.430	{}	[-]	()	----------------------------------------------------------------------------------------------------
7	2022/12/11 01:32:37.431	{}	[info]	(parameter)	testClass: exercise.TestClass1
8	2022/12/11 01:32:37.431	{}	[info]	(parameter)	sheetName: TestClass1
9	2022/12/11 01:32:37.431	{}	[info]	(parameter)	logLanguage: 
10	2022/12/11 01:32:38.059	{}	[info]	()	Initializing with testRun.properties.(testConfig/android/androidSettings/testrun.properties)
11	2022/12/11 01:32:38.128	{}	[info]	()	Logging to file:////Users/wave1008/Downloads/TestResults/androidSettingsConfig/2022-12-11_013237/TestClass1/
12	2022/12/11 01:32:38.135	{}	[info]	()	Loading config.(configFile=/Users/wave1008/Downloads/CreatingNewProject1/testConfig/android/androidSettings/androidSettingsConfig.json, profileName=Android *)
13	2022/12/11 01:32:38.166	{}	[warn]	()	screens directory not found. (/Users/wave1008/Downloads/CreatingNewProject1/testConfig/android/androidSettings/screens)
14	2022/12/11 01:32:38.177	{}	[info]	()	Scanning macro under '/Users/wave1008/Downloads/CreatingNewProject1/src/test/kotlin'
15	2022/12/11 01:32:38.178	{}	[info]	()	Initializing TestDriver.(profileName=Android *)
16	2022/12/11 01:32:38.179	{}	[info]	()	noLoadRun: false
17	2022/12/11 01:32:38.179	{}	[info]	()	boundsToRectRatio: 1
18	2022/12/11 01:32:38.180	{}	[info]	()	reuseDriver: true
19	2022/12/11 01:32:38.180	{}	[info]	()	autoScreenshot: true
20	2022/12/11 01:32:38.180	{}	[info]	()	onChangedOnly: true
21	2022/12/11 01:32:38.181	{}	[info]	()	onCondition: true
22	2022/12/11 01:32:38.181	{}	[info]	()	onAction: true
23	2022/12/11 01:32:38.181	{}	[info]	()	onExpectation: true
24	2022/12/11 01:32:38.182	{}	[info]	()	onExecOperateCommand: true
25	2022/12/11 01:32:38.182	{}	[info]	()	onCheckCommand: true
26	2022/12/11 01:32:38.182	{}	[info]	()	onScrolling: true
27	2022/12/11 01:32:38.183	{}	[info]	()	manualScreenshot: true
28	2022/12/11 01:32:38.184	{}	[info]	()	retryMaxCount: 1
29	2022/12/11 01:32:38.185	{}	[info]	()	retryIntervalSeconds: 2.0
30	2022/12/11 01:32:38.185	{}	[info]	()	shortWaitSeconds: 1.5
31	2022/12/11 01:32:38.185	{}	[info]	()	waitSecondsOnIsScreen: 15.0
32	2022/12/11 01:32:38.186	{}	[info]	()	waitSecondsForAnimationComplete: 0.5
33	2022/12/11 01:32:38.186	{}	[info]	()	waitSecondsForConnectionEnabled: 8.0
34	2022/12/11 01:32:38.186	{}	[info]	()	swipeDurationSeconds: 3.0
35	2022/12/11 01:32:38.187	{}	[info]	()	flickDurationSeconds: 0.3
36	2022/12/11 01:32:38.187	{}	[info]	()	swipeMarginRatio: 0.1
37	2022/12/11 01:32:38.188	{}	[info]	()	scrollVerticalMarginRatio: 0.2
38	2022/12/11 01:32:38.188	{}	[info]	()	scrollHorizontalMarginRatio: 0.2
39	2022/12/11 01:32:38.189	{}	[info]	()	tapHoldSeconds: 0.2
40	2022/12/11 01:32:38.189	{}	[info]	()	tapAppIconMethod: auto
41	2022/12/11 01:32:38.189	{}	[info]	()	tapAppIconMacro: 
42	2022/12/11 01:32:38.190	{}	[info]	()	syncWaitSeconds: 1.8
43	2022/12/11 01:32:38.190	{}	[info]	()	----------------------------------------------------------------------------------------------------
44	2022/12/11 01:32:38.191	{}	[info]	()	Searching device for (profileName=Android *)
45	2022/12/11 01:32:38.386	{}	[info]	()	Device found. (Pixel_3a_Android_12_-02:5554, Android 12, emulator-5554)
46	2022/12/11 01:32:38.435	{}	[info]	()	appium --session-override --relaxed-security --log /Users/wave1008/Downloads/TestResults/androidSettingsConfig/2022-12-11_013237/TestClass1/appium_2022-12-11_013238388.log --port 4720
Cleaning up unclosed ZipFile for archive /Users/wave1008/Downloads/TestResults/androidSettingsConfig/TestList_androidSettingsConfig.xlsx
47	2022/12/11 01:32:41.453	{}	[info]	()	Appium Server started. (pid=9753, port=4720)
48	2022/12/11 01:32:41.457	{}	[info]	()	Connecting to Appium Server.(http://127.0.0.1:4720/)
49	2022/12/11 01:32:47.341	{}	[info]	()	[Health check] start
50	2022/12/11 01:32:47.347	{}	[info]	(syncCache)	Syncing (1)
51	2022/12/11 01:32:48.115	{}	[info]	(syncCache)	elapsed=0.768, syncWaitSeconds=1.8
52	2022/12/11 01:32:48.616	{}	[info]	(syncCache)	Syncing (2)
53	2022/12/11 01:32:48.668	{}	[info]	(syncCache)	Synced. (elapsed=1.321, currentScreen=?)
54	2022/12/11 01:32:48.913	{}	[info]	()	[Health check] end
55	2022/12/11 01:32:48.919	{}	[info]	()	implicitlyWaitSeconds: 5.0
56	2022/12/11 01:32:49.008	{}	[info]	()	(settings) always_finish_activities: 0
57	2022/12/11 01:32:49.038	{}	[info]	()	AppiumDriver initialized.
58	2022/12/11 01:32:49.038	{}	[info]	(parameter)	testrun: testConfig/android/androidSettings/testrun.properties
59	2022/12/11 01:32:49.039	{}	[info]	(parameter)	testConfigName: androidSettingsConfig(/Users/wave1008/Downloads/CreatingNewProject1/testConfig/android/androidSettings/androidSettingsConfig.json)
60	2022/12/11 01:32:49.039	{}	[info]	(parameter)	profileName: Android *
61	2022/12/11 01:32:49.039	{}	[info]	(parameter)	appIconName: Settings
62	2022/12/11 01:32:49.040	{}	[-]	()	(capabilities)
63	2022/12/11 01:32:49.040	{}	[info]	(parameter)	appium:newCommandTimeout: 300
64	2022/12/11 01:32:49.041	{}	[info]	(parameter)	appium:takesScreenshot: true
65	2022/12/11 01:32:49.041	{}	[info]	(parameter)	appium:warnings: {}
66	2022/12/11 01:32:49.041	{}	[info]	(parameter)	appium:deviceApiLevel: 31
67	2022/12/11 01:32:49.042	{}	[info]	(parameter)	appium:automationName: UiAutomator2
68	2022/12/11 01:32:49.042	{}	[info]	(parameter)	appium:locationContextEnabled: false
69	2022/12/11 01:32:49.043	{}	[info]	(parameter)	appium:deviceScreenSize: 1080x2220
70	2022/12/11 01:32:49.043	{}	[info]	(parameter)	appium:deviceManufacturer: Google
71	2022/12/11 01:32:49.043	{}	[info]	(parameter)	appium:udid: emulator-5554
72	2022/12/11 01:32:49.044	{}	[info]	(parameter)	appium:pixelRatio: 2.75
73	2022/12/11 01:32:49.044	{}	[info]	(parameter)	platformName: android
74	2022/12/11 01:32:49.045	{}	[info]	(parameter)	appium:networkConnectionEnabled: true
75	2022/12/11 01:32:49.045	{}	[info]	(parameter)	appium:locale: US
76	2022/12/11 01:32:49.046	{}	[info]	(parameter)	appium:deviceScreenDensity: 440
77	2022/12/11 01:32:49.046	{}	[info]	(parameter)	appium:viewportRect: {left=0, top=66, width=1080, height=2022}
78	2022/12/11 01:32:49.046	{}	[info]	(parameter)	appium:language: en
79	2022/12/11 01:32:49.046	{}	[info]	(parameter)	appium:avd: Pixel_3a_Android_12_-02
80	2022/12/11 01:32:49.047	{}	[info]	(parameter)	appium:deviceModel: sdk_gphone64_arm64
81	2022/12/11 01:32:49.047	{}	[info]	(parameter)	appium:platformVersion: 12
82	2022/12/11 01:32:49.047	{}	[info]	(parameter)	appium:databaseEnabled: false
83	2022/12/11 01:32:49.047	{}	[info]	(parameter)	appium:deviceUDID: emulator-5554
84	2022/12/11 01:32:49.048	{}	[info]	(parameter)	appium:statBarHeight: 66
85	2022/12/11 01:32:49.048	{}	[info]	(parameter)	appium:webStorageEnabled: false
86	2022/12/11 01:32:49.048	{}	[info]	(parameter)	appium:appActivity: com.android.settings.Settings
87	2022/12/11 01:32:49.048	{}	[info]	(parameter)	appium:deviceName: emulator-5554
88	2022/12/11 01:32:49.049	{}	[info]	(parameter)	appium:javascriptEnabled: true
89	2022/12/11 01:32:49.049	{}	[info]	(parameter)	appium:appPackage: com.android.settings
90	2022/12/11 01:32:49.049	{}	[-]	()	settings
91	2022/12/11 01:32:49.125	{}	[info]	(parameter)	always_finish_activities: 0
92	2022/12/11 01:32:49.126	{}	[-]	()	(others)
93	2022/12/11 01:32:49.127	{}	[info]	(parameter)	isEmulator: true
94	2022/12/11 01:32:49.127	{}	[info]	(parameter)	hasOsaihuKeitai: false
95	2022/12/11 01:32:49.132	{}	[warn]	()	scenario not implemented.

org.opentest4j.TestAbortedException: scenario not implemented.


	at shirates.core.testcode.UITestCallbackExtension.afterEach(UITestCallbackExtension.kt:198)
	at org.junit.jupiter.engine.descriptor.TestMethodTestDescriptor.lambda$invokeAfterEachCallbacks$12(TestMethodTestDescriptor.java:260)
	at org.junit.jupiter.engine.descriptor.TestMethodTestDescriptor.lambda$invokeAllAfterMethodsOrCallbacks$13(TestMethodTestDescriptor.java:276)
	at org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(ThrowableCollector.java:73)
	at org.junit.jupiter.engine.descriptor.TestMethodTestDescriptor.lambda$invokeAllAfterMethodsOrCallbacks$14(TestMethodTestDescriptor.java:276)
	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
	at org.junit.jupiter.engine.descriptor.TestMethodTestDescriptor.invokeAllAfterMethodsOrCallbacks(TestMethodTestDescriptor.java:275)
	at org.junit.jupiter.engine.descriptor.TestMethodTestDescriptor.invokeAfterEachCallbacks(TestMethodTestDescriptor.java:259)
	at org.junit.jupiter.engine.descriptor.TestMethodTestDescriptor.execute(TestMethodTestDescriptor.java:144)
	at org.junit.jupiter.engine.descriptor.TestMethodTestDescriptor.execute(TestMethodTestDescriptor.java:68)
	at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$6(NodeTestTask.java:151)
	at org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(ThrowableCollector.java:73)
	at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$8(NodeTestTask.java:141)
	at org.junit.platform.engine.support.hierarchical.Node.around(Node.java:137)
	at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$9(NodeTestTask.java:139)
	at org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(ThrowableCollector.java:73)
	at org.junit.platform.engine.support.hierarchical.NodeTestTask.executeRecursively(NodeTestTask.java:138)
	at org.junit.platform.engine.support.hierarchical.NodeTestTask.execute(NodeTestTask.java:95)
	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
	at org.junit.platform.engine.support.hierarchical.SameThreadHierarchicalTestExecutorService.invokeAll(SameThreadHierarchicalTestExecutorService.java:41)
	at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$6(NodeTestTask.java:155)
	at org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(ThrowableCollector.java:73)
	at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$8(NodeTestTask.java:141)
	at org.junit.platform.engine.support.hierarchical.Node.around(Node.java:137)
	at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$9(NodeTestTask.java:139)
	at org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(ThrowableCollector.java:73)
	at org.junit.platform.engine.support.hierarchical.NodeTestTask.executeRecursively(NodeTestTask.java:138)
	at org.junit.platform.engine.support.hierarchical.NodeTestTask.execute(NodeTestTask.java:95)
	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
	at org.junit.platform.engine.support.hierarchical.SameThreadHierarchicalTestExecutorService.invokeAll(SameThreadHierarchicalTestExecutorService.java:41)
	at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$6(NodeTestTask.java:155)
	at org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(ThrowableCollector.java:73)
	at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$8(NodeTestTask.java:141)
	at org.junit.platform.engine.support.hierarchical.Node.around(Node.java:137)
	at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$9(NodeTestTask.java:139)
	at org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(ThrowableCollector.java:73)
	at org.junit.platform.engine.support.hierarchical.NodeTestTask.executeRecursively(NodeTestTask.java:138)
	at org.junit.platform.engine.support.hierarchical.NodeTestTask.execute(NodeTestTask.java:95)
	at org.junit.platform.engine.support.hierarchical.SameThreadHierarchicalTestExecutorService.submit(SameThreadHierarchicalTestExecutorService.java:35)
	at org.junit.platform.engine.support.hierarchical.HierarchicalTestExecutor.execute(HierarchicalTestExecutor.java:57)
	at org.junit.platform.engine.support.hierarchical.HierarchicalTestEngine.execute(HierarchicalTestEngine.java:54)
	at org.junit.platform.launcher.core.EngineExecutionOrchestrator.execute(EngineExecutionOrchestrator.java:147)
	at org.junit.platform.launcher.core.EngineExecutionOrchestrator.execute(EngineExecutionOrchestrator.java:127)
	at org.junit.platform.launcher.core.EngineExecutionOrchestrator.execute(EngineExecutionOrchestrator.java:90)
	at org.junit.platform.launcher.core.EngineExecutionOrchestrator.lambda$execute$0(EngineExecutionOrchestrator.java:55)
	at org.junit.platform.launcher.core.EngineExecutionOrchestrator.withInterceptedStreams(EngineExecutionOrchestrator.java:102)
	at org.junit.platform.launcher.core.EngineExecutionOrchestrator.execute(EngineExecutionOrchestrator.java:54)
	at org.junit.platform.launcher.core.DefaultLauncher.execute(DefaultLauncher.java:114)
	at org.junit.platform.launcher.core.DefaultLauncher.execute(DefaultLauncher.java:86)
	at org.junit.platform.launcher.core.DefaultLauncherSession$DelegatingLauncher.execute(DefaultLauncherSession.java:86)
	at org.junit.platform.launcher.core.SessionPerRequestLauncher.execute(SessionPerRequestLauncher.java:53)
	at com.intellij.junit5.JUnit5IdeaTestRunner.startRunnerWithArgs(JUnit5IdeaTestRunner.java:57)
	at com.intellij.rt.junit.IdeaTestRunner$Repeater$1.execute(IdeaTestRunner.java:38)
	at com.intellij.rt.execution.junit.TestsRepeater.repeat(TestsRepeater.java:11)
	at com.intellij.rt.junit.IdeaTestRunner$Repeater.startRunnerWithArgs(IdeaTestRunner.java:35)
	at com.intellij.rt.junit.JUnitStarter.prepareStreamsAndStart(JUnitStarter.java:235)
	at com.intellij.rt.junit.JUnitStarter.main(JUnitStarter.java:54)

96	2022/12/11 01:32:49.137	{}	[info]	()	Logging to file:////Users/wave1008/Downloads/TestResults/androidSettingsConfig/2022-12-11_013237/TestClass1/
Copying jar content _ReportScript.js to /Users/wave1008/Downloads/TestResults/androidSettingsConfig/2022-12-11_013237/TestClass1
Copying jar content _ReportStyle.css to /Users/wave1008/Downloads/TestResults/androidSettingsConfig/2022-12-11_013237/TestClass1
No scenario found. Outputting Spec-Report skipped.
Cleaning up unclosed ZipFile for archive /Users/wave1008/Downloads/TestResults/androidSettingsConfig/TestList_androidSettingsConfig.xlsx
97	2022/12/11 01:32:49.411	{}	[info]	()	Quitting TestDriver.
Disconnected from the target VM, address: '127.0.0.1:60559', transport: 'socket'

Process finished with exit code 255
```

You will find this line because scenario has not been implemented yet.

```
org.opentest4j.TestAbortedException: scenario not implemented.
```

If you encounter another error see [Error messages](../../troubleshooting/error_warning_messages.md).

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

