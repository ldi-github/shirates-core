# Parameter configuration files

## Overview

You can change running parameters by parameter configuration files.

* **testrun file** (properties file)
* **testConfig file** (json file)

## testrun.properties

**testrun.properties** file is testrun file that can be placed anywhere in the project and can have an arbitrary name.
This file should be designated in test setup procedure by [@Testrun](../creating_testclass) annotation.

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

## Config --------------------
## [Android]
#android.configFile=
#android.profile=Android *
android.profile=Android 12

## [iOS]
#ios.configFile=
ios.profile=iPhone 13(15.5)

## Stub --------------------
#stubServerUrl=http://stub1

## Test mode --------------------
#noLoadRun=true

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
#enableInnerMacroLog=true
#enableInnerCommandLog=true
#enableSilentLog=true
#enableTapElementImageLog=true
#enableXmlSourceDump=false
#enableRetryLog=false
#enableWarnOnRetryError=true
#enableGetSourceLog=true
#enableTrace=true
#enableShellExecLog=true
#enableTimeMeasureLog=true
#enableImageMatchDebugLog=true
#testResults=
#testListDir=

## Screenshot --------------------
#screenshotScale=0.333333
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
#imageMatchingScale=
#imageMatchingThreshold=
#imageMatchingCandidateCount=

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

## TestDriver --------------------
#reuseDriver=
#retryMaxCount=
#retryTimeoutSeconds=
#retryIntervalSeconds=
#enableHealthCheck=false
#tapTestSelector=

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
#scrollVerticalMarginRatio=
#scrollHorizontalMarginRatio=
#tapHoldSeconds=
#syncWaitSeconds=

## Custom --------------------
#CustomObject.scan.dir=src/test/kotlin

## Macro --------------------
#MacroObject.scan.dir=src/test/kotlin

## Spec-Report --------------------
#specReport.replace.MANUAL=
#specReport.replace.MANUAL.reason=
#specReport.replace.SKIP=
#specReport.replace.SKIP.reason=

## misc
#android.swipeOffsetY=-20
#ios.swipeOffsetY=-5
#xmlSourceRemovePattern=
#boundsToRectRatio=
#ios.selectIgnoreTypes=XCUIElementTypeCell,XCUIElementTypeApplication
#android.titleSelector=<#action_bar||#toolbar||#app_bar>:descendant(${title}||@${title})
#ios.titleSelector=<.XCUIElementTypeNavigationBar>:descendant(.XCUIElementTypeStaticText&&${title})
#android.webTitleSelector=.android.webkit.WebView&&${webTitle}
#ios.webTitleSelector=<.XCUIElementTypeWebView>:descendant(${webTitle}&&visible=false)
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
android.profile=Android 12
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
      "profileName": "Android 12",
      "capabilities": {
        "platformVersion": "12"
      }
    },
    {
      "profileName": "Android 13",
      "capabilities": {
        "platformVersion": "13"
      }
    },
    {
      "profileName": "Android 12 with Tag1",
      "specialTags": "Tag1",
      "capabilities": {
        "platformVersion": "12"
      }
    },
    {
      "profileName": "Android 12 with Tat2 & Tag3",
      "specialTags": "Tag2, Tag3",
      "capabilities": {
        "platformVersion": "12"
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
