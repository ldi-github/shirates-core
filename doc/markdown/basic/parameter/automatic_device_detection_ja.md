# プロファイル命名規約による自動デバイス検出

テストで使用する仮想デバイス/実デバイスをプロファイル命名規約によって決定することができます。

## shirates-core 1.x

shirates-core 1.xの時点ではデバイスが必要とするcapabilitiesを明示的にプロファイルで構成する必要がありました。

#### testConfig.json

```
...
  "profiles": [
    {
      "profileName": "Android 12",
      "capabilities": {
        "automationName": "UiAutomator2",
        "platformName": "Android",
        "platformVersion": "12"
      }
    }
  ]
...  
```

## shirates-core 2.x以降

shirates-core 2.x以降では **プロファイル命名規約**によってデバイスを指定できるようになりました。
testConfig.jsonにプロファイルを定義してcapabilitiesを構成する必要はありません。
プロファイル名を指定するだけです。

#### testConfig.json

```
...
  "profiles": [
  ]
...  
```

### 例 (Android)

AVD名をプロファイル名としてtestrunファイルで指定することができます。

#### testrun.properties

```
android.profile=Pixel 3a API 31
```

テストを実行するとAVD名が`Pixel 3a API 31`である仮想デバイスが検索されて起動します。

#### Console

```
63	2022/12/19 01:09:50.808	{}	[info]	()	Searching device for the profile. (profileName=Pixel 3a API 31)
64	2022/12/19 01:09:54.224	{}	[info]	()	emulator @Pixel_3a_API_31 -no-boot-anim -no-snapshot
65	2022/12/19 01:10:00.423	{}	[info]	()	Connected device found. (Pixel_3a_API_31:5554, Android 12, emulator-5554)
```

### 例 (iOS)

Simulatorのデバイス名をプロファイル名としてtestrunファイルで指定することができます。

#### testrun.properties

```
os=ios
ios.profile=iPhone 14(iOS 16.1)
```

テストを実行するとSimulatorデバイス`iPhone 14(iOS 16.1)`が検索されて起動します。

#### Console

```
61	2022/12/19 01:20:34.730	{}	[info]	()	Searching device for the profile. (profileName=iPhone 14(iOS 16.0)-01)
62	2022/12/19 01:20:38.265	{}	[info]	()	Device found. (iPhone 14(iOS 16.0)-01, iOS 16.0, 4801481D-60AA-4A1A-BFC5-4C636ADF4B3A)
```

## プロファイル命名規約

### Android

| パターン                  | プロファイル名              | 説明                                  |
|:----------------------|:---------------------|:------------------------------------|
| _AVD name_            | Pixel 3a API 31      | Android仮想デバイス `Pixel 3a API 31`     |
| _model_(_os_version_) | Pixel 3a(Android 12) | 接続されているデバイス `Pixel 3a(Android 12)`  |
| _model_(_os_version_) | Pixel 3a(12)         | 接続されているデバイス `Pixel 3a(Android 12)`  |
| _os_version_          | Android *            | 接続されているデバイス                         |
| _os_version_          | Android 12           | 接続されているデバイス `Android 12`            |
| _os_version_          | 12                   | 接続されているデバイス `Android 12`            |
| _udid_                | emulator-5554        | 接続されているエミュレーター `udid:emulator-5554` |
| _udid_                | 14141JEC20492X       | 接続されている実デバイス `udid:14141JEC20492X`  |

### iOS

| パターン                  | プロファイル名                              | 説明                                                  |
|:----------------------|:-------------------------------------|:----------------------------------------------------|
| _deviceName_          | Device 1                             | iOSデバイス `Device 1`                                  |
| _os_version_          | 16.2                                 | iOSデバイス (iOS 16.2)                                  |
| _model_(_os_version_) | iPhone *                             | iOSデバイス                                             |
| _model_               | iPhone 14                            | iOSデバイス (iPhone 14)                                 |
| _model_(_os_version_) | iPhone 14(16.2)                      | iOSデバイス (iPhone 14, iOS 16.2)                       |
| _udid_                | D0A63437-19F6-4756-8F27-8B1AA9EC22A4 | iOSデバイス (udid:D0A63437-19F6-4756-8F27-8B1AA9EC22A4) |

### Androidエミュレーター

**Android Studio**を開いて**Device Manager**で **virtual** タブを確認します。

![Device Manager](../_images/device-manager-virtual.png)

プロファイル名としてAVD名を使用できます。

- `Pixel 3a(Android 12)`
- `Pixel 3a(Android 12)-01`
- `Pixel 3a(Android 12)-02`
- `Pixel 3a(Android 12)-03`

テスト開始時に指定されたAVDが起動していない場合は自動で起動します。

### iOSシミュレーター

**Xcode** を起動して **Simulators** タブを確認します。

![Device Manager](../_images/devices-and-simulators.png)

プロファイル名としてシミュレーターのデバイス名を使用できます。

- `iPhone 14(16.0)`
- `iPhone 14(16.1)`
- `iPhone 14 Plus(16.0)`
- `iPhone 14 Plus(16.1)`
- `iPhone 14 Pro(16.0)`
- `iPhone 14 Pro(16.1)`
- `iPhone 14 Pro Max(16.0)`
- `iPhone 14 Pro Max(16.1)`

テスト開始時に指定されたシミュレーターデバイスが起動していない場合は自動で起動します。

<br>

### Link

- [index](../../index_ja.md)
