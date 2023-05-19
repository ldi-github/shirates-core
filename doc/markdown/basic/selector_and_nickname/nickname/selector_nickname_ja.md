# セレクターニックネーム

セレクターニックネームはキーと値のペアで定義します。キーはニックネーム、値はセレクター式です。

```
"[selector nickname]": "(selector expression)"
```

## 例1

セレクターニックネームを以下のように定義します。(参照 [画面ニックネーム](screen_nickname_ja.md))

```
"[Search Box]": "@Search"
```

定義したニックネームはテストコードで使用することができます。

```kotlin
it.tap("[Search Box]")
```

上記は以下と同じです。

```kotlin
it.tap("@Search")
```

ニックネームを使用するとテストコードが読みやすく意味がわかりやすくなります。

## 例2

セレクターニックネームを以下のように定義します。(参照 [画面ニックネーム](screen_nickname_ja.md))

```
"[Network & internet]": "Network & internet"
```

セレクター式がテキストフィルターであり、ニックネームから括弧を除いたラベル部分と一致する場合は、値の設定は省略できます。

```
"[Network & internet]": ""
```

定義したニックネームはテストコードで使用することができます。

```kotlin
it.tap("[Network & internet]")
```

### Link

- [画面ニックネーム](screen_nickname_ja.md)
- [データセットニックネーム](dataset_nickname_ja.md)
- [ニックネーム](nickname_ja.md)
