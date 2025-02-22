# Selector expression (Vision)

Shirates **Selector expression** is expression to filter visual elements.

**detect** function accepts selector expressions, parses them, retrieves and filters elements, and returns result
element.

In **vision mode**

- You can use information of text.
- You cannot use information of DOM element metadata(id, accessibility, class, etc.).
- Text accuracy is dependent on AI-OCR, so filter application results may differ from expected values.

So features of Selector is restricted in Shirates/Vision.

<br>

### Vision mode

The following selectors are allowed in **Vision mode**.

```kotlin
it.detect("text1")      // equals "text1"

it.detect("*text1*")    // contains "*text1*"

it.detect("text1*")     // starts with "text1"

it.detect("*text2")     // ends with "text2"

it.detect("text1*&&*text2") // starts with "text1" and ends with "text2"

it.detect("text1||text2")   // "text1" or "text2"

it.detect("text1*&&*text2||text3")  // (starts with "text1" and ends with "text2") or equals "text3"
```

### Classic mode

The following selectors are allowed only in **Classic mode**.

```kotlin
it.select("#id1")    // id filter

it.select("@accessibility1") // accessibility filter

it.select(".android.widget.ImageButton")    // class filter

it.select("xpath=//*[@resource-id='android:id/icon']")  // xpath filter

it.select("Hello&&.android.widget.TextView")    // text filter and class filter combined with "&&"(and) operator
```

### Link

- [index](../../../index.md)
