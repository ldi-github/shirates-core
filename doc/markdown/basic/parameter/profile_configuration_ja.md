# プロファイル構成

testConfigファイルのプロファイルを構成してテストで使用するデバイスの特定の条件を設定することができます。

## 例

### androidSettingsConfig.json

```
{
  "testConfigName": "androidSettingsConfig",

  "appIconName": "Settings",

  "packageOrBundleId": "com.android.settings",

  "startupPackageOrBundleId": "com.android.settings",
  "startupActivity": "com.android.settings.Settings",

  "settings": {
    "always_finish_activities": "0"
  },

  "dataset": {
    "accounts": "testConfig/android/androidSettings/dataset/accounts.json",
    "apps": "testConfig/android/androidSettings/dataset/apps.json",
    "data": "testConfig/android/androidSettings/dataset/data.json"
  },

  "screens": {
    "import": [
      "testConfig/android/calculator/screens",
      "testConfig/android/misc/screens",
      "testConfig/android/playStore/screen"
    ]
  },

  "capabilities": {
    "language": "en",
    "locale": "US"
  },

  "profiles": [
    {
      "profileName": "Android 14",
      "capabilities": {
        "platformVersion": "14"
      }
    },
    {
      "profileName": "Android 13",
      "capabilities": {
        "platformVersion": "13"
      }
    },
    {
      "profileName": "Android 14 with Tag1",
      "specialTags": "Tag1",
      "capabilities": {
        "platformVersion": "14"
      }
    },
    {
      "profileName": "Android 14 with Tat2 & Tag3",
      "specialTags": "Tag2, Tag3",
      "capabilities": {
        "platformVersion": "14"
      }
    },

    {
      "profileName": "emulator-5556",
      "capabilities": {
        "udid": "emulator-5556"
      }
    }

  ]

}
```

各プロファイルはトップレベルのプロパティを継承します。

* "appIconName"
* "packageOrBundleId"
* "startupPackageOrBundleId"
* "startupActivity"
* "settings"
* "dataset"
* "capabilities"

上記に加え、各プロファイルは固有のプロパティを設定できます。

* **"Android 14"** は `"platformVersion": "14"`
* **"Android 13"** は `"platformVersion": "13"`
* **"Android 14 with Tag1"** は `"specialTags": "Tag1"`
* **"Android 14 with Tag2 & Tag3"** は `"specialTags": "Tag2, Tag3"`
* **"emulator-5556"** は `"udid": "emulator-5556"`

specialTagに関するより詳細な情報はこちら
参照 [スペシャル分岐関数](../../basic/function_property/branch/special_branch_functions_ja.md)

### Link

- [index](../../index_ja.md)
