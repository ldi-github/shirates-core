# Could not complete execution for Gradle Test Executor (OutOfMemoryError: Java heap space)

## Message

```
org.gradle.api.internal.tasks.testing.TestSuiteExecutionException: Could not complete execution for Gradle Test Executor
```

## Stack trace

```
org.gradle.api.internal.tasks.testing.TestSuiteExecutionException: Could not complete execution for Gradle Test Executor 2.
	at org.gradle.api.internal.tasks.testing.SuiteTestClassProcessor.stop(SuiteTestClassProcessor.java:65)
	at java.base@17.0.14/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at java.base@17.0.14/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:77)
	at java.base@17.0.14/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.base@17.0.14/java.lang.reflect.Method.invoke(Method.java:569)
	at org.gradle.internal.dispatch.ReflectionDispatch.dispatch(ReflectionDispatch.java:36)
	at org.gradle.internal.dispatch.ReflectionDispatch.dispatch(ReflectionDispatch.java:24)
	at org.gradle.internal.dispatch.ContextClassLoaderDispatch.dispatch(ContextClassLoaderDispatch.java:33)
	at org.gradle.internal.dispatch.ProxyDispatchAdapter$DispatchingInvocationHandler.invoke(ProxyDispatchAdapter.java:92)
	at jdk.proxy1/jdk.proxy1.$Proxy4.stop(Unknown Source)
	at org.gradle.api.internal.tasks.testing.worker.TestWorker$3.run(TestWorker.java:200)
	at org.gradle.api.internal.tasks.testing.worker.TestWorker.executeAndMaintainThreadName(TestWorker.java:132)
	at org.gradle.api.internal.tasks.testing.worker.TestWorker.execute(TestWorker.java:103)
	at org.gradle.api.internal.tasks.testing.worker.TestWorker.execute(TestWorker.java:63)
	at org.gradle.process.internal.worker.child.ActionExecutionWorker.execute(ActionExecutionWorker.java:56)
	at org.gradle.process.internal.worker.child.SystemApplicationClassLoaderWorker.call(SystemApplicationClassLoaderWorker.java:121)
	at org.gradle.process.internal.worker.child.SystemApplicationClassLoaderWorker.call(SystemApplicationClassLoaderWorker.java:71)
	at app//worker.org.gradle.process.internal.worker.GradleWorkerMain.run(GradleWorkerMain.java:69)
	at app//worker.org.gradle.process.internal.worker.GradleWorkerMain.main(GradleWorkerMain.java:74)
Caused by: java.lang.OutOfMemoryError: Java heap space
	at org.apache.commons.io.IOUtils.byteArray(IOUtils.java:372)
	at org.apache.commons.io.output.AbstractByteArrayOutputStream.toByteArrayImpl(AbstractByteArrayOutputStream.java:201)
	at org.apache.commons.io.output.UnsynchronizedByteArrayOutputStream.toByteArray(UnsynchronizedByteArrayOutputStream.java:184)
	at org.apache.poi.util.IOUtils.toByteArray(IOUtils.java:261)
	at org.apache.poi.util.IOUtils.toByteArray(IOUtils.java:208)
	at org.apache.poi.openxml4j.util.ZipArchiveFakeEntry.<init>(ZipArchiveFakeEntry.java:83)
	at org.apache.poi.openxml4j.util.ZipInputStreamZipEntrySource.<init>(ZipInputStreamZipEntrySource.java:114)
	at org.apache.poi.openxml4j.opc.ZipPackage.<init>(ZipPackage.java:157)
	at org.apache.poi.openxml4j.opc.OPCPackage.open(OPCPackage.java:363)
	at org.apache.poi.ooxml.util.PackageHelper.open(PackageHelper.java:67)
	at org.apache.poi.xssf.usermodel.XSSFWorkbook.<init>(XSSFWorkbook.java:315)
	at org.apache.poi.xssf.usermodel.XSSFWorkbook.<init>(XSSFWorkbook.java:289)
	at shirates.spec.utilily.ExcelUtility.getWorkbook(ExcelUtility.kt:49)
	at shirates.spec.report.models.SpecReport.setupWorksheet(SpecReport.kt:48)
	at shirates.spec.report.models.SpecReport.output(SpecReport.kt:37)
	at shirates.spec.report.models.SpecReportExecutor.execute(SpecReportExecutor.kt:63)
	at shirates.core.logging.TestLog.outputSpecReport(TestLog.kt:1756)
	at shirates.core.testcode.UITestCallbackExtension.afterAll(UITestCallbackExtension.kt:392)
	at org.junit.jupiter.engine.descriptor.ClassBasedTestDescriptor.lambda$invokeAfterAllCallbacks$18(ClassBasedTestDescriptor.java:462)
	at org.junit.jupiter.engine.descriptor.ClassBasedTestDescriptor$$Lambda$1138/0x000000a00143fa50.execute(Unknown Source)
	at org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(ThrowableCollector.java:73)
	at org.junit.jupiter.engine.descriptor.ClassBasedTestDescriptor.lambda$invokeAfterAllCallbacks$19(ClassBasedTestDescriptor.java:462)
	at org.junit.jupiter.engine.descriptor.ClassBasedTestDescriptor$$Lambda$1137/0x000000a00143f818.accept(Unknown Source)
	at org.junit.platform.commons.util.CollectionUtils.forEachInReverseOrder(CollectionUtils.java:217)
	at org.junit.jupiter.engine.descriptor.ClassBasedTestDescriptor.invokeAfterAllCallbacks(ClassBasedTestDescriptor.java:461)
	at org.junit.jupiter.engine.descriptor.ClassBasedTestDescriptor.after(ClassBasedTestDescriptor.java:236)
	at org.junit.jupiter.engine.descriptor.ClassBasedTestDescriptor.after(ClassBasedTestDescriptor.java:85)
	at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$7(NodeTestTask.java:166)
	at org.junit.platform.engine.support.hierarchical.NodeTestTask$$Lambda$1091/0x000000a0019a0670.execute(Unknown Source)
	at org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(ThrowableCollector.java:73)
	at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$8(NodeTestTask.java:166)
	at org.junit.platform.engine.support.hierarchical.NodeTestTask$$Lambda$306/0x000000a00113f5c0.invoke(Unknown Source)
```

## Solution

Increase heap size of the test JVM.

### gradle.properties (example)

```properties
kotlin.code.style=official

org.gradle.jvmargs=-Xmx4g -Xms4g -XX:MaxMetaspaceSize=4g -Dkotlin.daemon.jvm.options=-Xmx4g
```

### build.gradle.kts (example)

```
tasks.test {
    useJUnitPlatform()
    maxHeapSize = "4G"
}
```

### See also

https://docs.gradle.org/current/dsl/org.gradle.api.tasks.testing.Test.html

### Link

- [Error messages / Warning messages](../error_warning_messages.md)

