# プロジェクトの作成 (Classic)

## 新しいプロジェクトの作成

1. IntelliJ IDEAを開き、`File > New > Project`を選択します。
1. 以下の項目を入力します。
    - `Name: Practice1`
    - `Location: (保存場所を入力してください)`
    - `Language: Kotlin`
    - `Build system: Gradle`
    - `JDK: (バージョンを選択してください)`
    - `Gradle DSL: Kotlin`
    - `Add sample code: OFF`
      <br>![](../_images/new_project.png)
1. `Create`をクリックします。
1. バックグラウンドタスクが完了するまで待ちます。これには数分かかる場合があります。
1. `build.gradle.kts`を下記の通りに編集します。

### build.gradle.kts (編集後)

```kotlin
plugins {
    kotlin("jvm") version "1.9.23"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val shiratesCoreVersion = "6.8.0"
val appiumClientVersion = "9.1.0"

dependencies {

    // JUnit 5
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.1")

    // Appium
    testImplementation("io.appium:java-client:$appiumClientVersion")

    // shirates-core
    testImplementation("io.github.ldi-github:shirates-core:$shiratesCoreVersion")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}
```

Gradleペインの `reload` をクリックします。

![](../_images/gradle_refresh.png)

### Link

- [index](../../index_ja.md)

