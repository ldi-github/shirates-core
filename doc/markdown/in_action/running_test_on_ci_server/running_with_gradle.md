# Running with Gradle

You can run test with Gradle.

## Example 1

You can run test with `gradlew` in console.

1. Change directory to project dir(ex.`Practice1`).
2. Run test with `gradlew`.

```
wave1008@SNB-M1 ~ % cd Downloads/Practice1
wave1008@SNB-M1 Practice1 % ./gradlew cleanTest test

BUILD SUCCESSFUL in 35s
3 actionable tasks: 2 executed, 1 up-to-date
wave1008@SNB-M1 Practice1 %
```

## Example 2

You can create and run script.

1. Create file `runtest.sh` under the project root directory.

Content of **runtest.sh**

```
./gradlew cleanTest test
```

2. Run `runtest.sh`.

```
wave1008@SNB-M1 Practice1 % sh ./runtest.sh         

BUILD SUCCESSFUL in 35s
3 actionable tasks: 2 executed, 1 up-to-date
wave1008@SNB-M1 Practice1 % 
```

## Example 3

You can set [parameters](../../basic/parameter/parameters.md) via environment variables using "`SR_*`" prefix.

1. Rewrite `runtest.sh` as follows.

```
export SR_os="android"
export SR_profile="Pixel 3a API 31(Android 12)"
export SR_appiumServerUrl="http://127.0.0.1:4720/"
export SR_appiumArgs="--session-override --relaxed-security"
export SR_testResults="$HOME/Downloads/TestResults/Practice1"
export SR_testListDir="$HOME/Downloads/TestResults/Practice1"
./gradlew cleanTest test
```

2. Run `runtest.sh`.

```
wave1008@SNB-M1 Practice1 % sh ./runtest.sh 

BUILD SUCCESSFUL in 35s
3 actionable tasks: 2 executed, 1 up-to-date
wave1008@SNB-M1 Practice1 % 
```

### Link

- [index](../../index.md)
