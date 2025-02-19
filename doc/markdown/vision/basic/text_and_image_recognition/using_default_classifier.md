# Using DefaultClassifier (Vision)

Shirates/Vision classifies the image using DefaultClassifier.

## Sample code

[Getting samples](../../getting_samples.md)

### Classify1.kt

(`src/test/kotlin/tutorial/basic/Classify1.kt`)

```kotlin
    @Test
    @Order(10)
    fun classify() {

        scenario {
            case(1) {
                condition {
                    it.screenIs("[Android Settings Top Screen]")
                }.action {
                    v1 = it.detect("Network & internet")
                        .leftItem()
                    s1 = v1.classify()
                }.expectation {
                    s1.thisIs("[Network & internet Icon]", message = "label is $s1")
                }
            }
            case(2) {
                action {
                    s2 = v1.classifyFull()
                }.expectation {
                    s2.thisIs(
                        "@a_Android Settings_Android Settings Top Screen_[Network & internet Icon]",
                        message = "fullLabel is $s2"
                    )
                }
            }
        }
    }
```

Right-click on `classify()` and select `debug` to run test.

### Console output

```
140	[00:00:19]	2025/02/19 23:20:48.261	{classify-1}	0	-	[info]	+246	!	()	139_[138.png]_recognized_text_rectangles.png
141	[00:00:19]	2025/02/19 23:20:48.444	{classify-1}	0	-	[info]	+183	!	()	[detect] in 1.874 sec
2025-02-19 23:20:48.464 java[31276:424563] +[IMKClient subclass]: chose IMKClient_Modern
2025-02-19 23:20:48.464 java[31276:424563] +[IMKInputSession subclass]: chose IMKInputSession_Modern
142	[00:00:19]	2025/02/19 23:20:48.490	{classify-1}	0	-	[info]	+46	!	()	[SegmentContainer] in 0.043 sec
143	[00:00:19]	2025/02/19 23:20:48.502	{classify-1}	0	-	[info]	+12	!	()	[split screenshot into segments] in 0.056 sec
144	[00:00:20]	2025/02/19 23:20:49.012	{classify-1}	0	-	[info]	+510	!	()	[SegmentContainer] in 0.507 sec
145	[00:00:20]	2025/02/19 23:20:49.088	{classify-1}	0	-	[info]	+76	!	()	split screenshot into segments. visionElements:1
146	[00:00:20]	2025/02/19 23:20:49.091	{classify-1}	0	-	[info]	+3	!	()	[rightLeftCore] in 0.645 sec
147	[00:00:20]	2025/02/19 23:20:49.093	{classify-1}	0	-	[info]	+2	!	()	<Network & internet>_leftItem.png
148	[00:00:20]	2025/02/19 23:20:49.168	{classify-1}	0	-	[info]	+75	!	()	[ImageClassifier/classifyImage] in 0.072 sec
149	[00:00:20]	2025/02/19 23:20:49.170	{classify-1}	0	-	[EXPECTATION]	+2	!	()	expectation
150	[00:00:20]	2025/02/19 23:20:49.172	{classify-1}	0	-	[OK]	+2	!	(thisIs)	label is [Network & internet Icon]
151	[00:00:20]	2025/02/19 23:20:49.173	{classify-2}	0	-	[CASE]	+1	!	()	(2)
152	[00:00:20]	2025/02/19 23:20:49.187	{classify-2}	0	-	[ACTION]	+14	!	()	action
153	[00:00:20]	2025/02/19 23:20:49.252	{classify-2}	0	-	[info]	+65	!	()	[ImageClassifier/classifyImage] in 0.063 sec
154	[00:00:20]	2025/02/19 23:20:49.253	{classify-2}	0	-	[EXPECTATION]	+1	!	()	expectation
155	[00:00:20]	2025/02/19 23:20:49.255	{classify-2}	0	-	[OK]	+2	!	(thisIs)	fullLabel is @a_Android Settings_Android Settings Top Screen_[Network & internet Icon]
```

### TestResults

You got test results files in TestResults directory(`~/Downloads/TestResults` is default).

### fullLabel and label

Open the directory `build/vision/classifiers/DefaultClassifier/training`.

![](_images/full_label_and_label.png)

`fullLabel` is the full description of the label.

```
@a_Android Settings_Android Settings Top Screen_[Network & internet Icon]
```

`label` is the short description.

```
[Network & internet Icon]
```

<br>
<hr>

### ImageIs1.kt

(`src/test/kotlin/tutorial/basic/ImageIs1.kt`)

```kotlin
    @Test
    @Order(10)
    fun imageIs() {

        scenario {
            case(1) {
                condition {
                    it.screenIs("[Android Settings Top Screen]")
                }.action {
                    v1 = it.detect("Network & internet")
                        .leftItem()
                }.expectation {
                    v1.imageIs("[Network & internet Icon]")
                }
            }
            case(2) {
                expectation {
                    v1.imageFullLabelIs("@a_Android Settings_Android Settings Top Screen_[Network & internet Icon]")
                }
            }
        }
    }
```

Right-click on `imageIs()` and select `debug` to run test.

### Console output

```
156	[00:01:06]	2025/02/19 23:28:28.452	{imageIs-1}	0	-	[EXPECTATION]	+1	!	()	expectation
157	[00:01:06]	2025/02/19 23:28:28.455	{imageIs-1}	0	-	[info]	+3	!	(imageIs)	<Network & internet>_leftItem.png
158	[00:01:06]	2025/02/19 23:28:28.521	{imageIs-1}	0	-	[info]	+66	!	(imageIs)	[ImageClassifier/classifyImage] in 0.063 sec
159	[00:01:06]	2025/02/19 23:28:28.524	{imageIs-1}	0	-	[info]	+3	!	(imageIs)	label: [Network & internet Icon]
160	[00:01:06]	2025/02/19 23:28:28.526	{imageIs-1}	0	-	[info]	+2	!	(imageIs)	159_text_ "", bounds_ [84,868][147,913] width=64, height=46, centerX=116, centerY=891, rect_ [84, 868, 147, 913](w=64, h=46).png
161	[00:01:06]	2025/02/19 23:28:28.532	{imageIs-1}	0	-	[OK]	+6	!	(imageIs)	Image of <Network & internet>:leftItem is [Network & internet Icon]
162	[00:01:06]	2025/02/19 23:28:28.532	{imageIs-2}	0	-	[CASE]	+0	!	()	(2)
163	[00:01:06]	2025/02/19 23:28:28.533	{imageIs-2}	0	-	[EXPECTATION]	+1	!	()	expectation
164	[00:01:06]	2025/02/19 23:28:28.580	{imageIs-2}	0	-	[info]	+47	!	(imageFullLabelIs)	[ImageClassifier/classifyImage] in 0.046 sec
165	[00:01:06]	2025/02/19 23:28:28.583	{imageIs-2}	0	-	[info]	+3	!	(imageFullLabelIs)	fullLabel: @a_Android Settings_Android Settings Top Screen_[Network & internet Icon]
166	[00:01:06]	2025/02/19 23:28:28.587	{imageIs-2}	0	-	[info]	+4	!	(imageFullLabelIs)	165_text_ "", bounds_ [84,868][147,913] width=64, height=46, centerX=116, centerY=891, rect_ [84, 868, 147, 913](w=64, h=46).png
167	[00:01:06]	2025/02/19 23:28:28.589	{imageIs-2}	0	-	[OK]	+2	!	(imageFullLabelIs)	Image fullLabel of <Network & internet>:leftItem is @a_Android Settings_Android Settings Top Screen_[Network & internet Icon]
```

<br>
<hr>

### FindImage1.kt

(`src/test/kotlin/tutorial/basic/FindImage1.kt`)

```kotlin
    @Test
    @Order(10)
    fun findImage() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    withScrollDown {
                        v1 = it.findImage("[Network & internet Icon]")
                        v2 = it.findImage("[Display Icon]")
                    }
                }.expectation {
                    v1.isFound.thisIsTrue("[Network & internet Icon] is found.")
                    v2.isFound.thisIsTrue("[Display Icon] is found.")
                }
            }
        }
    }
```

### Running test

1. Set `os` in `testrun.global.properties` to run as android (default is android).

```properties
## OS --------------------
#os=ios
```

2. Right-click on `findImage()` and select `debug` to run test.

### Console output

```
126	[00:00:20]	2025/01/28 16:14:35.571	{findImage-1}	0	-	[ACTION]	+3390	!	()	action
127	[00:00:20]	2025/01/28 16:14:35.574	{findImage-1}	0	-	[info]	+3	!	()	Trying to find image with scroll. (label="[Network & internet Icon]")
128	[00:00:20]	2025/01/28 16:14:35.761	{findImage-1}	0	-	[info]	+187	!	()	127_[0, 0, 1079, 2399](w=1080, h=2400).png
129	[00:00:21]	2025/01/28 16:14:36.100	{findImage-1}	0	-	[info]	+339	!	()	normalized_template_img.png
130	[00:00:23]	2025/01/28 16:14:38.305	{findImage-1}	0	-	[info]	+2205	!	()	[findImage] in 2.731 sec
131	[00:00:23]	2025/01/28 16:14:38.308	{findImage-1}	0	-	[info]	+3	!	()	Trying to find image with scroll. (label="[Display Icon]")
132	[00:00:23]	2025/01/28 16:14:38.442	{findImage-1}	0	-	[info]	+134	!	()	131_[0, 0, 1079, 2399](w=1080, h=2400).png
133	[00:00:23]	2025/01/28 16:14:38.541	{findImage-1}	0	-	[info]	+99	!	()	normalized_template_img.png
134	[00:00:25]	2025/01/28 16:14:40.018	{findImage-1}	0	-	[info]	+1477	!	()	findImage("[Display Icon]") not found. (distance 0.61345655 > 0.1)
135	[00:00:25]	2025/01/28 16:14:40.020	{findImage-1}	0	-	[operate]	+2	!	(scrollDown)	Scroll down
136	[00:00:29]	2025/01/28 16:14:44.178	{findImage-1}	0	-	[info]	+4158	!	()	endOfScroll=false
137	[00:00:29]	2025/01/28 16:14:44.180	{findImage-1}	0	-	[info]	+2	!	()	136_[0, 0, 1079, 2399](w=1080, h=2400).png
138	[00:00:29]	2025/01/28 16:14:44.259	{findImage-1}	0	-	[info]	+79	!	()	normalized_template_img.png
139	[00:00:31]	2025/01/28 16:14:46.636	{findImage-1}	0	-	[info]	+2377	!	()	[findImage] in 8.328 sec
140	[00:00:31]	2025/01/28 16:14:46.640	{findImage-1}	0	-	[EXPECTATION]	+4	!	()	expectation
141	[00:00:31]	2025/01/28 16:14:46.643	{findImage-1}	0	-	[OK]	+3	!	(thisIsTrue)	[Network & internet Icon] is found.
142	[00:00:31]	2025/01/28 16:14:46.644	{findImage-1}	0	-	[OK]	+1	!	(thisIsTrue)	[Display Icon] is found.
```

### Link

- [index](../../../index.md)
