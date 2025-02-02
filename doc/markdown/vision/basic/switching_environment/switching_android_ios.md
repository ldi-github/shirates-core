# Switching Android/iOS (Vision)

There are three methods to configure running device os.

- testrun.global.properties
- Platform annotation(**@android**/**@ios**)
- **@testrun** annotation

## testrun.global.properties

You can switch device os with parameter `os` in properties file.<br>

### Run as Android

Set `os` parameter in the `testrun.global.properties` file.

```properties
## OS --------------------
os=android
```

or

```properties
## OS --------------------
#os=ios
```

Just comment it out. `android` is default.

### Run as iOS

Set `os` parameter in the `testrun.global.properties` file.

```properties
## OS --------------------
os=ios
```

<br>
<hr>

## Platform annotation (@android/@ios)

You can switch device os with platform annotation on test classes.

### Run as Android

```kotlin
@android
class AndroidSettingsVisionDemo : VisionTest() {

}
```

### Run as iOS

```kotlin
@ios
class iOSSettingsVisionDemo : VisionTest() {

}
```

<br>
<hr>

## @testrun annotation

You can specify arbitrary testrun.properties with `@testrun` annotation.

### Run as Android

1. Put `@testrun` annotation on the test class to specify the testrun.properties file.

```kotlin
@Testrun("testConfig/android/androidSettings/testrun.properties")
class AndroidSettingsDemo : UITest() {

}
```

2. Set `os` parameter in the `testrun.properties` file.

```properties
## OS --------------------
os=android
```

or

```properties
## OS --------------------
#os=ios
```

Just comment it out. `android` is default.

### Run as iOS

1. Put `@testrun` annotation on the test class to specify the testrun.properties file.

```kotlin
@Testrun("testConfig/vision/ios/iOSSettings/testrun.properties")
class iOSSettingsVisionDemo : VisionTest() {

}
```

#### testrun.properties

2. Set `os` parameter in the `testrun.properties` file.

```properties
## OS --------------------
os=ios
```

### Link

- [index](../../../index.md)
