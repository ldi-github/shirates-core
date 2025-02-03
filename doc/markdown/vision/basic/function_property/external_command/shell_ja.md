# Shell (Vision)

これらの関数を使用してシェルコマンドを実行することができます。

## 関数

| 関数         | 説明            |
|:-----------|:--------------|
| shell      | シェルを実行します     |
| shellAsync | シェルを非同期で実行します |

### サンプルコード

[サンプルの入手](../../../getting_samples_ja.md)

### Shell1.kt

(`src/test/kotlin/tutorial/basic/Shell1.kt`)

```kotlin
    @Test
    @Order(10)
    fun shell() {

        scenario {
            case(1) {
                action {
                    if (isRunningOnWindows) {
                        s1 = it.shell("ping", "localhost").resultString
                    } else {
                        s1 = it.shell("ping", "localhost", "-c", "3").resultString
                    }
                }.expectation {
                    s1.thisContains("ping")
                }
            }
        }
    }

    @Test
    @Order(20)
    fun shellAsync() {

        var shellResult: ShellUtility.ShellResult? = null

        scenario {
            case(1) {
                action {
                    if (isRunningOnWindows) {
                        shellResult = it.shellAsync("ping", "localhost")
                    } else {
                        shellResult = it.shellAsync("ping", "localhost", "-c", "3")
                    }
                }.expectation {
                    shellResult!!.hasCompleted.thisIsFalse("hasCompleted=false")
                }
            }
            case(2) {
                expectation {
                    // resultString calls waitFor() in it
                    shellResult!!.resultString.thisStartsWith("PING localhost (127.0.0.1)")
                    shellResult!!.hasCompleted.thisIsTrue("hasCompleted=true")
                }
            }
        }
    }
```

### 注意

シェルの使用はプラットフォーム依存です。[マクロ関数](../../routine_work/macro_ja.md)を使用してプラットフォーム依存の実装を隠蔽することを推奨します。

### Link

- [index](../../../../index_ja.md)
