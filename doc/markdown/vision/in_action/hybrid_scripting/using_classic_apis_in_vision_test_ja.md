# classic APIをVisionTestで使用する (Vision)

`classicScope`を使用すればVisionTest内でclassic APIを使用することができます。<br>
これは厳密なテキストマッチングが必要な場合や要素の属性(id, accessibilityなど)にアクセスする必要がある場合に便利です。

### 注意

classic APIを使用するとiOSでパフォーマンスが低下する場合があります。

## サンプルコード

[サンプルの入手](../../getting_samples_ja.md)

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

- [index(Vision)](../../../index_ja.md)
- [index(Classic)](../../../classic/index_ja.md)

