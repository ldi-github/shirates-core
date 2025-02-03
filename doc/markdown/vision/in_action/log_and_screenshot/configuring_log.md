# Configuring log (Vision)

You can configure log in testrun file.
See also [parameters](../../../common/parameter/parameters.md).

```
## Log --------------------
#logLanguage=ja
#enableSyncLog=false
#enableTestList=false
#enableTestClassList=false
#enableSpecReport=false
#enableRelativeCommandTranslation=false
#enableInnerMacroLog=true
#enableInnerCommandLog=true
#enableSilentLog=true
#enableTapElementImageLog=true
#enableXmlSourceDump=false
#enableRetryLog=false
#enableWarnOnRetryError=true
#enableWarnOnSelectTimeout=true
#enableGetSourceLog=true
#enableTrace=true
#enableTimeMeasureLog=true
#enableShellExecLog=true
#enableImageMatchDebugLog=true
#enableIsInViewLog=true
#enableIsSafeLog=false
#enableIsScreenLog=true
#testResults=
#testListDir={TEST_RESULTS}
#reportIndexDir={DIRECTORY_FOR_TEST_CONFIG}
```

## Suppress sync log

`enableSyncLog` is true by default. `Syncing screen.` is output.

```
135	[00:00:23]	2025/01/26 04:11:02.911	{airplaneModeSwitch}	0	-	[info]	+1024	!	(silent)	Syncing screen.
136	[00:00:23]	2025/01/26 04:11:03.153	{airplaneModeSwitch}	0	-	[info]	+242	!	(silent)	[syncScreenshot] in 0.502 sec
```

If you want to disable syncLog, set this parameter to false.

```
enableSyncLog=false
```

## Enable inner macro log

`enableInnerMacroLog` is false by default. Logs in macro is suppressed.

```
153	[00:00:21]	2025/01/26 05:15:40.740	{swipeTo-1}	0	-	[CASE]	+690	!	()	(1)
154	[00:00:21]	2025/01/26 05:15:40.740	{swipeTo-1}	0	-	[CONDITION]	+0	!	()	condition
155	[00:00:21]	2025/01/26 05:15:40.746	{swipeTo-1}	0	-	[operate]	+6	!	()	[Android Settings Top Screen]
156	[00:00:25]	2025/01/26 05:15:44.871	{swipeTo-1}	0	-	[info]	+4125	!	()	Syncing screen.
157	[00:00:25]	2025/01/26 05:15:45.062	{swipeTo-1}	0	-	[info]	+191	!	()	[syncScreenshot] in 0.405 sec
158	[00:00:25]	2025/01/26 05:15:45.149	{swipeTo-1}	0	-	[screenshot]	+87	!	()	screenshot: 158.png
159	[00:00:26]	2025/01/26 05:15:45.482	{swipeTo-1}	0	-	[info]	+333	!	()	[TextRecognizer/recognizeText] in 0.328 sec
160	[00:00:26]	2025/01/26 05:15:45.774	{swipeTo-1}	0	-	[info]	+292	!	()	159_[158.png]_recognized_text_rectangles.png
161	[00:00:26]	2025/01/26 05:15:45.953	{swipeTo-1}	0	-	[info]	+179	!	()	[detect] in 1.802 sec
```

If you want to see logs in macro, set this parameter to true. This is useful for debugging macro.

```
enableInnerMacroLog=true
```

```
154	[00:00:22]	2025/01/26 05:17:46.783	{swipeTo-1}	0	-	[CASE]	+381	!	()	(1)
155	[00:00:22]	2025/01/26 05:17:46.784	{swipeTo-1}	0	-	[CONDITION]	+1	!	()	condition
156	[00:00:22]	2025/01/26 05:17:46.786	{swipeTo-1}	0	-	[operate]	+2	!	()	[Android Settings Top Screen]
157	[00:00:22]	2025/01/26 05:17:47.513	{swipeTo-1}	1	[Android Settings Top Screen]	[info]	+727	!	()	Syncing screen.
158	[00:00:23]	2025/01/26 05:17:47.723	{swipeTo-1}	1	[Android Settings Top Screen]	[info]	+210	!	()	[syncScreenshot] in 0.422 sec
159	[00:00:23]	2025/01/26 05:17:47.809	{swipeTo-1}	1	[Android Settings Top Screen]	[screenshot]	+86	!	()	screenshot: 159.png
160	[00:00:24]	2025/01/26 05:17:48.875	{swipeTo-1}	1	[Android Settings Top Screen]	[info]	+1066	!	()	[ImageFeaturePrintClassifier/classifyWithImageFeaturePrintOrText] in 1.064 sec
161	[00:00:24]	2025/01/26 05:17:48.881	{swipeTo-1}	1	[Android Settings Top Screen]	[operate]	+6	!	(flickAndGoUp)	Flick and go up
162	[00:00:24]	2025/01/26 05:17:48.886	{swipeTo-1}	1	[Android Settings Top Screen]	[operate]	+5	!	(flickAndGoUp)	Scroll up
163	[00:00:24]	2025/01/26 05:17:48.893	{swipeTo-1}	1	[Android Settings Top Screen]	[info]	+7	!	(flickAndGoUp)	scrollableRect: [0,0][1079,2399] width=1080, height=2400, centerX=540, centerY=1200
164	[00:00:25]	2025/01/26 05:17:50.008	{swipeTo-1}	1	[Android Settings Top Screen]	[info]	+1115	!	(flickAndGoUp)	Syncing screen.
165	[00:00:25]	2025/01/26 05:17:50.192	{swipeTo-1}	1	[Android Settings Top Screen]	[info]	+184	!	(flickAndGoUp)	[syncScreenshot] in 0.440 sec
166	[00:00:25]	2025/01/26 05:17:50.287	{swipeTo-1}	1	[Android Settings Top Screen]	[screenshot]	+95	!	(flickAndGoUp)	screenshot: 166.png
167	[00:00:25]	2025/01/26 05:17:50.288	{swipeTo-1}	1	[Android Settings Top Screen]	[info]	+1	!	()	[flickAndGoUp] in 1.409 sec
168	[00:00:26]	2025/01/26 05:17:51.124	{swipeTo-1}	0	-	[info]	+836	!	()	[syncScreenshot] in 0.327 sec
169	[00:00:26]	2025/01/26 05:17:51.535	{swipeTo-1}	0	-	[info]	+411	!	()	[TextRecognizer/recognizeText] in 0.323 sec
170	[00:00:27]	2025/01/26 05:17:51.802	{swipeTo-1}	0	-	[info]	+267	!	()	169_[166.png]_recognized_text_rectangles.png
171	[00:00:27]	2025/01/26 05:17:51.982	{swipeTo-1}	0	-	[info]	+180	!	()	[detect] in 1.692 sec
```

## Enable tap element image log

This parameter is not supported in Vision mode.

### Link

- [Configuring screenshot](configuring_screenshot.md)


- [index(Vision)](../../../index.md)
- [index(Classic)](../../../classic/index.md)

