# Selector expression (Shirates/Vision)

Shirates **Selector expression** is expression to filter visual elements.

**select** function accepts selector expressions, parses them, retrieves and filters elements, and returns result
element.

In **vision mode**

- You can use information of text and image.
- You cannot use information of DOM element metadata(id, accessibility, class, xpath).

So features of Selector is restricted in Shirates/Vision.

<br>

### Vision mode

The following selectors are allowed in Vision mode.

```kotlin
it.detect("text1")

it.detect("*text1*")

it.findImage("[Display Icon]")
```

### Classic mode

The following selectors are allowed in only Classic mode.

```kotlin
it.select("#id1")    // id filter

it.select("@accessibility1") // accessibility filter

it.select(".android.widget.ImageButton")    // class filter

it.select("xpath=//*[@resource-id='android:id/icon']")  // xpath filter

it.select("Hello&&.android.widget.TextView")    // text filter and class filter combined with "&&"(and) operator

it.select("About phone||About emulated device") // text filters combined with "||"(or) operator
```

### Note

Currently, operators `&&` and `||` are not supported. <br>

#### Invalid

```kotlin
it.detect("text1||text2")
```

### Link

- [index](../../../index.md)
