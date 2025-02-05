# テストコードの構造 (Vision)

## scenario と case

Shiratesでは JUnit 5のテストメソッドが自動テストのセッションに対応し、その中に1つの scenario と1つ以上の case を含みます。

### サンプルコード

[サンプルの入手](../../getting_samples_ja.md)

### TestScenarioAndTestCase1.kt

(`kotlin/tutorial_vision/basic/TestScenarioAndTestCase1.kt`)

```kotlin
package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.vision.driver.commandextension.screenIs
import shirates.core.vision.driver.commandextension.tap
import shirates.core.vision.testcode.VisionTest

class TestScenarioAndTestCase1 : VisionTest() {

    @Test
    @Order(10)
    fun scenarioAndCase() {

        scenario {
            case(1) {
                // TODO: implement action and expectation
            }

            case(2) {
                // TODO: implement action and expectation
            }
        }
    }

    @Test
    @Order(20)
    fun caePattern() {

        scenario {
            case(1) {
                condition {
                    it.screenIs("[Android設定トップ画面]")
                }.action {
                    it.tap("ネットワークとインターネット")
                }.expectation {
                    it.screenIs("[ネットワークとインターネット画面]")
                }
            }
        }
    }
}
```

### テストを実行する

1. Androidで実行されるように`testrun.global.properties`の`os`を設定します (デフォルトは`android`
   なので単にコメントアウトします)。

```properties
## OS --------------------
#os=ios
```

2. `TestScenarioAndTestCase1`を右クリックして`Debug`を選択します。

## condition-action-expectation (CAE)

case（テストケース）は以下のブロックの組み合わせです。

- `condition`(事前条件)
- `action`（アクション）
- `expectation`（期待結果）

### TestScenarioAndTestCase1.kt

```kotlin
    @Test
    @Order(20)
    fun caePattern() {

        scenario {
            case(1) {
                condition {
                    it.screenIs("[Android設定トップ画面]")
                }.action {
                    it.tap("ネットワークとインターネット")
                }.expectation {
                    it.screenIs("[ネットワークとインターネット画面]")
                }
            }
        }
    }
```

このパターンはShiratesでは "**CAE pattern**" と呼びます。これは有名なUnitテストプラクティスの "AAAパターン (
Arrange-Act-Assert
pattern)" に対応するものです。

### Test Report

![cae1](../../basic/_images/cae1.png)

### Link

- [index](../../../index_ja.md)
