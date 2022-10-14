# Dataset nickname

Dataset nickname is nickname for a set of text data.

## data.json

You can define dataset nicknames in `data.json` and call them by `[nickname].attribute` combination name.

### data.json

```
{
  "[product1]": {
    "product_name": "Super High Tension",
    "product_code": "P001",
    "unit_price": "100"
  },

  "[product2]": {
    "product_name": "Ultra High",
    "product_code": "P002",
    "unit_price": "200"
  }
}
```

**Dataset nickname** is decorated with square brackets.

You can get the value of `product_code` of `[product1]` by calling `data` function.

```kotlin
it.sendKeys(data("[product1].product_code")) // data function returns "P001"
```

<br>

## account.json

account.json is a special version of dataset nickname file for accounts.

### accounts.json

```
{
  "[account1]": {
    "id": "account1@example.com",
    "password": "p@ssword"
  },

  "[account2]": {
    "id": "account2@example.com",
    "password": "p@ssword"
  }
}
```

**Account nickname** is decorated with square brackets.

Attribute **id** and **password** are defined.

You can get the value of `id` of `[account1]` by calling `account` function.

```kotlin
it.sendKeys(account("[account1].id"))   // account function returns "account1@example.com"
```

You can add any attribute in account and get it.

```
{
  "[account1]": {
    "id": "account1@example.com",
    "password": "p@ssword",
    "avatar_name": "Mountain cat"
  },

  "[account2]": {
    "id": "account2@example.com",
    "password": "p@ssword",
    "avatar_name": "Wild tiger"
  }
}
```

```kotlin
it.sendKeys(account("[account1].avatar_name"))  // account function returns "Mountain cat"
```

<br>

## apps.json

apps.json is a special version of dataset nickname file for apps.

### apps.json

```
{
  "[Settings]": {
    "packageOrBundleId": "com.android.settings"
  },
  "[Calculator]": {
    "packageOrBundleId": "com.google.android.calculator"
  },
  "[Chrome]": {
    "packageOrBundleId": "com.android.chrome"
  },
  "[Maps]": {
    "packageOrBundleId": "com.google.android.apps.maps"
  },
  "[Play Store]": {
    "packageOrBundleId": "com.android.vending"
  },
  "[Clock]": {
    "packageOrBundleId": "com.google.android.deskclock"
  },
  "[App1]": {
    "packageOrBundleId": "com.example.app1"
  }
}
```

**App nickname** is decorated with square brackets.

Attribute **packageOrBundleId** is defined.

You can get the value of `packageOrBundleId` of `[App1]` by calling `app` function.

```kotlin
it.sendKeys(app("[App1].packageOrBundleId"))   // app function returns "com.example.app1"
```

<br>

You can know current app is expected app or not by calling `isApp` function.

```kotlin
if (isApp("[Maps]")) {
    // do something
}
```

### Link

- [Selector nickname](selector_nickname.md)

- [Screen nickname](screen_nickname.md)

- [Nickname](nickname.md)
