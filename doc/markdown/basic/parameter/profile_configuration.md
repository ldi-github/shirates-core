# Profile configuration

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
    "data": "testConfig/android/androidSettings/dataset/data.json"
  },

  "capabilities": {
    "automationName": "UiAutomator2",
    "platformName": "Android",
    "platformVersion": "12",
    "language": "en",
    "locale": "US"
  },

  "profiles": [
    {
      "profileName": "Android 12",
      "specialTags": "Tag1",
      "capabilities": {
      }
    },
    {
      "profileName": "Pixel 3a API 31(Android 12)",
      "specialTags": "Tag2, Tag3",
      "capabilities": {
        "avd": "Pixel_3a_API_31_Android_12_"
      }
    }
  ]

}
```

There are 2 profiles in the testConfig file.

* "Android 12"
* "Pixel 3a API 31(Android 12)"

Each profile inherits from **common properties**

* "appIconName"
* "packageOrBundleId"
* "startupPackageOrBundleId"
* "startupActivity"
* "settings"
* "dataset"
* "capabilities"

In addition to the above, each profile has specific properties.

* **"Android 12"** specifies
    * `"specialTags": "Tag1"`


* **"Pixel 3a API 31(Android 12)"** specifies
    * `"specialTags": "Tag2, Tag3"`
    * `"capabilities/avd": "Pixel_3a_API_31_Android_12_"`

For more information about specialTag,
see [Special branch function](../../basic/function_property/branch/special_branch_functions.md)

### Link

- [index](../../index.md)
