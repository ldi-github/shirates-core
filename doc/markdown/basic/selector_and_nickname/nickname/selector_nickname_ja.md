# セレクターニックネーム

セレクターニックネームはキーと値のペアで定義します。キーはニックネーム、値はセレクター式です。

```
"[selector nickname]": "(selector expression)"
```

## Example 1

Selector nickname is defined like this. (see also [Screen nickname](screen_nickname_ja.md))

```
"[Search Box]": "@Search"
```

You can use the nickname in test code.

```kotlin
it.tap("[Search Box]")
```

Above is equivalent to below.

```kotlin
it.tap("@Search")
```

Using nickname makes your test code more readable and semantic.

## Example 2

Selector nickname is defined like this. (see also [Screen nickname](screen_nickname_ja.md))

```
"[Network & internet]": "Network & internet"
```

When the selector expression is text filter and is equal to the nickname label(without brackets), the value can be
omitted.

```
"[Network & internet]": ""
```

You can use the nickname in test code.

```kotlin
it.tap("[Network & internet]")
```

### Link

- [画面ニックネーム](screen_nickname_ja.md)

- [データセットニックネーム](dataset_nickname_ja.md)

- [ニックネーム](nickname_ja.md)
