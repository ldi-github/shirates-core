# デバイスの切り替え (Vision)

## testrun.global.properties / testrun.properties

### Androidエミュレーター

propertiesファイルの中でAVD名を指定します。

```properties
#by AVD Name
android.profile=Pixel 9(Android 15)
```

### Android real device

propertiesファイルの中でudidを指定します。

```properties
# by udid
android.profile=93MAY0CY1M
```

### iOSシミュレーター

propertiesファイルの中でデバイス名を指定します。

```properties
#by model and platformVersion
ios.profile=iPhone 16(iOS 18.2)
```

### iOS実機

propertiesファイルの中でudidを指定します。

```properties
# by udid
ios.profile=360B43B0-CB94-418E-A880-494FEFB3A8D4
```

<br>

参考<br>
[プロファイル命名規約による自動デバイス検出](../../../common/parameter/automatic_device_detection_ja.md)

### Link

- [index](../../../index_ja.md)
