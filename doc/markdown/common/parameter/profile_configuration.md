# Profile configuration (Vision/Classic)

You can configure profiles in the testConfig file for the specific condition of the device used for testing.

## Example

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

Each profile inherits from top level properties.

* "appIconName"
* "packageOrBundleId"
* "startupPackageOrBundleId"
* "startupActivity"
* "settings"
* "dataset"
* "capabilities"

In addition to the above, each profile has specific properties.

* **"Android 14"** specifies `"platformVersion": "14"`
* **"Android 13"** specifies `"platformVersion": "13"`
* **"Android 14 with Tag1"** specifies `"specialTags": "Tag1"`
* **"Android 14 with Tag2 & Tag3"** specifies `"specialTags": "Tag2, Tag3"`
* **"emulator-5556"** specifies `"udid": "emulator-5556"`

For more information about specialTag,
see [Special branch function](../../classic/basic/function_property/branch/special_branch_functions.md)

### Link

- [index(Vision)](../../index.md)
- [index(Classic)](../../classic/index.md)

