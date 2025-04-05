# デバイスの言語の切り替え (Vision)

デバイスの言語を切り替えるにはAppiumのcapabilitiesで`language`と`locale`を設定します。<br>
capabilitiesはtestConfig.jsonで設定できます。

## 例

### Android

`testConfig/android/testConfig@a.json`

```json
{
  "testConfigName": "testConfig@a",

  "appIconName": "設定",

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

#### 注意

Androidエミュレーターはしばらく使用するとAppiumから言語設定することができなくなる場合があります。言語設定に失敗する場合は
[Cannot set the device locale to '(locale)'](../../../common/troubleshooting/performance_and_stability_android/cannot_set_the_device_locale_to.md)
を確認してください。

### iOS

`testConfig/ios/testConfig@i.json`

```json
{
  "testConfigName": "testConfig@i",

  "appIconName": "設定",

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

- [index](../../../index_ja.md)
