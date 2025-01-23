# Quick Start (Shirates/Vision) for macOS

## Environment

See [Tested Environments](../environments.md) before installation.

**Note:** AI-Vision feature is for only macOS currently.

## Installation

Install following prerequisite tools.

**Note:** Do not use OS account with username contains non-ASCII characters or white spaces. Some of following tools
don't work well.


<br>

### IntelliJ IDEA

If you have not installed, download Ultimate or COMMUNITY and install it.
(COMMUNITY is opensource product)

https://www.jetbrains.com/idea/

<br>

### Android Studio

If you have not installed, download Android Studio and install it.

https://developer.android.com/studio

<br>

### Xcode (Mac only)

If you have not installed, search Xcode in App Store and install it.

<br>

### Command Line Tools for Xcode (Mac only)

If you have not installed, open terminal window and run this command.

```
xcode-select --install
```

<br>

### Homebrew (Mac only)

If you have not installed, go to https://brew.sh/ and install it.

<br>

### Java Development Kit (JDK)

If you have not installed, search installation guide and install it.

<br>

### node & npm

If you have not installed, install it.

#### (for Mac)

You can install NPM with brew. Open terminal window and type these.

```
brew install node
node -v
npm -v
```

**Note:** Use newer version to avoid troubles of installing appium.

<br>

### Appium 2.0

Appium 2.0 is required.

**New install**

```
npm uninstall -g appium
npm install -g appium
appium -v
```

If you are already using appium, update to the latest version.

**Update install**

```
appium -v
npm uninstall -g appium
npm install -g appium
appium -v
```

See [Tested Environments](environments.md) to get tested version.

```
appium -v
npm uninstall -g appium
npm install -g appium
appium -v
```

<br>

### UIAutomator2 driver

Install UIAutomator2 driver.

**New install**

```
appium driver install uiautomator2
```

**Update install**

```
appium driver list
appium driver uninstall uiautomator2
appium driver install uiautomator2
appium driver list
```

See [Tested Environments](environments.md) to get tested version.

<br>

### XCUITest driver(Mac only)

Install XCUITest driver.

**New install**

```
appium driver install xcuitest
```

**Update install**

```
appium driver list
appium driver uninstall xcuitest
appium driver install xcuitest
appium driver list
```

See [Tested Environments](environments.md) to get tested version.

<br>

### Setting Environment Variables (Mac only)

Set environment variables in initializing script (.zshrc or others).

#### Example

```
export ANDROID_SDK_ROOT=/Users/$USER/Library/Android/sdk
export PATH=$ANDROID_SDK_ROOT/emulator:$ANDROID_SDK_ROOT/tools:$ANDROID_SDK_ROOT/platform-tools:$PATH
```

**Note:** Execute log out/log in to take effect above settings.

#### Example

```
export ANDROID_SDK_ROOT=/home/$USER/Android/Sdk
export PATH=$ANDROID_SDK_ROOT/emulator:$ANDROID_SDK_ROOT/tools:$ANDROID_SDK_ROOT/platform-tools:$PATH
```

## Setting up AVD (Android Virtual Device)

### Create AVD for demo

1. Open **Android Studio**.
2. Select menu `Tools > Device Manager`.
3. Click `[+]`. <br>
   ![](../_images/create_avd_1.png)
4. Select `Pixcel 8` and click Next.<br>
   ![](../_images/create_avd_2.png)
5. Select `UsideDownCake 34 Android 14.0 (Google Play)` and click `Next` (Google Play Store is required for
   demonstration using **Calculator** app). Select **arm64** image for Mac.<br>
   ![](../_images/create_avd_3.png)
6. Set AVD Name to `Pixel 8(Android 14)`.<br>
   Set `Enable device frame` off.<br>
   Click `Finish`<br>
   ![](../_images/create_avd_4.png)

<br>

![](_images/mlmodels.png)

## Setting up shirates-vision-server

1. Get shirates-vision-server from [shirates-vision-server](https://github.com/ldi-github/shirates-vision-server).
2. Open the project (open Package.swift with Xcode). Wait a while for the background process to finish.
3. Select Product > Destination > My Mac.
4. Select Product > Run. You can see `[ NOTICE ] Server started on http://127.0.0.1:8081`.

## Demonstration

Let's see demonstration.

### Getting shirates-core-vision-samples

1. Get shirates-core-vision-samples_en
   from [shirates-core-vision-samples_en](https://github.com/ldi-github/shirates-core-vision-samples_en).

### Opening Project

1. Open **shirates-core-vision-samples_en** project directory in Finder.
2. Right click `build.gradle.kts` and open with IntelliJ IDEA. <br>
   ![](_images/opening_project.png)

### Enable right-click test running

1. `IntelliJ IDEA > Settings` (or `File > Settings`)
1. `Build, Execution, Deployment > Build Tools > Gradle`
1. Set `Run tests using` to `IntelliJ IDEA`

![](_images/build_tools_gradle.png)

<br>

### Run AndroidSettingsVisionDemo

1. Launch the AVD of Android 14 from **Device Manager**.
1. Open `shirates-core-vision-samples_en` project in IntelliJ, right click on
   `kotlin/demo/vision/AndroidSettingsVisionDemo` and
   select
   **Debug 'AndroidSettingsVisionDemo'** ![](_images/android_settings_vision_demo.png)
3. You'll see logs in the console like this. ![](_images/console_android.png)
4. Click the link to open the log directory.
5. Open **_Report(simple).html**. <br> ![](_images/log_directory.png) <br> ![](_images/report_simple.png) <br><br>
6. Open **AndroidSettingsVisionDemo@a.xlsx**. <br> ![](_images/android_settings_vision_demo_xls.png)

<br>

### Run iOSSettingsVisionDemo

1. Open Xcode and setup iOS Simulator. `iPhone 16(iOS 18.2)`

2. Open `shirates-core-vision-samples_en` project in IntelliJ, right click on
   `src/test/Kotlin/demo/vision/iOSSettingsVisionDemo`
   and select
   **Debug 'iOSSettingsVisionDemo'**
3. You'll see logs in the console like this. ![](_images/console_ios.png)
4. Click the link to open the log directory.
5. Open **_Report(simple).html**. <br> ![](_images/log_directory.png) <br> ![](_images/report_simple_ios.png) <br><br>
6. Open **iOSSettingsVisionDemo@i.xlsx**. <br> ![](_images/ios_settings_vision_demo.png)

### Link

- [index](vision-index.md)
