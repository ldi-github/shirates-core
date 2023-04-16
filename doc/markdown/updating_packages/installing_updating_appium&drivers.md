# Installing/Updating Appium & drivers

## Appium 2.0

### Installing

```
npm install -g appium@next
appium -v
```

**Note:** Appium 2.0 is going to release. "@next" is required to install 2.0

### Re-Installing

```
appium -v
npm uninstall -g appium
npm install -g appium@next
appium -v
```

**Note:** Appium 2.0 is going to release. "@next" is required to install 2.0

See [Tested Environments](../environments.md) to get tested version.

<hr>

## UIAutomator2 driver

### Installing

```
appium driver install uiautomator2
appium driver list
```

### Re-Installing

```
appium driver list
appium driver uninstall uiautomator2
appium driver install uiautomator2
appium driver list
```

See [Tested Environments](../environments.md) to get tested version.

<hr>

## XCUITest driver(Mac only)

### Installing

```
appium driver install xcuitest
appium driver list
```

### Re-Installing

```
appium driver list
appium driver uninstall xcuitest
appium driver install xcuitest
appium driver list
```

See [Tested Environments](../environments.md) to get tested version.

<hr>

## Example

### Updating all latest

Copy this and paste on you terminal.

```
appium -v
appium driver list

npm uninstall -g appium
npm install -g appium@next

appium driver uninstall uiautomator2
appium driver uninstall xcuitest

appium driver install uiautomator2
appium driver install xcuitest

appium -v
appium driver list
```

#### Console

```
2.0.0-beta.64
✔ Listing available drivers
- uiautomator2@2.15.0 [installed (npm)]
- xcuitest@4.21.33 [installed (npm)]
- mac2 [not installed]
- espresso [not installed]
- safari [not installed]
- gecko [not installed]
- chromium [not installed]

removed 480 packages in 608ms

added 480 packages in 3s

48 packages are looking for funding
  run `npm fund` for details
✔ Successfully uninstalled driver 'uiautomator2'
✔ Successfully uninstalled driver 'xcuitest'
✔ Installing 'uiautomator2' using NPM install spec 'appium-uiautomator2-driver'
ℹ Driver uiautomator2@2.15.0 successfully installed
- automationName: UiAutomator2
- platformNames: ["Android"]
✔ Installing 'xcuitest' using NPM install spec 'appium-xcuitest-driver'
ℹ Driver xcuitest@4.21.33 successfully installed
- automationName: XCUITest
- platformNames: ["iOS","tvOS"]
2.0.0-beta.64
✔ Listing available drivers
- uiautomator2@2.15.0 [installed (npm)]
- xcuitest@4.21.33 [installed (npm)]
- mac2 [not installed]
- espresso [not installed]
- safari [not installed]
- gecko [not installed]
- chromium [not installed]
```

### Link

- [index](../index.md)
