# Using Appium Inspector

When you want to create screen nickname files, use **Appium Inspector** for inspecting element information.

## Install

1. Get install package from [appium/appium-inspector](https://github.com/appium/appium-inspector/releases).
    - `Appium-Inspector-mac-202x.x.x.dmg` (for Mac)
    - `Appium-Inspector-windows-202x.x.x.exe` (for Windows)
2. Install the package.
3. Start Appium Inspector.

## Start Appium Server

1. Open terminal.
2. Start Appium Server process. Type just ```appium``` and enter.

```
wave1008@SNB-M1 ~ % appium
info Appium Setting NODE_PATH to '/opt/homebrew/lib/node_modules'
[Appium] Welcome to Appium v2.0.0-beta.44 (REV 0b030b74e2ac518bd0bc4158f96c449198f9957f)
[Appium] Attempting to load driver xcuitest...
[debug] [Appium] Requiring driver at /Users/wave1008/.appium/node_modules/appium-xcuitest-driver
[Appium] Attempting to load driver uiautomator2...
[debug] [Appium] Requiring driver at /Users/wave1008/.appium/node_modules/appium-uiautomator2-driver
[Appium] Appium REST http interface listener started on 0.0.0.0:4723
[Appium] Available drivers:
[Appium]   - xcuitest@4.11.1 (automationName 'XCUITest')
[Appium]   - uiautomator2@2.4.6 (automationName 'UiAutomator2')
[Appium] No plugins have been installed. Use the "appium plugin" command to install the one(s) you want to use.
```

See [Quick Start](../../quick-start.md) to install appium.

## Start Appium Inspector

1. Start Appium Inspector.
2. Show `Desired Capabilities` tab.
3. Edit parameters as follows.
   <br>![](../_images/desired_capability_android.png)<br>

```
{
  "appium:automationName": "UiAutomator2",
  "platformName": "Android",
  "appium:platformVersion": "12",
  "appium:appPackage": "com.android.settings",
  "appium:appActivity": "com.android.settings.Settings"
}
```

4. Start `Android 12 emulator`. (For setting up AVD for Android 12, see [Quick Start](../../quick-start.md))
5. Click `[Start Session]`
6. `Settings app` starts in the device. <br>The screen image is captured. You can inspect screen elements.
   <br>![](../_images/screen_captured_in_inspector.png)

## Finding unique attribute(s)

To identify screen element, unique key information is required. You can use external editor to examine.

1. Capture the screen you want to inspect in Appium Inspector.
2. Click `Copy XML Source to Clipboard`.
   <br>![](../_images/copy_xml_source_to_clipboard.png)
3. Open your favorite editor and paste it. Now you can search unique attribute(s).
   <br>![](../_images/finding_unique_attributes_in_editor.png)

### Link

- [Creating screen nickname file](creating_screen_nickname_file.md)
- [index](../../index.md)
