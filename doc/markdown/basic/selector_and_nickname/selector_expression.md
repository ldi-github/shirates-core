# Selector expression

Shirates **Selector expression** is expression to filter screen elements.

**select** function accepts selector expressions, parses them, retrieves and filters elements, and returns result
element.

Selector expression consists of one or more **filter expressions**.

```kotlin
it.select("#id1")    // id filter

it.select("@accessibility1") // accessibility filter

it.select(".android.widget.ImageButton")    // class filter

it.select("text1")  // text filter

it.select("xpath=//*[@resource-id='android:id/icon']")  // xpath filter

it.select("Hello&&.android.widget.TextView")    // text filter and class filter combined with "&&"(and) operator

it.select("About phone||About emulated device") // text filters combined with "||"(or) operator
```

## Filter expression

Shirates **Filter expression** is expression describing conditions to filter screen elements.

| filter           | formal                    | abbreviation | Android attribute | iOS attribute | remarks    |
|:-----------------|:--------------------------|:-------------|:------------------|:--------------|:-----------|
| text             | text=text1                | text1        | text              | label         |            |
| textStartsWith   | textStartsWith=text1      | text1*       | text              | label         |            |
| textContains     | textContains=text1        | \*text1*     | text              | label         |            |
| textEndsWith     | textEndsWith=text1        | *text1       | text              | label         |            |
| textMatches      | textMatches=^text$        | n/a          | text              | label         |            |
| literal          | literal=literal1          | 'literal1'   | text              | label         |            |
| id               | id=id1                    | #id1         | resource-id       | name          |            |
| idStartsWith     | idStartsWith=id1          | #id1*        | resource-id       | name          |            |
| idContains       | idContains=id1            | #\*id1*      | resource-id       | name          |            |
| idEndsWith       | idEndsWith=id1            | #*id1        | resource-id       | name          |            |
| idMatches        | idMatches=^id1$           | n/a          | resource-id       | name          |            |
| access           | access=access1            | @access1     | content-desc      | name          |            |
| accessStartsWith | accessStartsWith=access1  | @access1*    | content-desc      | name          |            |
| accessContains   | accessContains=access1    | @\*access1*  | content-desc      | name          |            |
| accessEndsWith   | accessEndsWith=access1    | @*access1    | content-desc      | name          |            |
| accessMatches    | accessMatches=^access1$   | n/a          | content-desc      | name          |            |
| value            | value=value1              | n/a          | text              | value         |            |
| valueStartsWith  | valueStartsWith=value1    | n/a          | text              | value         |            |
| valueContains    | valueContains=value1      | n/a          | text              | value         |            |
| valueEndsWith    | valueEndsWith=value1      | n/a          | text              | value         |            |
| valueMatches     | valueMatches=^value1$     | n/a          | text              | value         |            |
| class            | class=class1              | .class1      | class             | type          |            |
| focusable        | focusable=true            | n/a          | focusable         | n/a           |            |
| scrollable       | scrollable=true           | n/a          | scrollable        | n/a           |            |
| selected         | selected=true             | n/a          | selected          | n/a           |            |
| visible          | visible=true              | n/a          | n/a               | visible       | deprecated |
| xpath            | xpath=//*[@text='text1']  | n/a          | (arbitrary)       | (arbitrary)   |            |
| pos              | pos=2                     | [2]          | n/a               | n/a           |            |
| ignoreTypes      | ignoreTypes=Class1,Class2 | n/a          | class             | type          |            |
| image            | image=image1.png          | image1.png   | n/a               | n/a           |            |
| capturable       | capturable=??             | ??           | n/a               | n/a           |            |

Note: The visibility attribute in iOS is deprecated because it cannot be used to determine whether an element is visible
on the screen.

## Multiple values in filter

Filter accepts multiple values using parentheses and "|"(or) operator.

```kotlin
it.select("text=(text1|text2)") // formal

it.select("(text1|text2)")  // abbreviation
```

The below selector expression is simple and equivalent to above.

```kotlin
it.select("text1||text2")   // equivalent selector expression
```

In this case, the result is the same whether the or expression by filter or by selector is used.

In more complex situation, "|" may be needed.

```kotlin
it.select("(text1|text2)&&.class1||(@access1|@access2)&&.class2)")
```

## Rules of selector expression

A filter expression can be used alone as a selector expression.

```
text1
```

Filter expression can be combined with "&&"(AND) operator.

```
text1&&.class1&&visible=true
```

Filter expressions can be combined with "||"(OR) operator.

```
text1||text2||@access1
```

"&&" is prior to "||"

```
text1&&.class1||@access1
```

The above means `(text=text1 and class=class1) or access=access1`, but do not specify parenthesis like below (not
supported).

**Bad example**

```
(text1&&.class1)||@access1
```

## Fully qualified id

In android `resource-id` has prefix that corresponds to the app package name. For example, "`com.android.settings`" is
package name of Settings app.

| fully qualified                    | abbreviated |
|:-----------------------------------|:------------|
| com.android.settings:id/search_bar | search_bar  |

In Shirates, abbreviated id is recommended to use because it is converted to fully qualified id automatically, and it
is easy to read. In case of failing, try fully qualified id explicitly.

## Platform annotation for selector expression

You can use **platform annotation**(@a, @i) to write selector expression for Android and for iOS in one line.

```
@a<.android.widget.ImageButton>,@i<.XCUIElementTypeButton>
```

```kotlin
it.select("@a<.android.widget.ImageButton>,@i<.XCUIElementTypeButton>")
```

With this annotation, a [nickname](nickname/nickname.md) can be defined for Android and iOS.

```
"[Button1]": "@a<.android.widget.ImageButton>,@i<.XCUIElementTypeButton>"
```

## Negative filter

In some complex situation you can use negative filter.

```
text=text1      // positive
text!=text1     // negative

text1       // positive
!text1      // negative

accessContains=text1    // positive
accessContains!=text1   // negative

@*text1*    // positive
!@*text1*   // negative
```

## Image filter

You can use image file for image matching(template matching).
See [Image assertion](../function_property/asserting_image/image_assertion.md)

```
image=image1.png    // formal
image1.png          // abbreviation
image1.png?scale=0.5&threshold=20   // with option(formal)
image1.png?s=0.5&t=20   // with option(abbreviation)
```

## Capturable filter (Meta Filter)

Depending on the implementation of the app, it may not be possible to retrieve the corresponding element even
though it is displayed on the screen.
For example, an element corresponding to an image displayed on the screen can be captured on Android, but not on iOS.
In this case, a capturable filter can be used to explicitly state that the element cannot be captured on iOS.

```kotlin
it.existImage("@a<.android.widget.ImageButton>,@i<??>")
```

In this example, on Android, the element is searched with `.android.widget.ImageButton` and matched with an image
template,
On iOS, element search and image matching are not performed, and the result of verification is `COND_AUTO`, which
indicates that manual testing is required.

### Link

- [index](../../index.md)
