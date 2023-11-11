# tapAppIcon関数を構成する

## tapAppIcon 関数

[tapAppIcon](../../basic/function_property/tap_element/tap_app_icon_ja.md)関数を使用するとアプリアイコンをタップして起動することができます。

```kotlin
it.tapAppIcon("Chrome")
```

この関数はデフォルトでは`Google Pixel` と `iOS`向けに最適化されています。

## パラメーターの構成

Androidデバイスではハードウェアの製造者がホーム画面やアプリのランチャーに独自のグラフィカルユーザーインターフェース(GUI)
をインストールすることがあります。
**tapAppIconMethod** や **tapAppIconMacro** をプロファイルのパラメーターに設定することでアプリのアイコンをタップする際の動作を指定することができます。

参照 [パラメーター](../../basic/parameter/parameters_ja.md).

### tapAppIconMethod

| 設定値             | 説明                               |
|:----------------|:---------------------------------|
| auto            | デフォルト。 Androidでは`googlePixel`と同じ |
| googlePixel     | Google Pixelに最適化                 |
| swipeLeftInHome | ホーム画面を左にスワイプしながらアプリアイコンを探す       |

### tapAppIconMacro

自身で作成したマクロを指定することができます。

## 例

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

(`kotlin/macro/common/TapAppIconMacro.kt`)

```kotlin
package macro.common

import shirates.core.driver.TestDrive
import shirates.core.driver.branchextension.android
import shirates.core.driver.branchextension.ios
import shirates.core.driver.commandextension.*
import shirates.core.driver.wait
import shirates.core.macro.Macro
import shirates.core.macro.MacroObject

@MacroObject
object TapAppIconMacro : TestDrive {

    @Macro("[TapAppIconMacro1]")
    fun tapAppIconMacro1(appIconName: String) {

        android {
            it.pressHome()
            it.flickBottomToTop()

            if (it.canSelectWithScrollDown(appIconName)) {
                it.tap()
                    .wait()
            }
        }
        ios {
            it.tapAppIcon(appIconName)
        }
    }
}
```

`[TapAppIconMacro1]`マクロをテストコードで使用することができます。

```kotlin
it.macro("[TapAppIconMacro1]", "Calculator")
```

## tapAppIcon関数をカスタマイズする

**CustomObject** を使用して`tapAppIcon`の動作をカスタマイズすることができます。この方法はより実用的です。

1. `src/test/kotlin`の配下の任意の場所に`custom`という名前のディレクトリを作成します。
   <br> ![](../_images/tap_app_icon_custom_object.png)

2. `TapAppIconCustomObject`という名前のクラスを作成します。
3. `tapAppIcon` 関数を実装します。

### カスタム関数の例

```kotlin
package custom

import shirates.core.customobject.CustomFunction
import shirates.core.customobject.CustomObject
import shirates.core.driver.TestDrive
import shirates.core.driver.branchextension.android
import shirates.core.driver.branchextension.ios
import shirates.core.driver.commandextension.*

@CustomObject
object TapAppIconCustomObject : TestDrive {

    @CustomFunction
    fun tapAppIcon(appIconName: String) {

        android {
            it.pressHome()
            it.flickBottomToTop()

            if (it.canSelectWithScrollDown(appIconName)) {
                it.tap()
                    .wait()
            }
        }
        ios {
            it.tapAppIcon(appIconName)
        }
    }
}
```

4. プロジェクトをビルドします。
5. tapAppIcon関数をテストコードで使用するとカスタマイズされた挙動となります。

```kotlin
it.tapAppIcon("Calculator")
```

### Link

- [index](../../index_ja.md)

