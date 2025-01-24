# マクロ (Classic)

テストコードを再利用することはとても重要です。定型処理はマクロとして登録することができます。

## マクロオブジェクト/マクロ関数を作成する

1. `src/test/kotlin`の配下の任意の場所にobjectクラスを作成します。例えば、`macro`ディレクトリを作成し、その下に
   `MacroObject1`を作成します。
   <br>![](_images/creating_macro_object.png)
2. 以下のようにコードを実装します。
    1. **@MacroObject** アノテーションをobjectクラスに付与します。
    2. **TestDrive** インターフェースを継承します。
    3. 関数を作成し、**@Macro** アノテーションを付与します。

```kotlin
package macro

import shirates.core.driver.TestDrive
import shirates.core.driver.commandextension.tap
import shirates.core.macro.Macro
import shirates.core.macro.MacroObject

@MacroObject
object MacroObject1 : TestDrive {

    @Macro("[Network preferences Screen]")
    fun internetScreen() {

        it.tap("Network & internet")
            .tap("Internet")
            .tap("Network preferences")
    }

}
```

4. プロジェクトをビルドします。`out` ディレクトリ内にマクロのクラスファイルが作成されます。
   <br>![](_images/macro_object_class_file.png)

## マクロ関数の使用

1. `kotlin/exercise` の下に`Macro1`クラスを作成します。
2. テストコードを以下のように実装します。

```kotlin
package exercise

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.exist
import shirates.core.driver.commandextension.macro
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class Macro1 : UITest() {

    @Test
    @Order(10)
    fun macro1() {

        scenario {
            case(1) {
                action {
                    it.macro("[Network preferences Screen]")
                }.expectation {
                    it.exist("Install certificates")
                }
            }
        }
    }

}
```

テストコードを実行します。コンソールに以下のようなログが出力されます。

```
[info]	()	Registering macro. (macro.MacroObject1)
```

### Link

- [index](../../index_ja.md)

