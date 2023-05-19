# OSのメッセージの変更に対応する

OSを新しいバージョンにアップグレードすると、以前は常に成功していたテストコードが失敗するようになるがあります。

OSのメッセージの変更がこの原因である可能性があります。

## 例

以下はAndroid 10における**位置情報アクセス許可**ダイアログです。

![](../_images/location_permissions_android_10.png)

`tap`関数を使用して **"Allow only while using the app"**をタップすることができます。

```kotlin
@Test
@Order(10)
fun original() {

    scenario {
        case(1) {
            action {
                it.tap("Allow only while using the app")
            }
        }
    }
}
```

Android 10 から 11へアップグレードした場合は **"Allow only while using the app"** は **"While using the app"** に変更されます。

![](../_images/location_permissions_comparison.png)

テストコードは失敗します。

この場合、テストコードを修正して`if-else`で分岐処理する必要があります。

### 条件分岐の使用

```kotlin
if (platformVersion.toInt() < 11) {
    it.tap("Allow only while using the app")
} else {
    it.tap("While using the app")
}
```

これは動作しますが良い方法ではありません。

条件分岐を使用する代わりにセレクター内で `||`(or)を使用することができます。

### セレクター内で `||` を使用する

```kotlin
it.tap("Allow only while using the app||While using the app")
```

これはベターな方法ですが、テストコード内に複数のメッセージを記述するのはメンテナンス性がよくありません。

### ニックネームを使用する

ニックネームを使用するとメンテナンス性の問題が改善します。

```kotlin
it.tap("[While using the app]")
```

ニックネームは画面ニックネームファイル内に定義します。

```
"[While using the app]": "Allow only while using the app||While using the app"
```

### Link

- [index](../../index_ja.md)

