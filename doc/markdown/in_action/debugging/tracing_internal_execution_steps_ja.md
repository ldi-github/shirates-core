# 内部実行ステップのトレース

以下の設定を行うと各コマンドの内部実行ステップをトレースすることができます。

### testrun.global.properties

```properties
enableTrace=true
enableTimeMeasureLog=true
```

## 例

```
lineNo	[elapsedTime]	logDateTime	{testCaseId}	macroDepth	macroName	[logType]	diff(ms)	(group)	message
...
343	[00:00:15]	2023/07/24 01:06:00.842	{missingSelector_ERROR}	0	-	[operate]	+1	C	(launchApp)	Launch app <Settings>
344	[00:00:15]	2023/07/24 01:06:00.843	{missingSelector_ERROR}	0	-	[trace]	+1	C	(launchApp)	[TestDriver.screenshotCore$shirates_core] screenshot() skipped. (449 < 500.0)
345	[00:00:15]	2023/07/24 01:06:01.132	{missingSelector_ERROR}	0	-	[trace]	+289	C	(launchApp)	[WaitUtility.doUntilTrue$loopAction] doUntilTrue(1)
346	[00:00:15]	2023/07/24 01:06:01.173	{missingSelector_ERROR}	0	-	[trace]	+41	C	(launchApp)	[TestDriverCommandContext.execSilentCommand] -start(346)
347	[00:00:15]	2023/07/24 01:06:01.174	{missingSelector_ERROR}	0	-	[trace]	+1	C	(launchApp)	[TestDriver.syncCache] syncCache called recursively.
348	[00:00:15]	2023/07/24 01:06:01.175	{missingSelector_ERROR}	0	-	[trace]	+1	C	(launchApp)	[TestDriver.syncCache] syncCache called recursively.
349	[00:00:15]	2023/07/24 01:06:01.175	{missingSelector_ERROR}	0	-	[trace]	+0	C	(launchApp)	[TestElementCache.select] <.android.widget.TextView&&#com.android.settings:id/homepage_title&&Settings&&focusable=false&&scrollable=false> -start(349)
350	[00:00:15]	2023/07/24 01:06:01.176	{missingSelector_ERROR}	0	-	[trace]	+1	C	(launchApp)	[TestElementCache.select] <.android.widget.TextView&&#com.android.settings:id/homepage_title&&Settings&&focusable=false&&scrollable=false> -end (349->350: 1[ms])
351	[00:00:15]	2023/07/24 01:06:01.176	{missingSelector_ERROR}	0	-	[silent]	+0	C	(launchApp)	
352	[00:00:15]	2023/07/24 01:06:01.176	{missingSelector_ERROR}	0	-	[info]	+0	C	(launchApp)	Syncing (1)
353	[00:00:15]	2023/07/24 01:06:01.177	{missingSelector_ERROR}	0	-	[trace]	+1	C	(launchApp)	[TestDriver.refreshCache] refreshCache -start(353)
354	[00:00:15]	2023/07/24 01:06:01.177	{missingSelector_ERROR}	0	-	[trace]	+0	C	(launchApp)	[WaitUtility.doUntilTrue$loopAction] doUntilTrue(1)
355	[00:00:15]	2023/07/24 01:06:01.177	{missingSelector_ERROR}	0	-	[trace]	+0	C	(launchApp)	[AppiumProxy.getSourceCore] getSourceCore() -start(355)
356	[00:00:16]	2023/07/24 01:06:01.800	{missingSelector_ERROR}	0	-	[trace]	+623	C	(launchApp)	[ElementCacheUtility.createTestElementFromXml] -trace
357	[00:00:16]	2023/07/24 01:06:01.804	{missingSelector_ERROR}	0	-	[trace]	+4	C	(launchApp)	[AppiumProxy.getSourceCore] getSourceCore() -end (355->357: 627[ms])
...
```

### Link

- [index](../../index_ja.md)

