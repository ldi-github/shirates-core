# installApp, removeApp (Vision)

You can install app by app package file.

Put your app's package file (`.apk` or `.app` or `.apks` or `.ipa`) in Download directory, set app file name to
`appPackageFile` in
`testConfig.json`.

## iOS package file (.app or .ipa)

Use `.app` on simulator. <br>
Use `.ipa` on real device.

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
  }

}
```

### testConfig@i.json

This is a sample configuration for iOS apps.

```
{
  "testConfigName": "st@i",

  "appIconName": "You app",

  "appPackageFile": "your_app_package.app",

  "packageOrBundleId": "com.example.ios.yourapp",

  "capabilities": {
    "language": "en",
    "locale": "US"
  }

}
```

### Link

- [index](../../../../index.md)
