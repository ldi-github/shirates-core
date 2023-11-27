# スペシャル分岐関数

## 関数

| 関数         | 説明                                                 |
|:-----------|:---------------------------------------------------|
| specialTag | プロファイル内の`specialTags`の任意のタグにマッチした場合にコードブロックが実行されます |

## 例

### androidSettingsConfig.json

`profiles`セクションにおいて各プロファイルに`specialTags`が設定されています。

```
  "profiles": [

  ...

    {
      "profileName": "Android 12 with Tag1",
      "specialTags": "Tag1",
      "capabilities": {
        "platformVersion": "12"
      }
    },
    {
      "profileName": "Android 12 with Tat2 & Tag3",
      "specialTags": "Tag2, Tag3",
      "capabilities": {
        "platformVersion": "12"
      }
    },

...

  ]
```

### SpecialTag1.kt

(`kotlin/tutorial/basic/SpecialTag1.kt`)

```kotlin
package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.branchextension.specialTag
import shirates.core.driver.commandextension.describe
import shirates.core.driver.testProfile
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties", profile = "Android 12 with Tag1")
class SpecialTag1 : UITest() {

    @Test
    @Order(10)
    fun specialTag1() {

        scenario {
            case(1) {
                condition {
                    describe("testProfile.specialTags=${testProfile.specialTags}")
                }.expectation {
                    specialTag("Tag1") {
                        OK("specialTag(\"Tag1\") called")
                    }
                    specialTag("Tag2") {
                        OK("specialTag(\"Tag2\") called")
                    }
                    specialTag("Tag3") {
                        OK("specialTag(\"Tag3\") called")
                    }
                }
            }
        }
    }

    @Test
    @Order(20)
    fun specialTag2() {

        scenario {
            case(1) {
                condition {
                    describe("testProfile.specialTags=${testProfile.specialTags}")
                }.expectation {
                    specialTag("Tag1") {
                        OK("specialTag(\"Tag1\") called")
                    }.ifElse {
                        describe("Tag1 not called")
                    }
                    specialTag("Tag2") {
                        OK("specialTag(\"Tag2\") called")
                    }.ifElse {
                        describe("Tag2 not called")
                    }
                }
            }
        }
    }

}
```

"`Android 12 with Tag1`"のプロファイルを使用して初期化した場合は"`Tag1`"に対応するspecialTag関数のコードブロックが実行されます。
"`Android 12 with Tag2 & Tag3`"を使用して初期化した場合は"`Tag2`"に対応するspecialTag関数のコードブロックが実行されます。

このように、specialTag関数を使用するとプロファイルに固有のテストコードを記述することができます。

### Link

- [index](../../../index_ja.md)

