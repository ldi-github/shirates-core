# Special branch function (Shirates/Vision)

## functions

| function   | description                                                              |
|:-----------|:-------------------------------------------------------------------------|
| specialTag | The code block is executed on any of specialTags in the profile matched. |

## Example

### androidSettingsConfig.json

In profiles section, each profile has specialTags.

```
  "profiles": [

  ...

    {
      "profileName": "Android 14 with Tag1",
      "specialTags": "Tag1",
      "capabilities": {
        "platformVersion": "14"
      }
    },
    {
      "profileName": "Android 14 with Tat2 & Tag3",
      "specialTags": "Tag2, Tag3",
      "capabilities": {
        "platformVersion": "14"
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

@Testrun("testConfig/android/androidSettings/testrun.properties", profile = "Android 14 with Tag1")
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

When initialized with "`Android 14 with Tag1`" profile, the function for "`Tag1`" is called. When initialized
with "`Android 14 with Tag2 & Tag3`", functions for "`Tag2`" is called.

In this way, you can describe profile specific test code using specialTag function.

### Link

- [index](../../../../index.md)

