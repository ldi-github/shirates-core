# Configuring remote Appium Server (Classic)

You can use remote appium server. But there are some limitations.

### Limitations on remote Appium Server

1. Automatic device detection is not available. You have to configure profile explicitly.
2. Automatic restarting appium server process is not available on rerunning test scenario.
3. Automatic restarting virtual devices is not available on rerunning test scenario.
4. Vision mode is not supported.

## Example

1. Set `appiumServerUrl` to the remote appium server.

```properties
appiumServerUrl=http://10.0.0.104:4723/
```

2. Configure profile explicitly. See [Profile configuration](../../../common/parameter/profile_configuration.md)

ex.

```json
  "profiles": [
    {
      "profileName": "remote device",
      "capabilities": {
        "automationName": "XCUITest",
        "platformName": "iOS",
        "deviceName": "iPhone 15(iOS 17.2)",
        "bundleId": "com.apple.Preferences"
      }
    }
  ]
```

3. Start appium server on remote machine.

```
appium --session-override --relaxed-security
```

4. Run the test written in Shirates.

### Link

- [index](../../index.md)
