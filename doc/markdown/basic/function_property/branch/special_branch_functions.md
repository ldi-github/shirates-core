# Special branch function

## functions

| function   | description                                                             |
|:-----------|:------------------------------------------------------------------------|
| specialTag | This function is executed on any of specialTags in the profile matched. |

## Example

### androidSettingsConfig.json

In profiles section, each profile has specialTags. "Android 12" has "Tag1". "Pixel 3a API 31(Android 12)" has "Tag2"
and "Tag3".

```
  "profiles": [
    {
      "profileName": "Android 12",
      "specialTags": "Tag1",
      "capabilities": {
      }
    },
    {
      "profileName": "Pixel 3a API 31(Android 12)",
      "specialTags": "Tag2, Tag3",
      "capabilities": {
        "avd": "Pixel_3a_API_31_Android_12_"
      }
    }
  ]
```

### SpecialTag1.kt

```kotlin
@Test
@Order(10)
fun specialTag1() {

    scenario {
        case(1) {
            expectation {
                output("testProfile.specialTags=${testProfile.specialTags}")

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
```

### TestLog(simple)

```
117	2022/06/06 23:02:24.407	{specialTag1}	[SCENARIO]	(scenario)	specialTag1()
118	2022/06/06 23:02:24.407	{specialTag1-1}	[CASE]	(case)	(1)
119	2022/06/06 23:02:24.408	{specialTag1-1}	[EXPECTATION]	(expectation)	expectation
120	2022/06/06 23:02:24.906	{specialTag1-1}	[output]	(output)	testProfile.specialTags=Tag1
121	2022/06/06 23:02:24.908	{specialTag1-1}	[branch]	(special)	Tag1 {
122	2022/06/06 23:02:24.909	{specialTag1-1}	[OK]	(ok)	specialTag("Tag1") called
123	2022/06/06 23:02:24.909	{specialTag1-1}	[branch]	(special)	} Tag1
124	2022/06/06 23:02:24.909	{specialTag1-1}	[info]	()	test finished.
```

When initialized with "Android 12" profile, the function for "Tag1" is called. When initialized with "Pixel 3a API 31(
Android 12)", functions for "Tag2" and "Tag3" are called.

In this way, you can describe profile specific test code using specialTag function.

### Link

- [index](../../../index.md)

