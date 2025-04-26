# Creating TestClass (Vision)

## UITest class

1. Create a package directory `src/test/kotlin/exercise`.

   ![](../_images/creating_package_1.png)<br><br>
   ![](../_images/creating_package_2.png)


2. Create a kotlin class file `VisionTestClass1`.

   ![](../_images/creating_testclass.png)

   ![](../_images/creating_testclass_2.png)

   ![](../_images/creating_testclass_3.png)


3. Type `" : VisionTest()"` after class name, move mouse cursor to it, then click `import` on context menu. <br>Shortcut
   is
   useful. (Mac: option+Enter, Windows: Alt+Enter)

   ![](../_images/import_package.png)

```kotlin
package exercise

import shirates.core.vision.testcode.VisionTest

class VisionTestClass1: VisionTest() {
}
```

## @Test annotation

Create a function (`testFunc1`) and put `@Test` annotation of **JUnit 5** (**org.junit.jupiter.api.Test**).

```kotlin
package exercise

import org.junit.jupiter.api.Test
import shirates.core.vision.testcode.VisionTest

class VisionTestClass1: VisionTest() {

    @Test
    fun testFunc1() {

    }
}
```

## Running test (as Android)

Now you can run `testFunc1()`. Default os is Android.<br>
Right-click on `testFunc1()` and select `Debug`. <br>
![](../_images/run_testfunc1.png)

When **Vision-Server** is not running, you will see the following error messages.

```
Could not connect to vision-server.
shirates.core.exception.TestEnvironmentException: Could not connect to vision-server.
```

See **Setting up shirates-vision-server** in [Quick Start](../../../quick-start.md)
to run the server.

You can see output in the console as follows if succeeded.

#### Console output

```
Connected to the target VM, address: '127.0.0.1:59904', transport: 'socket'
lineNo	[elapsedTime]	logDateTime	{testCaseId}	macroDepth	macroName	[logType]	timeDiff	mode	(group)	message
1	[00:00:00]	2025/04/26 14:46:56.660	{}	0	-	[-]	+0	C	()	----------------------------------------------------------------------------------------------------
2	[00:00:00]	2025/04/26 14:46:56.678	{}	0	-	[-]	+18	C	()	///
3	[00:00:00]	2025/04/26 14:46:56.679	{}	0	-	[-]	+1	C	()	/// shirates-core 8.3.1-SNAPSHOT
4	[00:00:00]	2025/04/26 14:46:56.679	{}	0	-	[-]	+0	C	()	///
5	[00:00:00]	2025/04/26 14:46:56.680	{}	0	-	[-]	+1	C	()	powered by Appium (io.appium:java-client:9.4.0)
6	[00:00:00]	2025/04/26 14:46:56.681	{}	0	-	[-]	+1	C	()	----------------------------------------------------------------------------------------------------
7	[00:00:00]	2025/04/26 14:46:56.681	{}	0	-	[-]	+0	C	()	testClass: exercise.VisionTestClass1
8	[00:00:00]	2025/04/26 14:46:56.681	{}	0	-	[-]	+0	C	()	sheetName: VisionTestClass1
9	[00:00:00]	2025/04/26 14:46:56.681	{}	0	-	[-]	+0	C	()	logLanguage: 
10	[00:00:00]	2025/04/26 14:46:56.692	{}	0	-	[info]	+11	C	()	
11	[00:00:00]	2025/04/26 14:46:56.693	{}	0	-	[info]	+1	C	()	----------------------------------------------------------------------------------------------------
12	[00:00:00]	2025/04/26 14:46:56.693	{}	0	-	[info]	+0	C	()	Test function: testFunc1 [testFunc1()]
13	[00:00:00]	2025/04/26 14:46:56.693	{}	0	-	[info]	+0	C	()	----------------------------------------------------------------------------------------------------
14	[00:00:00]	2025/04/26 14:46:57.333	{}	0	-	[info]	+640	C	()	Initializing with testrun file.(testrun.global.properties)
15	[00:00:00]	2025/04/26 14:46:57.341	{}	0	-	[info]	+8	C	()	Logging to file:////Users/wave1008/Downloads/TestResults/testConfig@a/2025-04-26_144656/VisionTestClass1/
16	[00:00:01]	2025/04/26 14:46:57.482	{}	0	-	[info]	+141	C	()	Loading config.(configFile=/Users/wave1008/github/ldi-github/shirates-core-vision-samples_en/testConfig/android/testConfig@a.json, profileName=Pixel 8(Android 14))
17	[00:00:01]	2025/04/26 14:46:58.252	{}	0	-	[info]	+770	C	()	Scanning macro under '/Users/wave1008/github/ldi-github/shirates-core-vision-samples_en/src/test/kotlin'
18	[00:00:01]	2025/04/26 14:46:58.283	{}	0	-	[info]	+31	C	()	Registering macro. (macro.ios.iOSSettingsMacro)
19	[00:00:01]	2025/04/26 14:46:58.312	{}	0	-	[info]	+29	C	()	Registering macro. (macro.android.AndroidSettingsMacro)
20	[00:00:01]	2025/04/26 14:46:58.318	{}	0	-	[info]	+6	C	()	Registering macro. (macro.android.FilesMacro)
21	[00:00:01]	2025/04/26 14:46:58.322	{}	0	-	[info]	+4	C	()	Registering macro. (macro.android.MapsMacro)
22	[00:00:01]	2025/04/26 14:46:58.325	{}	0	-	[info]	+3	C	()	Registering macro. (macro.MacroObject1)
23	[00:00:01]	2025/04/26 14:46:58.381	{}	0	-	[info]	+56	C	()	Classifier files loaded.(CheckStateClassifier, 2 labels, directory=/Users/wave1008/github/ldi-github/shirates-core-vision-samples_en/build/vision/classifiers/CheckStateClassifier/1)
24	[00:00:01]	2025/04/26 14:46:58.387	{}	0	-	[info]	+6	C	()	Learning skipped. Updated file not found. (CheckStateClassifier(1), /Users/wave1008/github/ldi-github/shirates-core-vision-samples_en/vision/classifiers/CheckStateClassifier/1)
25	[00:00:01]	2025/04/26 14:46:58.395	{}	0	-	[info]	+8	C	()	Classifier files loaded.(ScreenClassifier, 20 labels, directory=/Users/wave1008/github/ldi-github/shirates-core-vision-samples_en/build/vision/classifiers/ScreenClassifier/1)
26	[00:00:01]	2025/04/26 14:46:58.402	{}	0	-	[info]	+7	C	()	Learning skipped. Updated file not found. (ScreenClassifier(1), /Users/wave1008/github/ldi-github/shirates-core-vision-samples_en/vision/classifiers/ScreenClassifier/1)
27	[00:00:01]	2025/04/26 14:46:58.412	{}	0	-	[info]	+10	C	()	Classifier files loaded.(DefaultClassifier, 32 labels, directory=/Users/wave1008/github/ldi-github/shirates-core-vision-samples_en/build/vision/classifiers/DefaultClassifier/1)
28	[00:00:02]	2025/04/26 14:46:58.421	{}	0	-	[info]	+9	C	()	Learning skipped. Updated file not found. (DefaultClassifier(1), /Users/wave1008/github/ldi-github/shirates-core-vision-samples_en/vision/classifiers/DefaultClassifier/1)
TextIndex: [Developer options Screen] priority=1, length=2, [Developer options, Use developer options], imageIndexFile=/Users/wave1008/github/ldi-github/shirates-core-vision-samples_en/vision/classifiers/ScreenClassifier/@a/Android Settings/[Developer options Screen]/#1.png
29	[00:00:02]	2025/04/26 14:46:58.445	{}	0	-	[info]	+24	C	()	Initializing TestDriver.(profileName=Pixel 8(Android 14))
30	[00:00:02]	2025/04/26 14:46:58.447	{}	0	-	[info]	+2	C	()	noLoadRun: false
31	[00:00:02]	2025/04/26 14:46:58.447	{}	0	-	[info]	+0	C	()	boundsToRectRatio: 1
32	[00:00:02]	2025/04/26 14:46:58.448	{}	0	-	[info]	+1	C	()	reuseDriver: true
33	[00:00:02]	2025/04/26 14:46:58.449	{}	0	-	[info]	+1	C	()	autoScreenshot: true
34	[00:00:02]	2025/04/26 14:46:58.449	{}	0	-	[info]	+0	C	()	onChangedOnly: true
35	[00:00:02]	2025/04/26 14:46:58.450	{}	0	-	[info]	+1	C	()	onCondition: true
36	[00:00:02]	2025/04/26 14:46:58.450	{}	0	-	[info]	+0	C	()	onAction: true
37	[00:00:02]	2025/04/26 14:46:58.451	{}	0	-	[info]	+1	C	()	onExpectation: true
38	[00:00:02]	2025/04/26 14:46:58.451	{}	0	-	[info]	+0	C	()	onExecOperateCommand: true
39	[00:00:02]	2025/04/26 14:46:58.452	{}	0	-	[info]	+1	C	()	onCheckCommand: true
40	[00:00:02]	2025/04/26 14:46:58.452	{}	0	-	[info]	+0	C	()	onScrolling: true
41	[00:00:02]	2025/04/26 14:46:58.453	{}	0	-	[info]	+1	C	()	manualScreenshot: true
42	[00:00:02]	2025/04/26 14:46:58.453	{}	0	-	[info]	+0	C	()	retryMaxCount: 2
43	[00:00:02]	2025/04/26 14:46:58.455	{}	0	-	[info]	+2	C	()	retryIntervalSeconds: 2.0
44	[00:00:02]	2025/04/26 14:46:58.456	{}	0	-	[info]	+1	C	()	shortWaitSeconds: 1.5
45	[00:00:02]	2025/04/26 14:46:58.456	{}	0	-	[info]	+0	C	()	waitSecondsOnIsScreen: 15.0
46	[00:00:02]	2025/04/26 14:46:58.457	{}	0	-	[info]	+1	C	()	waitSecondsForLaunchAppComplete: 15.0
47	[00:00:02]	2025/04/26 14:46:58.458	{}	0	-	[info]	+1	C	()	waitSecondsForAnimationComplete: 0.5
48	[00:00:02]	2025/04/26 14:46:58.458	{}	0	-	[info]	+0	C	()	waitSecondsForConnectionEnabled: 8.0
49	[00:00:02]	2025/04/26 14:46:58.460	{}	0	-	[info]	+2	C	()	swipeDurationSeconds: 3.0
50	[00:00:02]	2025/04/26 14:46:58.462	{}	0	-	[info]	+2	C	()	flickDurationSeconds: 0.3
51	[00:00:02]	2025/04/26 14:46:58.464	{}	0	-	[info]	+2	C	()	swipeMarginRatio: 0.0
52	[00:00:02]	2025/04/26 14:46:58.465	{}	0	-	[info]	+1	C	()	scrollVerticalStartMarginRatio: 0.15
53	[00:00:02]	2025/04/26 14:46:58.466	{}	0	-	[info]	+1	C	()	scrollVerticalEndMarginRatio: 0.1
54	[00:00:02]	2025/04/26 14:46:58.466	{}	0	-	[info]	+0	C	()	scrollHorizontalStartMarginRatio: 0.2
55	[00:00:02]	2025/04/26 14:46:58.467	{}	0	-	[info]	+1	C	()	scrollHorizontalEndMarginRatio: 0.1
56	[00:00:02]	2025/04/26 14:46:58.467	{}	0	-	[info]	+0	C	()	tapHoldSeconds: 0.0
57	[00:00:02]	2025/04/26 14:46:58.468	{}	0	-	[info]	+1	C	()	tapAppIconMethod: auto
58	[00:00:02]	2025/04/26 14:46:58.469	{}	0	-	[info]	+1	C	()	tapAppIconMacro: 
59	[00:00:02]	2025/04/26 14:46:58.470	{}	0	-	[info]	+1	C	()	enableCache: true
60	[00:00:02]	2025/04/26 14:46:58.470	{}	0	-	[info]	+0	C	()	syncWaitSeconds: 1.8
61	[00:00:03]	2025/04/26 14:46:59.728	{}	0	-	[info]	+1258	C	()	Running device found. (udid=emulator-5554, avd=Pixel_8_Android_14)
62	[00:00:03]	2025/04/26 14:46:59.730	{}	0	-	[info]	+2	C	()	Connected device found. (Pixel_8_Android_14:5554, Android 14, emulator-5554)
63	[00:00:03]	2025/04/26 14:46:59.859	{}	0	-	[info]	+129	C	()	Terminating Appium Server. (pid=22242, port=4721)
64	[00:00:03]	2025/04/26 14:46:59.894	{}	0	-	[info]	+35	C	()	Starting Appium Server.
65	[00:00:03]	2025/04/26 14:46:59.903	{}	0	-	[info]	+9	C	()	appium --session-override --relaxed-security --log /Users/wave1008/Downloads/TestResults/testConfig@a/2025-04-26_144656/VisionTestClass1/appium_2025-04-26_144659732.log --port 4721
66	[00:00:06]	2025/04/26 14:47:03.222	{}	0	-	[info]	+3319	C	()	Appium Server started. (pid=22353, port=4721)
67	[00:00:07]	2025/04/26 14:47:04.235	{}	0	-	[info]	+1013	C	()	Connecting to Appium Server.(http://127.0.0.1:4721/)
68	[00:00:10]	2025/04/26 14:47:06.840	{}	0	-	[info]	+2605	C	()	implicitlyWaitSeconds: 5.0
69	[00:00:10]	2025/04/26 14:47:06.961	{}	0	-	[info]	+121	C	()	(settings) always_finish_activities: 0
70	[00:00:11]	2025/04/26 14:47:07.539	{}	0	-	[info]	+578	C	()	Syncing screen.(isSame: false, changed: false, matchRate: 0.0, distance=1.0)
71	[00:00:12]	2025/04/26 14:47:08.739	{}	0	-	[info]	+1200	C	()	Syncing screen.(isSame: false, changed: true, matchRate: 0.9615554511547089, distance=0.03844454884529114)
72	[00:00:13]	2025/04/26 14:47:09.667	{}	0	-	[info]	+928	C	()	Syncing screen.(isSame: true, changed: true, matchRate: 0.9999802496149641, distance=1.9750385035877116E-5)
73	[00:00:13]	2025/04/26 14:47:09.668	{}	0	-	[screenshot]	+1	C	()	screenshot: 73.png
74	[00:00:13]	2025/04/26 14:47:10.304	{}	0	-	[info]	+636	C	()	73_[73.png]_recognizeText_rectangles.png
2025-04-26 14:47:10.496 java[22336:278809] +[IMKClient subclass]: chose IMKClient_Modern
2025-04-26 14:47:10.496 java[22336:278809] +[IMKInputSession subclass]: chose IMKInputSession_Modern
75	[00:00:14]	2025/04/26 14:47:10.847	{}	0	-	[info]	+543	C	()	[Android Settings Top Screen] found by matchTextScoreRate
76	[00:00:14]	2025/04/26 14:47:10.851	{}	0	-	[info]	+4	C	()	[recognizeScreen] in 1.179 sec
77	[00:00:14]	2025/04/26 14:47:10.853	{}	0	-	[info]	+2	C	()	currentScreen=[Android Settings Top Screen]
78	[00:00:14]	2025/04/26 14:47:10.855	{}	0	-	[info]	+2	C	()	AppiumDriver initialized.
79	[00:00:14]	2025/04/26 14:47:10.857	{}	0	-	[-]	+2	C	()	testMode: Vision
80	[00:00:14]	2025/04/26 14:47:10.859	{}	0	-	[-]	+2	C	()	testrun: testrun.global.properties
81	[00:00:14]	2025/04/26 14:47:10.863	{}	0	-	[-]	+4	C	()	testConfigName: testConfig@a(/Users/wave1008/github/ldi-github/shirates-core-vision-samples_en/testConfig/android/testConfig@a.json)
82	[00:00:14]	2025/04/26 14:47:10.865	{}	0	-	[-]	+2	C	()	profileName: Pixel 8(Android 14)
83	[00:00:14]	2025/04/26 14:47:10.866	{}	0	-	[-]	+1	C	()	appIconName: Settings
84	[00:00:14]	2025/04/26 14:47:10.868	{}	0	-	[-]	+2	C	()	(capabilities)
85	[00:00:14]	2025/04/26 14:47:10.872	{}	0	-	[-]	+4	C	()	appium:newCommandTimeout: 300
86	[00:00:14]	2025/04/26 14:47:10.873	{}	0	-	[-]	+1	C	()	appium:takesScreenshot: true
87	[00:00:14]	2025/04/26 14:47:10.874	{}	0	-	[-]	+1	C	()	appium:warnings: {}
88	[00:00:14]	2025/04/26 14:47:10.874	{}	0	-	[-]	+0	C	()	appium:deviceApiLevel: 34
89	[00:00:14]	2025/04/26 14:47:10.875	{}	0	-	[-]	+1	C	()	appium:automationName: UiAutomator2
90	[00:00:14]	2025/04/26 14:47:10.876	{}	0	-	[-]	+1	C	()	appium:locationContextEnabled: false
91	[00:00:14]	2025/04/26 14:47:10.884	{}	0	-	[-]	+8	C	()	appium:deviceScreenSize: 1080x2400
92	[00:00:14]	2025/04/26 14:47:10.891	{}	0	-	[-]	+7	C	()	appium:deviceManufacturer: Google
93	[00:00:14]	2025/04/26 14:47:10.894	{}	0	-	[-]	+3	C	()	appium:enforceXPath1: true
94	[00:00:14]	2025/04/26 14:47:10.894	{}	0	-	[-]	+0	C	()	appium:udid: emulator-5554
95	[00:00:14]	2025/04/26 14:47:10.894	{}	0	-	[-]	+0	C	()	appium:pixelRatio: 2.625
96	[00:00:14]	2025/04/26 14:47:10.895	{}	0	-	[-]	+1	C	()	platformName: ANDROID
97	[00:00:14]	2025/04/26 14:47:10.896	{}	0	-	[-]	+1	C	()	appium:networkConnectionEnabled: true
98	[00:00:14]	2025/04/26 14:47:10.897	{}	0	-	[-]	+1	C	()	appium:locale: US
99	[00:00:14]	2025/04/26 14:47:10.898	{}	0	-	[-]	+1	C	()	appium:deviceScreenDensity: 420
100	[00:00:14]	2025/04/26 14:47:10.899	{}	0	-	[-]	+1	C	()	appium:viewportRect: {left=0, top=132, width=1080, height=2268}
101	[00:00:14]	2025/04/26 14:47:10.899	{}	0	-	[-]	+0	C	()	appium:language: en
102	[00:00:14]	2025/04/26 14:47:10.900	{}	0	-	[-]	+1	C	()	appium:avd: Pixel_8_Android_14
103	[00:00:14]	2025/04/26 14:47:10.900	{}	0	-	[-]	+0	C	()	appium:deviceModel: sdk_gphone64_arm64
104	[00:00:14]	2025/04/26 14:47:10.901	{}	0	-	[-]	+1	C	()	appium:platformVersion: 14
105	[00:00:14]	2025/04/26 14:47:10.901	{}	0	-	[-]	+0	C	()	appium:databaseEnabled: false
106	[00:00:14]	2025/04/26 14:47:10.902	{}	0	-	[-]	+1	C	()	appium:deviceUDID: emulator-5554
107	[00:00:14]	2025/04/26 14:47:10.903	{}	0	-	[-]	+1	C	()	appium:statBarHeight: 132
108	[00:00:14]	2025/04/26 14:47:10.904	{}	0	-	[-]	+1	C	()	appium:webStorageEnabled: false
109	[00:00:14]	2025/04/26 14:47:10.904	{}	0	-	[-]	+0	C	()	appium:appActivity: com.android.settings.Settings
110	[00:00:14]	2025/04/26 14:47:10.905	{}	0	-	[-]	+1	C	()	appium:deviceName: emulator-5554
111	[00:00:14]	2025/04/26 14:47:10.905	{}	0	-	[-]	+0	C	()	appium:javascriptEnabled: true
112	[00:00:14]	2025/04/26 14:47:10.906	{}	0	-	[-]	+1	C	()	appium:appPackage: com.android.settings
113	[00:00:14]	2025/04/26 14:47:10.906	{}	0	-	[-]	+0	C	()	settings
114	[00:00:14]	2025/04/26 14:47:11.046	{}	0	-	[-]	+140	C	()	always_finish_activities: 0
115	[00:00:14]	2025/04/26 14:47:11.047	{}	0	-	[-]	+1	C	()	(others)
116	[00:00:14]	2025/04/26 14:47:11.048	{}	0	-	[-]	+1	C	()	isEmulator: true
117	[00:00:14]	2025/04/26 14:47:11.049	{}	0	-	[-]	+1	C	()	hasOsaifuKeitai: false
118	[00:00:14]	2025/04/26 14:47:11.052	{}	0	-	[info]	+3	C	()	disableCache
119	[00:00:14]	2025/04/26 14:47:11.054	{}	0	-	[info]	+2	!	()	Setup executed. (duration: 14.0 sec)
120	[00:00:14]	2025/04/26 14:47:11.060	{}	0	-	[info]	+6	C	()	Test function executed. (duration: 14.4 sec)
121	[00:00:14]	2025/04/26 14:47:11.061	{}	0	-	[info]	+1	C	()	End of Test function: testFunc1 [testFunc1()]
122	[00:00:14]	2025/04/26 14:47:11.088	{}	0	-	[WARN]	+27	C	()	scenario not implemented.

org.opentest4j.TestAbortedException: scenario not implemented.


	at shirates.core.testcode.UITestCallbackExtension.afterEach(UITestCallbackExtension.kt:387)
	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)

123	[00:00:15]	2025/04/26 14:47:12.100	{}	0	-	[info]	+1012	C	()	Logging to file:////Users/wave1008/Downloads/TestResults/testConfig@a/2025-04-26_144656/VisionTestClass1/
Copying jar content _ReportScript.js to /Users/wave1008/Downloads/TestResults/testConfig@a/2025-04-26_144656/VisionTestClass1
Copying jar content _ReportStyle.css to /Users/wave1008/Downloads/TestResults/testConfig@a/2025-04-26_144656/VisionTestClass1
No scenario found. Outputting Spec-Report skipped.
124	[00:00:16]	2025/04/26 14:47:12.548	{}	0	-	[info]	+448	C	()	Quitting TestDriver.
125	[00:00:16]	2025/04/26 14:47:12.793	{}	0	-	[info]	+245	C	()	Test class executed. (duration: 16.4 sec)
Disconnected from the target VM, address: '127.0.0.1:59904', transport: 'socket'

Process finished with exit code 255
```

## Running test (as iOS)

1. Open `testrun.global.properties`.
2. Set `os=ios`. <br> ![](../_images/testrun_global_properties_ios.png)
3. Right-click on `testFunc1()` and select `Debug`.

#### Console output

```
...
62	[00:00:07]	2025/04/26 14:47:50.396	{}	0	-	[info]	+19	C	()	Starting Appium Server.
63	[00:00:07]	2025/04/26 14:47:50.397	{}	0	-	[info]	+1	C	()	appium --session-override --relaxed-security --log /Users/wave1008/Downloads/TestResults/testConfig@i/2025-04-26_144743/VisionTestClass1/appium_2025-04-26_144750249.log --port 4721
64	[00:00:10]	2025/04/26 14:47:53.678	{}	0	-	[info]	+3281	C	()	Appium Server started. (pid=22488, port=4721)
65	[00:00:11]	2025/04/26 14:47:54.695	{}	0	-	[info]	+1017	C	()	Optimizing installing WebDriverAgent.
66	[00:00:11]	2025/04/26 14:47:54.715	{}	0	-	[info]	+20	C	()	WebDriverAgentRunner-Runner.app not found. Optimization skipped.
67	[00:00:11]	2025/04/26 14:47:54.719	{}	0	-	[info]	+4	C	()	Connecting to Appium Server.(http://127.0.0.1:4721/)
68	[00:00:11]	2025/04/26 14:47:54.721	{}	0	-	[info]	+2	C	()	Note: Initializing IOSDriver may take a few minutes to build and install WebDriverAgent.
...
```

If you encounter another error, see [Error messages](../../troubleshooting/error_warning_messages.md).

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
import shirates.core.vision.testcode.VisionTest

class VisionTestClass1 : VisionTest() {

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

No. **Do not** use `@Nested annotation` of JUnit 5. This limitation is of software design of shirates-core.

### Link

- [index](../../../index.md)

