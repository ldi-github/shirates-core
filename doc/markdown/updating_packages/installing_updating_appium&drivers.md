# Installing/Updating Appium & drivers

## Appium 2.0

### Installing

```
npm install -g appium
appium -v
```

### Re-Installing

```
appium -v
npm uninstall -g appium
npm install -g appium
appium -v
```

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
npm install -g appium

appium driver uninstall uiautomator2
appium driver uninstall xcuitest

appium driver install uiautomator2
appium driver install xcuitest

appium -v
appium driver list
```

### Link

- [index](../index.md)
