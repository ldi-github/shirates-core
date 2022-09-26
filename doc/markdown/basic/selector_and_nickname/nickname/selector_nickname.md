# Selector nickname

Selector nickname is defined of key and value pair. The key is nickname, and the value is selector expression.

```
"[selector nickname]": "(selector expression)"
```

## Example 1

Selector nickname is defined like this. (see also [Screen nickname](screen_nickname.md))

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

Selector nickname is defined like this. (see also [Screen nickname](screen_nickname.md))

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

- [Screen nickname](screen_nickname.md)

- [Dataset nickname](dataset_nickname.md)

- [Nickname](nickname.md)
