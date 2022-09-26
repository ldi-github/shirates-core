# Creating project

## Preparation

Now preparing remote repository. Currently local publishing is available.
Setup [Local Publishing](../../advanced/local_publishing.md).

## Create new project

1. Open IntelliJ IDEA, select `File > New > Project`.
1. In New Project window, select `New Project` tab.
1. Input fields.
    - Name
    - Location
    - Language (select `Kotlin`)
    - Build system (select `Gradle`)
    - JDK
    - Gradle DSL (select `Kotlin`)
      <br>![](../_images/new_project.png)
1. Click `Create`.
1. Wait for a while until background tasks finish. It may take minutes.

### JVM setting

If you encountered tooltip `Found invalid Gradle JVM configuration`, set JVM version.

![](../_images/invalid_gradle_jvm_configuration.png)

1. `File > Project Structure`
2. `Project Settings > Project`
3. Set SDK to compatible one.

![](../_images/project_sdk.png)

### build.gradle.kts (after created)

```kotlin
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
```

Append some lines to the file.

### build.gradle.kts (after edit)

```kotlin
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
}

group = "org.example"
version = "1.0-SNAPSHOT"

val shiratesCoreVersion = "0.9.0-SNAPSHOT"
val appiumClientVersion = "8.1.0"
val userHome = System.getProperty("user.home")

repositories {
    mavenCentral()
    maven(url = "file:/$userHome/github/ldi-github/shirates-core/build/repository")
}

dependencies {
    testImplementation(kotlin("test"))

    // JUnit 5
    // (Required) Writing and executing Unit Tests on the JUnit Platform
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.0")

    // Appium
    testImplementation("io.appium:java-client:$appiumClientVersion")

    // shirates-core
    implementation("shirates:shirates-core:$shiratesCoreVersion")
    testImplementation("shirates:shirates-core:$shiratesCoreVersion")

    // https://mvnrepository.com/artifact/org.apache.logging.log4j/log4j-core
    implementation("org.apache.logging.log4j:log4j-core:2.18.0")

    // https://mvnrepository.com/artifact/org.slf4j/slf4j-nop
    testImplementation("org.slf4j:slf4j-nop:1.7.36")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

configurations.all {
    resolutionStrategy {

        // cache dynamic versions for 10 minutes
        cacheDynamicVersionsFor(10 * 60, "seconds")
        // don't cache changing modules at all
        cacheChangingModulesFor(0, "seconds")
    }
}
```

Click reload on Gradle pane.

![](../_images/gradle_refresh.png)

### Link

- [index](../../index.md)

