# Quick Start

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
```

#### (for Windows)

Download installation package.
https://nodejs.org/en/download/

<br>

**Note:** Use newer version to avoid troubles of installing appium.

<br>

### Appium 2.0

Appium 2.0 is required.

If you are already using appium, check version.

```
appium -v
```

If you have installed appium 1.x, uninstall it.

```
npm uninstall -g appium
```

Install appium 2.0 using npm.

```
npm install -g appium@next
appium -v
```

**Note:** Appium 2.0 is going to release. "@next" is required to install 2.0

See [Tested Environments](environments.md) to get tested version.

<br>
If you have already installed appium, uninstall it and install it again.

```
appium -v
npm uninstall -g appium
npm install -g appium@next
appium -v
```

#### Example

```
wave1008@SNB-M1 ~ % appium -v
2.0.0-beta.33
wave1008@SNB-M1 ~ % npm uninstall -g appium

removed 437 packages, and audited 1 package in 816ms

found 0 vulnerabilities
wave1008@SNB-M1 ~ % npm install -g appium@next

added 426 packages, and audited 427 packages in 16s

62 packages are looking for funding
  run `npm fund` for details

found 0 vulnerabilities
wave1008@SNB-M1 ~ % appium -v                 
2.0.0-beta.35
wave1008@SNB-M1 ~ % 
```

<br>

### UIAutomator2 driver

Install UIAutomator2 driver.

```
appium driver install uiautomator2
```

<br>
If you have already installed the driver, uninstall it and install it again.

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

```
appium driver install xcuitest
```

<br>
If you have already installed the driver, uninstall it and install it again.

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

### Setting Environment Variables (Windows only)

1. Open **Android Studio**.
2. Select `Tools > SDK Manager > Appearance & Behavior > System Settings > Android SDK`
3. Copy Android SDK Location.<br>
   ![](_images/android_sdk_location.png)
4. Set environment variable `ANDROID_SDK_ROOT`.<br>
   ![](_images/set_environment_variable_windows.png)
5. Edit environment variable `Path`. Add entries as follows.
    - `%ANDROID_SDK_ROOT%\emulator`
    - `%ANDROID_SDK_ROOT%\platform-tools`
    - `%ANDROID_SDK_ROOT%\tools`<br>
      ![](_images/set_environment_variable_windows_2.png)
6. Reboot the PC.

## Setting up AVD (Android Virtual Device)

### Create AVD for demo

1. Open **Android Studio**.
2. Select menu `Tools > Device Manager`.
3. Click `Create device`. <br>
   ![](_images/create_avd_1.png)


4. Select `Pixcel 3a` and click Next.<br>
   ![](_images/create_avd_2.png)


5. Select `S/API Level 31/Android 12.0 (Google Play)` and click `Next` (Google Play Store is required for demonstration
   using **Calculator** app). Select **arm64** image for M1 Mac, otherwise select **x86_64** image.<br>
   ![](_images/create_avd_3.png)


6. Set AVD Name to `Pixel 3a(Android 12)` and click `Finish`.<br>
   ![](_images/create_avd_4.png)

<br>

## Demonstration

Let's see demonstration.

### Opening Project

1. Open **shirates-core** project directory in Finder or Explorer.
2. Right click `build.gradle.kts` and open with IntelliJ IDEA. <br>
   ![](_images/opening_project.png)

### Enable right-click test running

1. `IntelliJ IDEA > Preferences` (or `File > Settings`)
1. `Build, Execution, Deployment > Build Tools > Gradle`
1. Set `Run tests using` to `IntelliJ IDEA`

![](basic/_images/build_tools_gradle.png)

<br>

### Run AndroidSettingsDemo

1. Launch the AVD of Android 12 from **Device Manager**.
1. Open `shirates-core` project in IntelliJ, right click on `src/test/Kotlin/demo/AndroidSettingsDemo` and select
   **Debug 'AndroidSettingsDemo'**
1. You'll see logs in the Console like this.

#### Console output

```
Connected to the target VM, address: '127.0.0.1:59302', transport: 'socket'
lineNo	logDateTime	testCaseId	logType	group	message
1	2022/12/09 04:17:17.294	{}	[-]	()	----------------------------------------------------------------------------------------------------
2	2022/12/09 04:17:17.307	{}	[-]	()	///
3	2022/12/09 04:17:17.308	{}	[-]	()	/// Shirates 2.0.0
4	2022/12/09 04:17:17.308	{}	[-]	()	///
5	2022/12/09 04:17:17.309	{}	[-]	()	powered by Appium (io.appium:java-client:8.1.1)
6	2022/12/09 04:17:17.309	{}	[-]	()	----------------------------------------------------------------------------------------------------
7	2022/12/09 04:17:17.309	{}	[info]	(parameter)	testClass: demo.AndroidSettingsDemo
8	2022/12/09 04:17:17.309	{}	[info]	(parameter)	sheetName: AndroidSettingsDemo
9	2022/12/09 04:17:17.309	{}	[info]	(parameter)	logLanguage: 
10	2022/12/09 04:17:17.903	{}	[info]	()	Initializing with testRun.properties.(testConfig/android/androidSettings/testrun.properties)
...
```

<br>

### Check output reports

1. Click the hyperlink in the IntelliJ IDEA's console to open the log directory<br>
   ![](_images/hyper_link_to_testresults.png)
2. You'll see log and report files.<br>
   ![](_images/test_results.png)
3. First, open **_Report(simple).html** file. You'll see simple test report. Click on a line to highlight the screenshot
   image. Double-click on a line to show a larger image.
4. Second, open **_Report(detail).html** file. You'll see more information with log type "info".
5. At last, open **AndroidSettingsDemo@a.xlsx** file with MS-Excel or other compatible software.

### HTML-Report(simple)

![](_images/html_report.png)

### Spec-Report

AndroidSettingsDemo@a.xlsx

![](_images/spec_report.png)

### TestList

1. Open Downloads/TestResults in Finder (or Explorer).
1. Open **TestList_androidSettingsConfig.xlsx** file with MS-Excel or other compatible software.

**TestList_androidSettingsConfig.xlsx<br>**
![](_images/test_list.png)

<br>

### Run CalculatorDemo

1. Launch the AVD of Android 12 from **Device Manager**.
2. Open **Google Play Store** and install **Calculator**(Google LLC).
3. Open this project in IntelliJ, right click on `src/test/Kotlin/demo/CalculatorDemo` and select
   **Debug 'CalculatorDemo'**
4. You'll see the calculator test works.

![](_images/calculator_demo.png)

<br>

### Run iOSSettingsDemo

1. Open Xcode and setup iOS Simulator.(iPhone 13, iOS 15.5)

![](_images/setup_ios_simulator.png)

2. Open shirates-core project in IntelliJ, right click on `src/test/Kotlin/demo/iOSSettingsDemo` and select
   **Debug 'iOSSettingsDemo'**
3. You'll see the iOS Settings test works.

![](_images/ios_settings_demo.png)

### Link

- [index](index.md)
