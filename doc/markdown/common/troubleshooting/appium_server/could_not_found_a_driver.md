# Could not find a driver

## Symptom

#### Android

`org.openqa.selenium.SessionNotCreatedException: Unable to create a new remote session. Please check the server log for more details. Original error: An unknown server-side error occurred while processing the command. Original error: Could not find a driver for automationName 'UiAutomator2' and platformName android'. Have you installed a driver that supports those capabilities? Run 'appium driver list --installed' to see.`

#### iOS

`org.openqa.selenium.SessionNotCreatedException: Unable to create a new remote session. Please check the server log for more details. Original error: An unknown server-side error occurred while processing the command. Original error: Could not find a driver for automationName 'XCUITest' and platformName ios'. Have you installed a driver that supports those capabilities? Run 'appium driver list --installed' to see.`

## Platform

Android, iOS

## Cause

Appium could not find a driver for some reason. You should (re)install it.

## Solution

1. Confirm appium drivers installed in your machine.

```
appium driver list
```

2. Uninstall driver.

```
appium driver uninstall [drivername]
```

3. (Re)Install driver.

```
appium driver install [drivername]
```

## See also

(v2) appium install driver deletes previous installed driver(s) when installing multiple drivers. #16674
https://github.com/appium/appium/issues/16674

### Link

- [Troubleshooting](../troubleshooting.md)
