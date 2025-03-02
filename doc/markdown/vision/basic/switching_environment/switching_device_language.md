# Switching device language (Vision)

Set `language` and `locale` of capabilities of Appium. <br>
You can set capabilities in testConfig.json.

## Example

### Android

`testConfig/android/testConfig@a.json`

```json
{
  "testConfigName": "testConfig@a",

  "appIconName": "Settings",

  "packageOrBundleId": "com.android.settings",

  "startupPackageOrBundleId": "com.android.settings",
  "startupActivity": "com.android.settings.Settings",

  "settings": {
    "always_finish_activities": "0"
  },

  "dataset": {
    "accounts": "testConfig/android/dataset/accounts.json",
    "apps": "testConfig/android/dataset/apps.json",
    "data": "testConfig/android/dataset/data.json"
  },

  "capabilities": {
    "language": "ja",
    "locale": "JP"
  }

}
```

#### Note

After using the Android emulator for a while, you may not be able to set the language from Appium. If language setting
fails, please check
[Cannot set the device locale to '(locale)'](../../../common/troubleshooting/performance_and_stability_android/cannot_set_the_device_locale_to.md) .

### iOS

`testConfig/ios/testConfig@i.json`

```json
{
  "testConfigName": "testConfig@i",

  "appIconName": "Settings",

  "packageOrBundleId": "com.apple.Preferences",

  "startupPackageOrBundleId": "com.apple.Preferences",

  "dataset": {
    "apps": "testConfig/ios/dataset/apps.json"
  },

  "capabilities": {
    "language": "ja",
    "locale": "JP"
  }

}
```

### Link

- [index](../../../index.md)
