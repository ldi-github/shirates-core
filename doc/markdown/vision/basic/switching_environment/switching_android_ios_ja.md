# Android/iOSの切り替え (Vision)

実行するデバイスのOSを切り替える方法は3つあります。

- testrun.global.properties
- プラットフォームアノテーション(**@android**/**@ios**)
- **@testrun** アノテーション

## testrun.global.properties

propertiesファイルの`os`パラメーターでデバイスのOSを切り替えることができます。<br>

### Androidで実行する

`testrun.global.properties`ファイルの`os`パラメーターを設定します。

```properties
## OS --------------------
os=android
```

または単にコメントアウトします。

```properties
## OS --------------------
#os=ios
```

`android`がデフォルトです。

### iOSで実行する

`testrun.global.properties`ファイルの`os`パラメーターを設定します。

```properties
## OS --------------------
os=ios
```

<br>
<hr>

## プラットフォームアノテーション (@android/@ios)

テストクラスにプラットフォームアノテーションを付与することでデバイスのOSを切り替えることができます。

### Androidで実行する

```kotlin
@android
class AndroidSettingsVisionDemo : VisionTest() {

}
```

### iOSで実行する

```kotlin
@ios
class iOSSettingsVisionDemo : VisionTest() {

}
```

<br>
<hr>

## @testrun アノテーション

`@testrun`アノテーションを使用して任意のtestrun.propertiesを指定できます。

### Androidで実行する

1. `@testrun`アノテーションをテストクラスに付与してtestrun.propertiesファイルを指定します。

```kotlin
@Testrun("testConfig/android/androidSettings/testrun.properties")
class AndroidSettingsDemo : UITest() {

}
```

2. `testrun.properties`ファイルの`os`パラメーターを設定します。

```properties
## OS --------------------
os=android
```

または単にコメントアウトします。

```properties
## OS --------------------
#os=ios
```

`android`がデフォルトです。

### iOSで実行する

1. `@testrun`アノテーションをテストクラスに付与してtestrun.propertiesファイルを指定します。

```kotlin
@Testrun("testConfig/vision/ios/iOSSettings/testrun.properties")
class iOSSettingsVisionDemo : VisionTest() {

}
```

#### testrun.properties

2. `testrun.properties`ファイルの`os`パラメーターを設定します。

```properties
## OS --------------------
os=ios
```

### Link

- [index](../../../index_ja.md)
