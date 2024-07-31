# Parameter configuration files

## Overview

You can change running parameters by parameter configuration files.

* **testrun file** (properties file)
* **testConfig file** (json file)

## testrun.properties

**testrun.properties** file is testrun file that can be placed anywhere in the project and can have an arbitrary name.
This file should be designated in test setup procedure
by [@Testrun annotation](../creating_testclass/creating_testclass.md).

```kotlin
@Testrun("testConfig/android/androidSettings/testrun.properties")
class Test1 : UITest() {

}
```

"**testConfig/testrun.properties**" is default and applied when `@Testrun` is omitted.

```kotlin
class Test1 : UITest() {

}
```

The above is equivalent to below.

```kotlin
@Testrun("testConfig/testrun.properties")
class Test1 : UITest() {

}
```

## testrun.global.properties

**testrun.global.properties** file is the testrun file that is placed under the project directory. This file is fixed
name file and should not be designated by `@Testrun` annotation. The parameters in this file is effective to all tests
in
this project, but they can be overridden by other configuration files(`testrun.properties` or `testConfig.json`).

## Example of testrun file

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
#excludeItemExpectation=true

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
#specReport.replace.MANUAL.reason=
#specReport.replace.SKIP.reason=
#specReport.replace.EXCLUDED.reason=

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

## testConfig file

**testConfig file** is json file that can be placed anywhere in the project and can have an arbitrary name. This file
should be designated in test setup procedure by the testrun file.

**testrun file**

```properties
## OS --------------------
os=android
## Config --------------------
## [Android]
android.configFile=testConfig/android/androidSettings/androidSettingsConfig.json
android.profile=Android 14
```

In **testConfig** file, you can set test **profiles** for test devices and `desired capabilities` of appium.

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

## Priority

testConfig file > testrun.properties > testrun.global.properties

### Link

- [index](../../index.md)
