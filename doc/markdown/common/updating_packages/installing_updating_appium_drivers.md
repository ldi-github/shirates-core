# Installing/Updating Appium & drivers (Vision/Classic)

## Appium

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

# Example

## Updating all latest (for macOS)

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

## Updating all latest (for Windows)

```
appium -v
appium driver list

npm uninstall -g appium
npm install -g appium

appium driver uninstall uiautomator2
appium driver install uiautomator2

appium -v
appium driver list
```

## Updating all latest (for Linux)

```
appium -v
appium driver list

sudo npm uninstall -g appium
sudo npm install -g appium

appium driver uninstall uiautomator2
appium driver install uiautomator2

appium -v
appium driver list
```

### Link

- [index(Vision)](../../index.md)
- [index(Classic)](../../classic/index.md)
