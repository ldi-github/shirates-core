# Performance problem of getPageSource in iOS

Before selecting elements, **shirates-core** gets source xml with `getPageSource()` into **TestElementCache**.
You can query elements in the TestElementCache flexibly (e.g.relative selector).

Unfortunately, WebDriverAgent for iOS has a performance problem in getPageSource(). When there are many elements on the
screen(or out of the screen), it may take minutes to get source xml with getPageSource(). It is said that this
problem is of lack of API for retrieve whole element tree. To solve this problem, Apple must provide such API.

To avoid serious performance problem in iOS, you can tune your test code with **direct access mode**. This mode is
restricted with function but getting page source for cache is not required.

### Link

- [Direct access mode](direct_access_mode.md)
- [index](../../index.md)
