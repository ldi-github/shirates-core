# Using classic APIs in VisionTest (Vision)

You can use classic APIs in VisionTest using `classicScope`.<br>
This is useful when exact text matching required, or accessing attributes of elements (id, accessibility, etc.) are
required.

### Note

Using classic APIs may occur performance problem especially in iOS.

## Sample code

[Getting samples](../../getting_samples.md)

## classicScope

### UsingClassicAPIsInVisionTest1.kt

(`src/test/kotlin/tutorial/inaction/UsingClassicAPIsInVisionTest1.kt`)

```kotlin
    @Test
    fun exactTextMatching() {

        scenario {
            case(1) {
                condition {
                    it.screenIs("[Android Settings Top Screen]")
                }.expectation {
                    classicScope {
                        it.existWithScrollDown("Tips & support")    // exact match
                    }
                }
            }
        }

    }
```

## Nesting scopes

You can use `visionScope` to reverse the scope back to vision scope in classicScope code block.<br>
Nesting is allowed.

### nestingScopes.kt

(`src/test/kotlin/tutorial/inaction/UsingClassicAPIsInVisionTest1.kt`)

```kotlin
    @Test
    fun nestingScopes() {

        scenario {
            case(1) {
                condition {
                    it.screenIs("[Android Settings Top Screen]")
                }.expectation {
                    classicScope {
                        it.existWithScrollDown("Tips & support")    // exact match

                        visionScope {
                            it.exist("Tips & support")  // best effort match

                            classicScope {
                                it.exist("Tips & support")  // exact match
                            }
                        }
                    }
                }
            }
        }

    }
```

### Link

- [index(Vision)](../../../index.md)
- [index(Classic)](../../../classic/index.md)

