# Test code structure (Vision)

## scenario and case

In Shirates, a JUnit 5 test method is an autotest session that includes a scenario and one or more test cases.

## Sample code

[Getting samples](../../getting_samples.md)

### TestScenarioAndTestCase1.kt

(`kotlin/tutorial_vision/basic/TestScenarioAndTestCase1.kt`)

```kotlin
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
                    it.screenIs("[Android Settings Top Screen]")
                }.action {
                    it.tap("Network & internet")
                }.expectation {
                    it.screenIs("[Network & internet Screen]")
                }
            }
        }
    }
```

### Running test

1. Set `os` in `testrun.global.properties` to run as android (default is android).

```properties
## OS --------------------
#os=ios
```

2. Right-click on the `TestScenarioAndTestCase1` and select `Debug`.

## condition-action-expectation (CAE)

A test case is a combination of these blocks.

- `condition`
- `action`
- `expectation`

### TestScenarioAndTestCase1.kt

```kotlin
    @Test
    @Order(20)
    fun caePattern() {

        scenario {
            case(1) {
                condition {
                    it.screenIs("[Android Settings Top Screen]")
                }.action {
                    it.tap("Network & internet")
                }.expectation {
                    it.screenIs("[Network & internet Screen]")
                }
            }
        }
    }
```

This pattern is Shirates's "**CAE pattern**", like well known unit testing practice "AAA pattern (Arrange-Act-Assert
pattern)".

### Test Report

![cae1](../../basic/_images/cae1.png)

### Link

- [index](../../../index.md)
