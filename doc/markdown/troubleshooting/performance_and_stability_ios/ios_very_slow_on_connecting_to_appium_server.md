# Very slow on connecting Appium Server (iOS)

## Symptom

Very slow on connecting to Appium Server.

## Platform

iOS

## Cause

Appium driver executes building Web Driver Agent(WDA) and install it into device or simulator at first time. It takes a
minute or more.

## Solution

Wait for a minute or more.

For more information, see [Appium Server log](../appium_server/watching_appium_server_log.md).

### Link

- [Troubleshooting](../troubleshooting.md)
