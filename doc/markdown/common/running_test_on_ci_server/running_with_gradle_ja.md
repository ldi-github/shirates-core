# テストをGradleで実行する (Vision/Classic)

テストをGradleで実行することができます。

## 例1

コンソールで`gradlew`を使用してテストを実行することができます。

1. プロジェクトのディレクトリへ移動します。
2. `gradlew`を使用してテストを実行します。

```
wave1008@SNB-M1 ~ % cd Downloads/Practice1
wave1008@SNB-M1 Practice1 % ./gradlew cleanTest test

BUILD SUCCESSFUL in 35s
3 actionable tasks: 2 executed, 1 up-to-date
wave1008@SNB-M1 Practice1 %
```

## 例2

スクリプトを作成して実行することができます。

1. プロジェクトのルートディレクトに`runtest.sh`を作成します。

**runtest.sh** の内容

```
./gradlew cleanTest test
```

2. `runtest.sh`を実行します。

```
wave1008@SNB-M1 Practice1 % sh ./runtest.sh         

BUILD SUCCESSFUL in 35s
3 actionable tasks: 2 executed, 1 up-to-date
wave1008@SNB-M1 Practice1 % 
```

## 例3

"`SR_`"のプレフィックスを付与して環境変数経由で[パラメーター](../../basic/parameter/parameters_ja.md)を設定することができます。

1. `runtest.sh`を以下のように書き換えます。

```
export SR_os="android"
export SR_profile="Pixel 8(Android 14)"
export SR_appiumServerUrl="http://127.0.0.1:4720/"
export SR_appiumArgs="--session-override --relaxed-security"
export SR_testResults="$HOME/Downloads/TestResults/Practice1"
export SR_testListDir="$HOME/Downloads/TestResults/Practice1"
./gradlew cleanTest test
```

2. `runtest.sh`を実行します。

```
wave1008@SNB-M1 Practice1 % sh ./runtest.sh 

BUILD SUCCESSFUL in 35s
3 actionable tasks: 2 executed, 1 up-to-date
wave1008@SNB-M1 Practice1 % 
```

## 例4

環境変数`includeTestsMatching`を使用して実行するテストを指定することができます。

1. `build.gradle.kts`の **tasks.test** にフィルター処理を記述します。

```kotlin
tasks.test {
    useJUnitPlatform()
    jvmArgs = listOf(
        "--add-exports", "java.desktop/sun.awt.image=ALL-UNNAMED"
    )

    // Filter test methods
    val envIncludeTestMatching = System.getenv("includeTestsMatching") ?: "*"
    val list = envIncludeTestMatching.split(",").map { it.trim() }
    filter {
        for (item in list) {
            println("includeTestMatching($item)")
            includeTestsMatching(item)
        }
    }
}
```

2. カンマ区切りでテストを指定することができます。

```
export includeTestsMatching="product1.Test1,product1.Test2,product1.Test3"
```

### Link

- [index(Vision)](../../index_ja.md)
- [index(Classic)](../../classic/index_ja.md)
