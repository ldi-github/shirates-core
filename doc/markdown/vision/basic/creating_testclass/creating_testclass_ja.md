# テストクラスを作成する (Vision)

## VisionTest class

1. `src/test/kotlin/exercise`にパッケージディレクトリを作成します。

   ![](../_images/creating_package_1.png)<br><br>
   ![](../_images/creating_package_2.png)


2. kotlinのクラスファイルとして`VisionTestClass1`を作成します。

   ![](../_images/creating_testclass.png)

   ![](../_images/creating_testclass_2.png)

   ![](../_images/creating_testclass_3.png)


3. クラス名の後で`" : VisionTest()"`とタイプし、 マウスカーソルをその上に移動し、コンテキストメニューの`import`
   を選択します。 <br>
   ショートカット(option+Enter)を使用すると便利です。

   ![](../_images/import_package.png)

```kotlin
package exercise

import shirates.core.vision.testcode.VisionTest

class VisionTestClass1: VisionTest() {
}
```

## @Testアノテーション

`testFunc1`という名前で関数を作成し **JUnit 5** (**org.junit.jupiter.api.Test**)の`@Test`アノテーションを付与します。

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

## テストを実行する(Android)

これで `testFunc1()` を実行できるようになりました。デフォルトのOSはAndroidです。<br>
`testFunc1()` を右クリックして`Debug`を選択します。 <br>
![](../_images/run_testfunc1.png)

以下のようなエラーが出力された場合はVision-Serverが起動していません。

```
Could not connect to vision-server.
shirates.core.exception.TestEnvironmentException: Could not connect to vision-server.
```

この場合、[クイックスタート](../../../quick-start_ja.md)
の**shirates-vision-server のセットアップ**を参照してサーバーを起動してください。

正常に起動した場合はコンソールに以下のように出力されます。

#### コンソール出力

```
Connected to the target VM, address: '127.0.0.1:60644', transport: 'socket'
lineNo	[elapsedTime]	logDateTime	{testCaseId}	macroDepth	macroName	[logType]	timeDiff	mode	(group)	message
1	[00:00:00]	2025/04/26 14:51:20.546	{}	0	-	[-]	+0	C	()	----------------------------------------------------------------------------------------------------
2	[00:00:00]	2025/04/26 14:51:20.564	{}	0	-	[-]	+18	C	()	///
3	[00:00:00]	2025/04/26 14:51:20.564	{}	0	-	[-]	+0	C	()	/// shirates-core 8.3.1-SNAPSHOT
4	[00:00:00]	2025/04/26 14:51:20.565	{}	0	-	[-]	+1	C	()	///
5	[00:00:00]	2025/04/26 14:51:20.566	{}	0	-	[-]	+1	C	()	powered by Appium (io.appium:java-client:9.4.0)
6	[00:00:00]	2025/04/26 14:51:20.566	{}	0	-	[-]	+0	C	()	----------------------------------------------------------------------------------------------------
7	[00:00:00]	2025/04/26 14:51:20.566	{}	0	-	[-]	+0	C	()	testClass: exercise.VisionTestClass1
8	[00:00:00]	2025/04/26 14:51:20.567	{}	0	-	[-]	+1	C	()	sheetName: VisionTestClass1
9	[00:00:00]	2025/04/26 14:51:20.567	{}	0	-	[-]	+0	C	()	logLanguage: ja
10	[00:00:00]	2025/04/26 14:51:20.576	{}	0	-	[info]	+9	C	()	
11	[00:00:00]	2025/04/26 14:51:20.577	{}	0	-	[info]	+1	C	()	----------------------------------------------------------------------------------------------------
12	[00:00:00]	2025/04/26 14:51:20.577	{}	0	-	[info]	+0	C	()	Test function: testFunc1 [testFunc1()]
13	[00:00:00]	2025/04/26 14:51:20.577	{}	0	-	[info]	+0	C	()	----------------------------------------------------------------------------------------------------
14	[00:00:00]	2025/04/26 14:51:21.194	{}	0	-	[info]	+617	C	()	testrunファイルを使用して初期化します。(testrun.global.properties)
15	[00:00:00]	2025/04/26 14:51:21.201	{}	0	-	[info]	+7	C	()	ログは次の場所に出力します。 file:////Users/wave1008/Downloads/TestResults/testConfig@a/2025-04-26_145120/VisionTestClass1/
16	[00:00:01]	2025/04/26 14:51:21.374	{}	0	-	[info]	+173	C	()	Loading config.(configFile=/Users/wave1008/github/ldi-github/shirates-core-vision-samples_ja/testConfig/android/testConfig@a.json, profileName=Pixel 8(Android 14))
17	[00:00:01]	2025/04/26 14:51:22.037	{}	0	-	[info]	+663	C	()	Scanning macro under '/Users/wave1008/github/ldi-github/shirates-core-vision-samples_ja/src/test/kotlin'
18	[00:00:01]	2025/04/26 14:51:22.059	{}	0	-	[info]	+22	C	()	Registering macro. (macro.ios.iOSSettingsMacro)
19	[00:00:01]	2025/04/26 14:51:22.087	{}	0	-	[info]	+28	C	()	Registering macro. (macro.android.AndroidSettingsMacro)
20	[00:00:01]	2025/04/26 14:51:22.091	{}	0	-	[info]	+4	C	()	Registering macro. (macro.android.FilesMacro)
21	[00:00:01]	2025/04/26 14:51:22.095	{}	0	-	[info]	+4	C	()	Registering macro. (macro.android.MapsMacro)
22	[00:00:01]	2025/04/26 14:51:22.099	{}	0	-	[info]	+4	C	()	Registering macro. (macro.MacroObject1)
23	[00:00:01]	2025/04/26 14:51:22.155	{}	0	-	[info]	+56	C	()	Classifier files loaded.(CheckStateClassifier, 2 labels, directory=/Users/wave1008/github/ldi-github/shirates-core-vision-samples_ja/build/vision/classifiers/CheckStateClassifier/1)
24	[00:00:01]	2025/04/26 14:51:22.162	{}	0	-	[info]	+7	C	()	Learning skipped. Updated file not found. (CheckStateClassifier(1), /Users/wave1008/github/ldi-github/shirates-core-vision-samples_ja/vision/classifiers/CheckStateClassifier/1)
25	[00:00:01]	2025/04/26 14:51:22.173	{}	0	-	[info]	+11	C	()	Classifier files loaded.(ScreenClassifier, 20 labels, directory=/Users/wave1008/github/ldi-github/shirates-core-vision-samples_ja/build/vision/classifiers/ScreenClassifier/1)
26	[00:00:01]	2025/04/26 14:51:22.179	{}	0	-	[info]	+6	C	()	Learning skipped. Updated file not found. (ScreenClassifier(1), /Users/wave1008/github/ldi-github/shirates-core-vision-samples_ja/vision/classifiers/ScreenClassifier/1)
27	[00:00:01]	2025/04/26 14:51:22.191	{}	0	-	[info]	+12	C	()	Classifier files loaded.(DefaultClassifier, 32 labels, directory=/Users/wave1008/github/ldi-github/shirates-core-vision-samples_ja/build/vision/classifiers/DefaultClassifier/1)
28	[00:00:01]	2025/04/26 14:51:22.199	{}	0	-	[info]	+8	C	()	Learning skipped. Updated file not found. (DefaultClassifier(1), /Users/wave1008/github/ldi-github/shirates-core-vision-samples_ja/vision/classifiers/DefaultClassifier/1)
TextIndex: [開発者向けオプション画面] priority=1, length=3, [開発者向けオプション, 開発者向けオプションを, 使用], imageIndexFile=/Users/wave1008/github/ldi-github/shirates-core-vision-samples_ja/vision/classifiers/ScreenClassifier/@a/Android設定/[開発者向けオプション画面]/#1.png
29	[00:00:01]	2025/04/26 14:51:22.223	{}	0	-	[info]	+24	C	()	Initializing TestDriver.(profileName=Pixel 8(Android 14))
30	[00:00:01]	2025/04/26 14:51:22.224	{}	0	-	[info]	+1	C	()	noLoadRun: false
31	[00:00:01]	2025/04/26 14:51:22.224	{}	0	-	[info]	+0	C	()	boundsToRectRatio: 1
32	[00:00:01]	2025/04/26 14:51:22.225	{}	0	-	[info]	+1	C	()	reuseDriver: true
33	[00:00:01]	2025/04/26 14:51:22.225	{}	0	-	[info]	+0	C	()	autoScreenshot: true
34	[00:00:01]	2025/04/26 14:51:22.226	{}	0	-	[info]	+1	C	()	onChangedOnly: true
35	[00:00:01]	2025/04/26 14:51:22.226	{}	0	-	[info]	+0	C	()	onCondition: true
36	[00:00:01]	2025/04/26 14:51:22.227	{}	0	-	[info]	+1	C	()	onAction: true
37	[00:00:01]	2025/04/26 14:51:22.227	{}	0	-	[info]	+0	C	()	onExpectation: true
38	[00:00:01]	2025/04/26 14:51:22.228	{}	0	-	[info]	+1	C	()	onExecOperateCommand: true
39	[00:00:01]	2025/04/26 14:51:22.228	{}	0	-	[info]	+0	C	()	onCheckCommand: true
40	[00:00:01]	2025/04/26 14:51:22.229	{}	0	-	[info]	+1	C	()	onScrolling: true
41	[00:00:01]	2025/04/26 14:51:22.229	{}	0	-	[info]	+0	C	()	manualScreenshot: true
42	[00:00:01]	2025/04/26 14:51:22.230	{}	0	-	[info]	+1	C	()	retryMaxCount: 2
43	[00:00:01]	2025/04/26 14:51:22.231	{}	0	-	[info]	+1	C	()	retryIntervalSeconds: 2.0
44	[00:00:01]	2025/04/26 14:51:22.231	{}	0	-	[info]	+0	C	()	shortWaitSeconds: 1.5
45	[00:00:01]	2025/04/26 14:51:22.232	{}	0	-	[info]	+1	C	()	waitSecondsOnIsScreen: 15.0
46	[00:00:01]	2025/04/26 14:51:22.232	{}	0	-	[info]	+0	C	()	waitSecondsForLaunchAppComplete: 15.0
47	[00:00:01]	2025/04/26 14:51:22.233	{}	0	-	[info]	+1	C	()	waitSecondsForAnimationComplete: 0.5
48	[00:00:01]	2025/04/26 14:51:22.234	{}	0	-	[info]	+1	C	()	waitSecondsForConnectionEnabled: 8.0
49	[00:00:01]	2025/04/26 14:51:22.234	{}	0	-	[info]	+0	C	()	swipeDurationSeconds: 3.0
50	[00:00:01]	2025/04/26 14:51:22.235	{}	0	-	[info]	+1	C	()	flickDurationSeconds: 0.3
51	[00:00:01]	2025/04/26 14:51:22.235	{}	0	-	[info]	+0	C	()	swipeMarginRatio: 0.0
52	[00:00:01]	2025/04/26 14:51:22.236	{}	0	-	[info]	+1	C	()	scrollVerticalStartMarginRatio: 0.15
53	[00:00:01]	2025/04/26 14:51:22.236	{}	0	-	[info]	+0	C	()	scrollVerticalEndMarginRatio: 0.1
54	[00:00:01]	2025/04/26 14:51:22.237	{}	0	-	[info]	+1	C	()	scrollHorizontalStartMarginRatio: 0.2
55	[00:00:01]	2025/04/26 14:51:22.237	{}	0	-	[info]	+0	C	()	scrollHorizontalEndMarginRatio: 0.1
56	[00:00:01]	2025/04/26 14:51:22.238	{}	0	-	[info]	+1	C	()	tapHoldSeconds: 0.0
57	[00:00:01]	2025/04/26 14:51:22.238	{}	0	-	[info]	+0	C	()	tapAppIconMethod: auto
58	[00:00:01]	2025/04/26 14:51:22.239	{}	0	-	[info]	+1	C	()	tapAppIconMacro: 
59	[00:00:01]	2025/04/26 14:51:22.239	{}	0	-	[info]	+0	C	()	enableCache: true
60	[00:00:01]	2025/04/26 14:51:22.240	{}	0	-	[info]	+1	C	()	syncWaitSeconds: 1.8
61	[00:00:02]	2025/04/26 14:51:22.534	{}	0	-	[info]	+294	C	()	Running device found. (udid=emulator-5554, avd=Pixel_8_Android_14)
62	[00:00:02]	2025/04/26 14:51:22.535	{}	0	-	[info]	+1	C	()	接続された端末が見つかりました。(Pixel_8_Android_14:5554, Android 14, emulator-5554)
63	[00:00:02]	2025/04/26 14:51:22.665	{}	0	-	[info]	+130	C	()	Terminating Appium Server. (pid=22986, port=4722)
64	[00:00:02]	2025/04/26 14:51:22.685	{}	0	-	[info]	+20	C	()	Starting Appium Server.
65	[00:00:02]	2025/04/26 14:51:22.686	{}	0	-	[info]	+1	C	()	appium --session-override --relaxed-security --log /Users/wave1008/Downloads/TestResults/testConfig@a/2025-04-26_145120/VisionTestClass1/appium_2025-04-26_145122537.log --port 4722
66	[00:00:05]	2025/04/26 14:51:25.954	{}	0	-	[info]	+3268	C	()	Appium Serverを開始しました。 (pid=23767, port=4722)
67	[00:00:06]	2025/04/26 14:51:26.973	{}	0	-	[info]	+1019	C	()	Appium Serverへ接続しています。(http://127.0.0.1:4722/)
68	[00:00:12]	2025/04/26 14:51:32.870	{}	0	-	[info]	+5897	C	()	implicitlyWaitSeconds: 5.0
69	[00:00:12]	2025/04/26 14:51:32.999	{}	0	-	[info]	+129	C	()	(settings) always_finish_activities: 0
70	[00:00:15]	2025/04/26 14:51:35.595	{}	0	-	[info]	+2596	C	()	Syncing screen.(isSame: false, changed: false, matchRate: 0.0, distance=1.0)
71	[00:00:16]	2025/04/26 14:51:36.839	{}	0	-	[info]	+1244	C	()	Syncing screen.(isSame: true, changed: true, matchRate: 1.0, distance=0.0)
72	[00:00:16]	2025/04/26 14:51:36.841	{}	0	-	[screenshot]	+2	C	()	screenshot: 72.png
73	[00:00:17]	2025/04/26 14:51:37.717	{}	0	-	[info]	+876	C	()	72_[72.png]_recognizeText_rectangles.png
2025-04-26 14:51:37.912 java[23745:292689] +[IMKClient subclass]: chose IMKClient_Modern
2025-04-26 14:51:37.912 java[23745:292689] +[IMKInputSession subclass]: chose IMKInputSession_Modern
74	[00:00:18]	2025/04/26 14:51:38.528	{}	0	-	[info]	+811	C	()	[Android設定トップ画面] found by matchTextScoreRate
75	[00:00:18]	2025/04/26 14:51:38.532	{}	0	-	[info]	+4	C	()	[recognizeScreen] in 1.688 sec
76	[00:00:18]	2025/04/26 14:51:38.533	{}	0	-	[info]	+1	C	()	currentScreen=[Android設定トップ画面]
77	[00:00:18]	2025/04/26 14:51:38.534	{}	0	-	[info]	+1	C	()	AppiumDriver initialized.
78	[00:00:18]	2025/04/26 14:51:38.535	{}	0	-	[-]	+1	C	()	testMode: Vision
79	[00:00:18]	2025/04/26 14:51:38.535	{}	0	-	[-]	+0	C	()	testrun: testrun.global.properties
80	[00:00:18]	2025/04/26 14:51:38.536	{}	0	-	[-]	+1	C	()	testConfigName: testConfig@a(/Users/wave1008/github/ldi-github/shirates-core-vision-samples_ja/testConfig/android/testConfig@a.json)
81	[00:00:18]	2025/04/26 14:51:38.537	{}	0	-	[-]	+1	C	()	profileName: Pixel 8(Android 14)
82	[00:00:18]	2025/04/26 14:51:38.538	{}	0	-	[-]	+1	C	()	appIconName: 設定
83	[00:00:18]	2025/04/26 14:51:38.538	{}	0	-	[-]	+0	C	()	(capabilities)
84	[00:00:18]	2025/04/26 14:51:38.539	{}	0	-	[-]	+1	C	()	appium:newCommandTimeout: 300
85	[00:00:18]	2025/04/26 14:51:38.540	{}	0	-	[-]	+1	C	()	appium:takesScreenshot: true
86	[00:00:18]	2025/04/26 14:51:38.540	{}	0	-	[-]	+0	C	()	appium:warnings: {}
87	[00:00:18]	2025/04/26 14:51:38.541	{}	0	-	[-]	+1	C	()	appium:deviceApiLevel: 34
88	[00:00:18]	2025/04/26 14:51:38.542	{}	0	-	[-]	+1	C	()	appium:automationName: UiAutomator2
89	[00:00:18]	2025/04/26 14:51:38.542	{}	0	-	[-]	+0	C	()	appium:locationContextEnabled: false
90	[00:00:18]	2025/04/26 14:51:38.543	{}	0	-	[-]	+1	C	()	appium:deviceScreenSize: 1080x2400
91	[00:00:18]	2025/04/26 14:51:38.544	{}	0	-	[-]	+1	C	()	appium:deviceManufacturer: Google
92	[00:00:18]	2025/04/26 14:51:38.544	{}	0	-	[-]	+0	C	()	appium:enforceXPath1: true
93	[00:00:18]	2025/04/26 14:51:38.553	{}	0	-	[-]	+9	C	()	appium:udid: emulator-5554
94	[00:00:18]	2025/04/26 14:51:38.554	{}	0	-	[-]	+1	C	()	appium:pixelRatio: 2.625
95	[00:00:18]	2025/04/26 14:51:38.554	{}	0	-	[-]	+0	C	()	platformName: ANDROID
96	[00:00:18]	2025/04/26 14:51:38.555	{}	0	-	[-]	+1	C	()	appium:networkConnectionEnabled: true
97	[00:00:18]	2025/04/26 14:51:38.555	{}	0	-	[-]	+0	C	()	appium:locale: JP
98	[00:00:18]	2025/04/26 14:51:38.556	{}	0	-	[-]	+1	C	()	appium:deviceScreenDensity: 420
99	[00:00:18]	2025/04/26 14:51:38.556	{}	0	-	[-]	+0	C	()	appium:viewportRect: {left=0, top=132, width=1080, height=2268}
100	[00:00:18]	2025/04/26 14:51:38.557	{}	0	-	[-]	+1	C	()	appium:language: ja
101	[00:00:18]	2025/04/26 14:51:38.557	{}	0	-	[-]	+0	C	()	appium:avd: Pixel_8_Android_14
102	[00:00:18]	2025/04/26 14:51:38.558	{}	0	-	[-]	+1	C	()	appium:deviceModel: sdk_gphone64_arm64
103	[00:00:18]	2025/04/26 14:51:38.558	{}	0	-	[-]	+0	C	()	appium:platformVersion: 14
104	[00:00:18]	2025/04/26 14:51:38.558	{}	0	-	[-]	+0	C	()	appium:databaseEnabled: false
105	[00:00:18]	2025/04/26 14:51:38.559	{}	0	-	[-]	+1	C	()	appium:deviceUDID: emulator-5554
106	[00:00:18]	2025/04/26 14:51:38.559	{}	0	-	[-]	+0	C	()	appium:statBarHeight: 132
107	[00:00:18]	2025/04/26 14:51:38.560	{}	0	-	[-]	+1	C	()	appium:webStorageEnabled: false
108	[00:00:18]	2025/04/26 14:51:38.560	{}	0	-	[-]	+0	C	()	appium:appActivity: com.android.settings.Settings
109	[00:00:18]	2025/04/26 14:51:38.560	{}	0	-	[-]	+0	C	()	appium:deviceName: emulator-5554
110	[00:00:18]	2025/04/26 14:51:38.561	{}	0	-	[-]	+1	C	()	appium:javascriptEnabled: true
111	[00:00:18]	2025/04/26 14:51:38.561	{}	0	-	[-]	+0	C	()	appium:appPackage: com.android.settings
112	[00:00:18]	2025/04/26 14:51:38.565	{}	0	-	[-]	+4	C	()	settings
113	[00:00:18]	2025/04/26 14:51:38.667	{}	0	-	[-]	+102	C	()	always_finish_activities: 0
114	[00:00:18]	2025/04/26 14:51:38.668	{}	0	-	[-]	+1	C	()	(others)
115	[00:00:18]	2025/04/26 14:51:38.669	{}	0	-	[-]	+1	C	()	isEmulator: true
116	[00:00:18]	2025/04/26 14:51:38.670	{}	0	-	[-]	+1	C	()	hasOsaifuKeitai: false
117	[00:00:18]	2025/04/26 14:51:38.672	{}	0	-	[info]	+2	C	()	disableCache
118	[00:00:18]	2025/04/26 14:51:38.675	{}	0	-	[info]	+3	!	()	セットアップの実行が完了しました。(処理時間: 18.0 sec)
119	[00:00:18]	2025/04/26 14:51:38.680	{}	0	-	[info]	+5	C	()	テスト関数の実行が完了しました。(処理時間: 18.1 sec)
120	[00:00:18]	2025/04/26 14:51:38.680	{}	0	-	[info]	+0	C	()	End of Test function: testFunc1 [testFunc1()]
121	[00:00:18]	2025/04/26 14:51:38.707	{}	0	-	[WARN]	+27	C	()	scenario not implemented.

org.opentest4j.TestAbortedException: scenario not implemented.


	at shirates.core.testcode.UITestCallbackExtension.afterEach(UITestCallbackExtension.kt:387)
	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)

122	[00:00:19]	2025/04/26 14:51:39.718	{}	0	-	[info]	+1011	C	()	ログは次の場所に出力します。 file:////Users/wave1008/Downloads/TestResults/testConfig@a/2025-04-26_145120/VisionTestClass1/
Copying jar content _ReportScript.js to /Users/wave1008/Downloads/TestResults/testConfig@a/2025-04-26_145120/VisionTestClass1
Copying jar content _ReportStyle.css to /Users/wave1008/Downloads/TestResults/testConfig@a/2025-04-26_145120/VisionTestClass1
No scenario found. Outputting Spec-Report skipped.
123	[00:00:19]	2025/04/26 14:51:40.160	{}	0	-	[info]	+442	C	()	Quitting TestDriver.
124	[00:00:20]	2025/04/26 14:51:40.757	{}	0	-	[info]	+597	C	()	テストクラスの実行が完了しました。(処理時間: 20.5 sec)
Disconnected from the target VM, address: '127.0.0.1:60644', transport: 'socket'

Process finished with exit code 255
```

## テストを実行する (iOS)

1. `testrun.global.properties`を開きます。
2. `os=ios`を指定します。 <br> ![](../_images/testrun_global_properties_ios.png)
3. `testFunc1()`を右クリックして`Debug`を選択します。

#### コンソール出力

```
...
62	[00:00:06]	2025/04/26 14:40:32.250	{}	0	-	[info]	+7	C	()	Starting Appium Server.
63	[00:00:06]	2025/04/26 14:40:32.262	{}	0	-	[info]	+12	C	()	appium --session-override --relaxed-security --log /Users/wave1008/Downloads/TestResults/testConfig@i/2025-04-26_144026/VisionTestClass1/appium_2025-04-26_144032117.log --port 4722
64	[00:00:09]	2025/04/26 14:40:35.579	{}	0	-	[info]	+3317	C	()	Appium Serverを開始しました。 (pid=19583, port=4722)
65	[00:00:10]	2025/04/26 14:40:36.606	{}	0	-	[info]	+1027	C	()	Optimizing installing WebDriverAgent.
66	[00:00:10]	2025/04/26 14:40:36.738	{}	0	-	[info]	+132	C	()	WebDriverAgentRunner-Runner.app not found. Optimization skipped.
67	[00:00:10]	2025/04/26 14:40:36.750	{}	0	-	[info]	+12	C	()	Appium Serverへ接続しています。(http://127.0.0.1:4722/)
68	[00:00:10]	2025/04/26 14:40:36.756	{}	0	-	[info]	+6	C	()	注意：iOSDriverの初期化はWebDriverAgentのビルドとインストールのため数分かかる場合があります。
...
```

他のエラーが出る場合は [Error messages](../../../common/troubleshooting/error_warning_messages.md)を参照してください。

## @Orderアノテーション

JUnit 5の **@Order** アノテーションを使用することで実行順を指定できます。

## @DisplayNameアノテーション

JUnit 5の **@DisplayName** アノテーションを使用することでテストシナリオの説明を記載できます。

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

## @Nestedのサポートについて

JUnit 5の `@Nested`アノテーションは使用しないでください。 これはshirates-coreのソフトウェアデザインに伴う制限です。

### Link

- [index](../../../index_ja.md)

