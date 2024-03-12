# リモートのAppium Serverの構成

リモートのAppium Serverを利用することができます。ただし、いくつかの制限があります。

### リモートのAppium Serverにおける制限事項

1. 自動デバイス検出は利用できません。プロファイルは明示的に構成する必要があります。
2. テストシナリオのリラン実行時においてAppium Serverプロセスの自動再起動は利用できません。
3. テストシナリオのリラン実行時において仮想マシンの自動再起動は利用できません。

## 例

1. `appiumServerUrl`にリモートのAppium ServerのURLを設定します。
2. `useRemoteServer`に true を設定します。

```properties
appiumServerUrl=http://10.0.0.104:4723/
useRemoteServer=true
```

3. プロファイルを明示的に構成します。 参照 [プロファイル構成](../../basic/parameter/profile_configuration_ja.md)

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

4. リモートマシン上でAppium Serverを起動します。

```
appium --session-override --relaxed-security
```

5. Shiratesで記述したテストを実行します。

### Link

- [index](../../index_ja.md)
