# Creating project (Shirates/Vision)

## Create new project

1. Open IntelliJ IDEA, select `File > New > Project`.
1. In New Project window, select `Kotlin` tab.
1. Input fields.
    - `Name: Vision1`
    - `Location: (Your location)`
    - `Build system: Gradle`
    - `JDK: (Your choice)`
    - `Gradle DSL: Kotlin`
    - `Add sample code: OFF`
    - `Generate multi-module build`: OFF
      <br>![](../_images/new_project.png)
1. Click `Create`.
1. Wait for a while until background tasks finish. It may take minutes.
1. Edit `build.gradle.kts` as follows.

### build.gradle.kts (after edit)

```kotlin
plugins {
    kotlin("jvm") version "2.0.21"
}

val shiratesCoreVersion = "8.0.1"
val appiumClientVersion = "9.1.0"

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

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
```

Click reload on Gradle pane.

![](../_images/gradle_refresh.png)

### Link

- [index](../../vision-index.md)

