# Configuring tapAppIcon function

You can tap app icon to launch app with [tapAppIcon](../../basic/function_property/tap_element/tap_app_icon.md).

```kotlin
it.tapAppIcon("Chrome")
```

This function is optimized for Google Pixel by default.

On android devices, hardware manufacturer may install their own graphical user interface(GUI) for home screen and app
launcher. You can specify how to tap app icon in profiles with parameter **tapAppIconMethod** or **tapAppIconMacro**.

See [parameters](../../basic/parameter/parameters.md).

## tapAppIconMethod

| value           | description                                    |
|:----------------|:-----------------------------------------------|
| auto            | Default. Currently same as googlePixel         |
| googlePixel     | Optimized for Google Pixel                     |
| swipeLeftInHome | Find app icon with swiping home screen to left |

## tapAppIconMacro

Specify macro name that you created.

## Example

### profiles

```
  "profiles": [
    {
      "profileName": "Android/googlePixel",
      "tapAppIconMethod": "googlePixel",
      "capabilities": {
      }
    },
    {
      "profileName": "Android/swipeLeftInHome",
      "tapAppIconMethod": "swipeLeftInHome",
      "capabilities": {
      }
    },
    {
      "profileName": "Android/TapAppIconMacro1",
      "tapAppIconMacro": "[TapAppIconMacro1]",
      "capabilities": {
      }
    }
  ]
```

### TapAppIconMacro.kt

```kotlin
@MacroObject
object TapAppIconMacro {

    @Macro("[TapAppIconMacro1]")
    fun tapAppIconMacro1(appIconName: String) {

        it.pressHome()
        it.flickBottomToTop()

        if (it.canSelectWithScrollDown(appIconName)) {
            it.tap()
                .wait()
        }
    }

}
```

### Link

- [index](../../index.md)

