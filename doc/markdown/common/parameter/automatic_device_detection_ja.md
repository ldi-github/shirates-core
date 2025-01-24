# プロファイル命名規約による自動デバイス検出

テストで使用する仮想デバイス/実デバイスを **プロファイル命名規約** によって決定することができます。

### 注意

この機能はローカルマシンで利用できます。リモートサーバーでは利用できません。

**プロファイル命名規約** を使用すれば
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
android.profile=Pixel 8(Android 14)
```

テストを実行するとAVD名が`Pixel 8(Android 14)`である仮想デバイスが検索されて起動します。

#### Console

```
78	[00:00:13]	2024/04/14 22:04:36.055	{}	0	-	[info]	+10175	C	()	Connected device found. (Pixel_8_Android_14_:5554, Android 14, emulator-5554)
```

### 例 (iOS)

Simulatorのデバイス名をプロファイル名としてtestrunファイルで指定することができます。

#### testrun.properties

```
os=ios
ios.profile=iPhone 15(iOS 18.2)
```

テストを実行するとSimulatorデバイス`iPhone 16(iOS 18.2)`が検索されて起動します。

#### Console

```
125	[00:00:24]	2025/01/24 22:01:47.766	{s10}	0	-	[info]	+2268	C	(launchApp)	Device found. (iPhone 16(iOS 18.2), iOS 18.2, 4F87A640-BC60-46F1-8124-BD01B3862DD1)
```

## プロファイル命名規約

### Android

| パターン                  | プロファイル名             | 説明                                  |
|:----------------------|:--------------------|:------------------------------------|
| _AVD name_            | Pixel 8 API 34      | Android仮想デバイス `Pixel 8 API 34`      |
| _model_(_os_version_) | Pixel 8(Android 14) | 接続されているデバイス `Pixel 8(Android 14)`   |
| _model_(_os_version_) | Pixel 8(14)         | 接続されているデバイス `Pixel 8(Android 14)`   |
| _os_version_          | Android *           | 接続されているデバイス                         |
| _os_version_          | Android 14          | 接続されているデバイス `Android 14`            |
| _os_version_          | 14                  | 接続されているデバイス `Android 14`            |
| _udid_                | emulator-5554       | 接続されているエミュレーター `udid:emulator-5554` |
| _udid_                | 14141JEC20492X      | 接続されている実デバイス `udid:14141JEC20492X`  |

### iOS

| パターン                  | プロファイル名                              | 説明                                                  |
|:----------------------|:-------------------------------------|:----------------------------------------------------|
| _deviceName_          | Device 1                             | iOSデバイス `Device 1`                                  |
| _os_version_          | 18.2                                 | iOSデバイス (iOS 18.2)                                  |
| _model_(_os_version_) | iPhone *                             | iOSデバイス                                             |
| _model_               | iPhone 16                            | iOSデバイス (iPhone 16)                                 |
| _model_(_os_version_) | iPhone 16(18.2)                      | iOSデバイス (iPhone 16, iOS 18.2)                       |
| _udid_                | D0A63437-19F6-4756-8F27-8B1AA9EC22A4 | iOSデバイス (udid:D0A63437-19F6-4756-8F27-8B1AA9EC22A4) |

### Androidエミュレーター

**Android Studio**を開いて**Device Manager**で **virtual** タブを確認します。

![Device Manager](../_images/device_manager.png)

プロファイル名としてAVD名を使用できます。

- `Pixel 8(Android 14)`
- `Pixel 8(Android 14)-01`
- `Pixel 8(Android 14)-02`

テスト開始時に指定されたAVDが起動していない場合は自動で起動します。

### iOSシミュレーター

**Xcode** を起動して **Simulators** タブを確認します。

![Simulators](../_images/simulators.png)

プロファイル名としてシミュレーターのデバイス名を使用できます。

- `iPhone 16(18.2)`
- `iPhone 16(18.2)-01`
- `iPhone 16(18.2)-02`
- `iPhone 16 Pro(18.2)`
- `iPhone 16 Pro Max(18.2)`

テスト開始時に指定されたシミュレーターデバイスが起動していない場合は自動で起動します。

<br>

### Link

- [index(vision)](../../index.md)
- [index(classic)](../../classic/index.md)
