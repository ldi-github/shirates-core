# プロジェクトの作成

## 新しいプロジェクトの作成

1. IntelliJ IDEAを開き、`File > New > Project`を選択します。
1. New Project ウィンドウで`New Project` タブを選択します。
1. 以下の項目を入力します。
    - `Name: Practice1`
    - `Location: (保存場所を入力してください)`
    - `Language: Kotlin`
    - `Build system: Gradle`
    - `JDK: (好みのバージョンを選択してください)`
    - `Gradle DSL: Kotlin`
    - `Add sample code: OFF`
      <br>![](../_images/new_project.png)
1. `Create`をクリックします。
1. バックグラウンドタスクが完了するまで待ちます。これには数分かかる場合があります。
1. `build.gradle.kts`を下記の通りに編集します。

### build.gradle.kts (編集後)

```kotlin
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.21"
}

group = "org.example"
version = "1.0-SNAPSHOT"

val shiratesCoreVersion = "3.1.2"
val appiumClientVersion = "8.1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    // JUnit 5
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.0")

    // Appium
    testImplementation("io.appium:java-client:$appiumClientVersion")

    // shirates-core
    testImplementation("io.github.ldi-github:shirates-core:$shiratesCoreVersion")

    // https://mvnrepository.com/artifact/org.apache.logging.log4j/log4j-core
    testImplementation("org.apache.logging.log4j:log4j-core:2.19.0")

    // https://mvnrepository.com/artifact/org.slf4j/slf4j-nop
    testImplementation("org.slf4j:slf4j-nop:2.0.5")
}

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

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
```

Gradleペインの `reload` をクリックします。

![](../_images/gradle_refresh.png)

### Link

- [index](../../index_ja.md)

