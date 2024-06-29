# installApp, removeApp

アプリをパッケージファイルからインストールすることができます。

ダウンロードディレクトリにアプリのパッケージファイル(`.apk` または `.app` または `.ipa`)を配置し
`testConfig.json`で`appPackageFile`にファイル名を設定してください。

## iOSのパッケージファイル (.app または .ipa)

シミュレーターでは`.app`、実機では`.ipa`を使用してください。

## 例

このコードは再インストールの例を示しています。

```kotlin
if (it.isAppInstalled()) {
    it.removeApp()
}
it.installApp()
```

### testConfig@a.json

以下はAndroidアプリ用の簡単な設定例です。

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
      "profileName": "Android 14",
      "tapAppIconMethod": "",
      "tapAppIconMacro": "",
      "capabilities": {
        "platformVersion": "14"
      }
    }
  ]

}
```

### testConfig@i.json

これはiOSアプリ用の簡単な設定例です。

```
{
  "testConfigName": "st@i",

  "appIconName": "You app",

  "appPackageFile": "your_app_package.app",

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

- [index](../../../index_ja.md)
