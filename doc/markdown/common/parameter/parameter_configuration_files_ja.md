# パラメーター構成ファイル (Vision/Classic)

## 概要

実行時のパラメーターをパラメーター設定ファイルで変更できます。

* **testrun file** (propertiesファイル)
* **testConfig file** (jsonファイル)

## testrun.properties

**testrun.properties**ファイルはプロジェクト内の任意の場所に任意の名前で配置できます。
このファイルは [@Testrunアノテーション](../../classic/basic/creating_testclass/creating_testclass_ja.md )
によってテストのセットアップ中に指定されます。

```kotlin
@Testrun("testConfig/android/androidSettings/testrun.properties")
class Test1 : UITest() {

}
```

"**testConfig/testrun.properties**"は`@Testrun`の指定を省略した場合に適用されるデフォルトのファイルです。

```kotlin
class Test1 : UITest() {

}
```

上記は以下と同じ意味です。

```kotlin
@Testrun("testConfig/testrun.properties")
class Test1 : UITest() {

}
```

## testrun.global.properties

**testrun.global.properties**ファイルはプロジェクトディレクトリ下に配置されるファイルです。
このファイルは固定名であり、`@Testrun`アノテーションで指定する必要はありません。
このファイルで設定したパラメーターはプロジェクト内の全てのテストに適用されますが、他の構成ファイル(`testrun.properties`
および `testConfig.json`)でオーバーライドされます。

## testrunファイルの例

```
# testrun

## OS --------------------
#os=ios

## Test mode --------------------
#noLoadRun=true

## Config --------------------
## [Android]
#android.configFile=

# auto
android.profile=Android *

#by AVD Name
#android.profile=Pixel 8(Android 14)
#android.profile=Pixel 7(Android 14)
#android.profile=Pixel 6(Android 13)

#by platformVersion
#android.profile=14
#android.profile=13
#android.profile=Android 14

#by model and platformVersion
#android.profile=Pixel 8(14)
#android.profile=Pixel_6(13)

# by model
#android.profile=Pixel 8

# by udid
#android.profile=emulator-5554
#android.profile=93MAY0CY1M

## [iOS]
#ios.configFile=
# auto
ios.profile=iOS *

#by deviceName
#ios.profile=My iPhone

#by platformVersion
#ios.profile=17.4
#ios.profile=17.2

#by model and platformVersion
#ios.profile=iPhone 15(iOS 17.4)
#ios.profile=iPhone 15(iOS 17.2)
#ios.profile=iPhone 15 Pro Max(iOS 17.4)
#ios.profile=Hoge-01(16.1)

# by model
#ios.profile=iPhone 15
#ios.profile=iPhone 15 Pro Max
#ios.profile=Hoge-01

# by udid
#ios.profile=EDF2DD70-439D-40F3-8835-54EF8B7297EA
#ios.profile=80da80b8002e31e5ec8b1a0085a7e3c64d8b2d51


## Stub --------------------
#stubServerUrl=http://stub1

## Priority filter --------------------
#must=false
#should=false
#want=false
#none=false

## Log --------------------
#logLanguage=ja
#enableSyncLog=false
#enableTestList=false
#enableTestClassList=false
#enableSpecReport=false
#enableRelativeCommandTranslation=false
#enableInnerMacroLog=true
#enableInnerCommandLog=true
#enableSilentLog=true
#enableTapElementImageLog=true
#enableXmlSourceDump=false
#enableRetryLog=false
#enableWarnOnRetryError=true
#enableWarnOnSelectTimeout=true
#enableGetSourceLog=true
#enableTrace=true
#enableTimeMeasureLog=true
#enableStopWatchLog=true
#enableShellExecLog=true
#enableImageMatchDebugLog=true
#enableIsInViewLog=true
#enableIsSafeLog=false
#enableIsScreenLog=true
#testResults=
#testListDir={TEST_RESULTS}
#reportIndexDir={DIRECTORY_FOR_TEST_CONFIG}

## String Comparing --------------------
#strictCompareMode=true
#keepZenkakuSpace=false
#keepLF=true
#keepTAB=true
#waveDashToFullWidthTilde=false
#compressWhitespaceCharacters=false
#trimString=false

## Screenshot --------------------
#screenshotScale=0.333333
#screenshotIntervalSeconds=0.5
#autoScreenshot=false
#onChangedOnly=false
#onCondition=false
#onAction=false
#onExpectation=false
#onExecOperateCommand=false
#onCheckCommand=false
#onScrolling=false
#manualScreenshot=false

## Image Matching --------------------
#enableImageAssertion=false
#imageMatchingScale=
#imageMatchingThreshold=
#imageMatchingCandidateCount=

## Emulator/Simulator
emulatorOptions=-no-snapshot
#deviceStartupTimeoutSeconds=
#deviceWaitSecondsAfterStartup=
#enableWdaInstallOptimization=false
#emulatorPort=5554

## Appium --------------------
appiumServerUrl=http://127.0.0.1:4720/
appiumPath=appium
appiumArgs=--session-override --relaxed-security
#appiumArgsSeparator=
#appiumServerStartupTimeoutSeconds=
#appiumSessionStartupTimeoutSeconds=
#implicitlyWaitSeconds=
#appPackageFile=
#appPackageDir=
#packageOrBundleId=
#startupPackageOrBundleId=
#startupActivity=
## UIAutomator 2 --------------------
#enforceXPath1=false

## Appium Proxy --------------------
#appiumProxyReadTimeoutSeconds=
#appiumProxyGetSourceTimeoutSeconds=

## TestDriver --------------------
#reuseDriver=
#retryMaxCount=
#retryTimeoutSeconds=
#retryIntervalSeconds=
#enableHealthCheck=false
#tapTestSelector=
#enableAutoSyncAndroid=true
#enableAutoSyncIos=true
#enableLaunchOnScenario=false
#launchAppMethod=auto
launchAppMethod=tapAppIcon
#launchAppMethod=[My Launch Macro]
#enableRerunScenario=false
#enableAlwaysRerunOnErrorAndroid=true
#enableAlwaysRerunOnErrorIos=true
#scenarioTimeoutSeconds=
#scenarioMaxCount=
#enableRerunOnScreenshotBlackout=false
#screenshotBlackoutThreshold=0.99999
#enableRestartDeviceOnResettingAppiumSession=true
#safeCpuLoad=80
#enableWaitCpuLoad=false
#enableWaitCpuLoadPrintDebug=true

## App operation --------------------
#appIconName=
#tapAppIconMethod=
#tapAppIconMacro=
#shortWaitSeconds=
#waitSecondsForLaunchAppComplete=
#waitSecondsForAnimationComplete=
#waitSecondsOnIsScreen=
#waitSecondsForConnectionEnabled=
#swipeDurationSeconds=
#flickDurationSeconds=
#swipeMarginRatio=
#scrollVerticalStartMarginRatio=
#scrollVerticalEndMarginRatio=
#scrollHorizontalStartMarginRatio=
#scrollHorizontalEndMarginRatio=
#scrollToEdgeBoost=
#scrollIntervalSeconds=
#scrollMaxCount=
#tapHoldSeconds=
#enableCache=false
#findWebElementTimeoutSeconds=
#syncWaitSeconds=
#syncMaxLoopCount=

## Custom --------------------
#CustomObject.scan.dir=src/test/kotlin

## Macro --------------------
#MacroObject.scan.dir=src/test/kotlin

## Spec-Report --------------------
#specReport.exclude.detail=true
#specReport.SKIP.reason=
#specReport.EXCLUDED.reason=

## misc
#android.statBarHeight=
#ios.statBarHeight=
#android.swipeOffsetY=-20
#ios.swipeOffsetY=-5
#xmlSourceRemovePattern=
#boundsToRectRatio=
#ios.selectIgnoreTypes=XCUIElementTypeCell,XCUIElementTypeApplication
#android.titleSelector=<#action_bar||#toolbar||#app_bar>:descendant(${title}||@${title})
#ios.titleSelector=<.XCUIElementTypeNavigationBar>:descendant(.XCUIElementTypeStaticText&&${title})
#android.webTitleSelector=.android.webkit.WebView&&${webTitle}
#ios.webTitleSelector=<.XCUIElementTypeWebView>:descendant(${webTitle}&&visible=*)
#jquerySource=
```

## testConfigファイル

**testConfigファイル**はプロジェクト内の任意の場所に任意の名前で配置できるjsonファイルです。このファイルはtestrunファイルで指定する必要があります。

**testrunファイル**

```properties
## OS --------------------
os=android
## Config --------------------
## [Android]
android.configFile=testConfig/android/androidSettings/androidSettingsConfig.json
android.profile=Android 14
```

**testConfig**ファイルにおいては、テスト用のデバイスに対するプロファイルを定義し、Appiumの`desired capabilities`
を設定することができます。

```
{
  "testConfigName": "androidSettingsConfig",

  "appIconName": "Settings",

  "packageOrBundleId": "com.android.settings",

  "startupPackageOrBundleId": "com.android.settings",
  "startupActivity": "com.android.settings.Settings",

  "settings": {
    "always_finish_activities": "0"
  },

  "dataset": {
    "accounts": "testConfig/android/androidSettings/dataset/accounts.json",
    "apps": "testConfig/android/androidSettings/dataset/apps.json",
    "data": "testConfig/android/androidSettings/dataset/data.json"
  },

  "screens": {
    "import": [
      "testConfig/android/calculator/screens",
      "testConfig/android/misc/screens",
      "testConfig/android/playStore/screen"
    ]
  },

  "capabilities": {
    "language": "en",
    "locale": "US"
  },

  "profiles": [
    {
      "profileName": "Android 14",
      "capabilities": {
        "platformVersion": "14"
      }
    },
    {
      "profileName": "Android 13",
      "capabilities": {
        "platformVersion": "13"
      }
    },
    {
      "profileName": "Android 14 with Tag1",
      "specialTags": "Tag1",
      "capabilities": {
        "platformVersion": "14"
      }
    },
    {
      "profileName": "Android 14 with Tat2 & Tag3",
      "specialTags": "Tag2, Tag3",
      "capabilities": {
        "platformVersion": "14"
      }
    },

    {
      "profileName": "emulator-5556",
      "capabilities": {
        "udid": "emulator-5556"
      }
    }

  ]

}
```

## 優先順位

testConfigファイル > testrun.propertiesファイル > testrun.global.propertiesファイル

### Link

- [index(Vision)](../../index_ja.md)
- [index(Classic)](../../classic/index_ja.md)