# Special branch function

## functions

| function   | description                                                             |
|:-----------|:------------------------------------------------------------------------|
| specialTag | This function is executed on any of specialTags in the profile matched. |

## Example

### androidSettingsConfig.json

In profiles section, each profile has specialTags.

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

@Testrun("testConfig/android/androidSettings/testrun.properties", profile = "Android 12")
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
                    }.notMatched {
                        describe("Tag1 not called")
                    }
                    specialTag("Tag2") {
                        OK("specialTag(\"Tag2\") called")
                    }.notMatched {
                        describe("Tag2 not called")
                    }
                }
            }
        }
    }

}
```

### TestLog(simple)

```
168	2022/09/27 19:41:36.840	{specialTag2}	[SCENARIO]	(scenario)	specialTag2()
169	2022/09/27 19:41:36.840	{specialTag2-1}	[CASE]	(case)	(1)
170	2022/09/27 19:41:36.840	{specialTag2-1}	[CONDITION]	(condition)	condition
171	2022/09/27 19:41:36.841	{specialTag2-1}	[describe]	(describe)	testProfile.specialTags=Tag1
172	2022/09/27 19:41:37.101	{specialTag2-1}	[EXPECTATION]	(expectation)	expectation
173	2022/09/27 19:41:37.361	{specialTag2-1}	[branch]	(special)	Tag1 {
174	2022/09/27 19:41:37.361	{specialTag2-1}	[OK]	(ok)	specialTag("Tag1") called
175	2022/09/27 19:41:37.362	{specialTag2-1}	[branch]	(special)	} Tag1
176	2022/09/27 19:41:37.363	{specialTag2-1}	[branch]	(special)	not(Tag2) {
177	2022/09/27 19:41:37.363	{specialTag2-1}	[describe]	(describe)	Tag2 not called
178	2022/09/27 19:41:37.364	{specialTag2-1}	[branch]	(special)	} not(Tag2)
179	2022/09/27 19:41:37.618	{specialTag2-1}	[info]	()	test finished.
```

When initialized with "`Android 12 with Tag1`" profile, the function for "`Tag1`" is called. When initialized
with "`Android 12 with Tag2 & Tag3`", functions for "`Tag2`" is called.

In this way, you can describe profile specific test code using specialTag function.

### Link

- [index](../../../index.md)

