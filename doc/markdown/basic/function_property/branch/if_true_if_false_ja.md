# 分岐関数 (ifTrue, ifFalse)

Kotlinのif-elseステートメントの代わりにこれらの関数を使用することができます。

## 関数

| 関数      | 説明                      |
|:--------|:------------------------|
| ifTrue  | trueの場合にコードブロックが実行されます  |
| ifFalse | falseの場合にコードブロックが実行されます |

### IfTrueIfFalse1.kt

(`kotlin/tutorial/basic/IfTrueIfFalse1.kt`)

```kotlin
@Test
@Order(10)
fun ifTrueIfFalse() {

    scenario {
        case(1) {
            condition {
                it.macro("[Android Settings Top Screen]")
            }.action {
                isEmulator
                    .ifTrue {
                        it.caption("on emulator")
                            .tapWithScrollDown("About emulated device")
                    }
                    .ifElse {
                        it.caption("on real device")
                            .tapWithScrollDown("About phone")
                    }
            }.expectation {
                isEmulator
                    .ifTrue {
                        it.caption("on emulator")
                            .exist("@About emulated device")
                    }
                    .ifElse {
                        it.caption("on real device")
                            .exist("@About phone")
                    }
            }
        }
    }
}
```

## なぜ if-else ステートメントの代わりに分岐関数を使用するのか？

[無負荷実行モード(NLR)](../../report/no_load_run_mode_ja.md)を実現してテスト仕様書のレポートを取得するためです。
無負荷実行モードでは分岐関数はtrueのブロックとfalseのブロックを両方実行し、実際のテストを実行することなく、テスト仕様を説明するためのログを出力します。
if-elseステートメントの代わりに分岐関数を使用してSpec-Reportを取得できるようにしてください。

### Link

- [index](../../../index_ja.md)

