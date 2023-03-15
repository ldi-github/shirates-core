# installApp, removeApp

You can install app by app package file.

Put your app's package file (`.apk` or `.app` or `.ipa`) in Download directory, set app file path to `appPackageFile` in
`testConfig.json`.

## iOS package file (.app or .ipa)

Use `.app` on simulator. Use `.ipa` on real device.

## Example

This code demonstrates how to reinstall.

```kotlin
if (it.isAppInstalled()) {
    it.removeApp()
}
it.installApp()
```

### testConfig@a.json

This is a sample configuration for Android apps.

```
{
  "testConfigName": "st@a",

  "appIconName": "You app",

  "appPackageFile": "your_app_package.apk",

  "packageOrBundleId": "com.example.android.yourapp",

  "startupPackageOrBundleId": "com.android.settings",

  "startupActivity": "com.android.settings.Settings",

  "capabilities": {
    "language": "en",
    "locale": "US"
  },

  "profiles": [
    {
      "profileName": "Android 12",
      "tapAppIconMethod": "",
      "tapAppIconMacro": "",
      "capabilities": {
        "platformVersion": "12"
      }
    }
  ]

}
```

### testConfig@i.json

This is a sample configuration for iOS apps.

```
{
  "testConfigName": "st@i",

  "appIconName": "You app",

  "appPackageFile": ".app",

  "packageOrBundleId": "com.example.ios.yourapp",

  "#startupPackageOrBundleId": "",

  "#startupActivity": "",

  "capabilities": {
    "useJSONSource": true,
    "showXcodeLog": true,
    "language": "en",
    "locale": "US"
  },

  "profiles": [
    {
      "profileName": "iPhone 13(15.5)",
      "tapAppIconMethod": "",
      "tapAppIconMacro": "",
      "capabilities": {
      }
    }
  ]

}
```

### Link

- [index](../../../index.md)
