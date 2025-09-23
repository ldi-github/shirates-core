plugins {
    val kotlin_version = "1.9.20"
    kotlin("jvm") version kotlin_version
    id("idea")
    id("com.vanniktech.maven.publish") version "0.34.0"
    id("signing")
    id("java")
//    id("org.jetbrains.dokka") version "1.6.21"
    id("com.github.gmazzo.buildconfig") version "2.0.2"
    jacoco
}

group = "io.github.ldi-github"
version = "8.6.4"

val appiumClientVersion = "9.4.0"

repositories {
    mavenCentral()
}

dependencies {

    implementation(kotlin("stdlib"))

    // Appium
    implementation("io.appium:java-client:$appiumClientVersion")

    // JUnit 5
    // (Required) Writing and executing Unit Tests on the JUnit Platform
    implementation("org.junit.jupiter:junit-jupiter-api:5.10.5")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.5")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.5")

    // Assert J
    implementation("org.assertj:assertj-core:3.27.3")
    testImplementation("org.assertj:assertj-core:3.27.3")

    // Apache POI
    implementation("org.apache.poi:poi:5.4.0")
    implementation("org.apache.poi:poi-ooxml:5.4.0")
    testImplementation("org.apache.poi:poi:5.4.0")
    testImplementation("org.apache.poi:poi-ooxml:5.4.0")

    // SLF4J NOP Binding
    // https://mvnrepository.com/artifact/org.slf4j/slf4j-nop
    testImplementation("org.slf4j:slf4j-nop:2.0.16")

    // Apache Log4j Core
    implementation("org.apache.logging.log4j:log4j-core:2.24.3")

    // Apache Commons IO
    implementation("commons-io:commons-io:2.18.0")
    testImplementation("commons-io:commons-io:2.18.0")

    // Apache Commons Text
    implementation("org.apache.commons:commons-text:1.13.0")
    testImplementation("org.apache.commons:commons-text:1.13.0")

    // Apache Commons Exec
    implementation("org.apache.commons:commons-exec:1.4.0")
    testImplementation("org.apache.commons:commons-exec:1.4.0")

    // Apache Commons Codec
    implementation("commons-codec:commons-codec:1.17.2")
    testImplementation("commons-codec:commons-codec:1.17.2")

    // Jackson Module Kotlin
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.18.2")
    testImplementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.18.2")

    // Jackson Dataformat XML
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.18.2")
    testImplementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.18.2")

    // org.json
    // workaround for 'java.lang.RuntimeException: Method getString in org.json.JSONObject not mocked.'
    implementation("org.json:json:20250107")
    testImplementation("org.json:json:20250107")

    // OkHttp
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    testImplementation("com.squareup.okhttp3:okhttp:4.12.0")

//    // Dokka
//    // https://github.com/Kotlin/dokka
//    dokkaHtmlPlugin("org.jetbrains.dokka:kotlin-as-java-plugin:1.9.20")

    // BoofCV(core)
    implementation("org.boofcv:boofcv-core:1.1.7")
    testImplementation("org.boofcv:boofcv-core:1.1.7")

    // BoofCV(swing)
    implementation("org.boofcv:boofcv-swing:1.1.7")
    testImplementation("org.boofcv:boofcv-swing:1.1.7")

    // jsoup
    implementation("org.jsoup:jsoup:1.18.3")
    testImplementation("org.jsoup:jsoup:1.18.3")

    // md2html
    implementation("io.github.ldi-github:md2html:0.2.0")

    // ICU4J
    implementation("com.ibm.icu:icu4j:76.1")
}

configurations.all {
    resolutionStrategy {

        // cache dynamic versions for 10 minutes
        cacheDynamicVersionsFor(10 * 60, "seconds")
        // don't cache changing modules at all
        cacheChangingModulesFor(0, "seconds")
    }
}

tasks {
    val sourcesJar by registering(Jar::class, fun Jar.() {
        archiveClassifier.set("sources")
        from(sourceSets["main"].allSource)
        mustRunAfter("generateBuildConfig")
    })
}

kotlin {
    jvmToolchain(17)
}

// Dokka
//tasks.dokkaHtml {
//    outputDirectory.set(buildDir.resolve("doc/html"))
//}
//tasks.dokkaGfm {
//    outputDirectory.set(buildDir.resolve("doc/markdown"))
//}
//tasks.dokkaJavadoc {
//    outputDirectory.set(buildDir.resolve("doc/javadoc"))
//}

/**
 * buildConfig
 *
 * gradle-buildconfig-plugin
 * https://github.com/gmazzo/gradle-buildconfig-plugin
 */
buildConfig {
    buildConfigField("String", "appName", "\"${project.name}\"")
    buildConfigField("String", "version", "\"${project.version}\"")
    buildConfigField("String", "packageName", "\"${project.group}\"")
    buildConfigField("String", "charset", "\"UTF-8\"")
    buildConfigField("String", "appiumClientVersion", "\"${appiumClientVersion}\"")
}

//java {
//    withJavadocJar()
//    withSourcesJar()
//}

mavenPublishing {
    publishToMavenCentral()

    signAllPublications()

    coordinates(group.toString(), "shirates-core", version.toString())

    pom {
        name = "shirates-core"
        description =
            "Shirates is an integration testing framework that makes it easy and fun to write test code for mobile apps. shirates-core is core library."
        inceptionYear = "2022"
        url = "https://github.com/ldi-github/shirates-core"
        licenses {
            license {
                name = "The Apache License, Version 2.0"
                url = "http://www.apache.org/licenses/LICENSE-2.0.txt"
                distribution = "http://www.apache.org/licenses/LICENSE-2.0.txt"
            }
        }
        developers {
            developer {
                id = "ldi-github"
                name = "ldi-github"
                url = "https://github.com/ldi-github"
            }
        }
        scm {
            url = "https://github.com/ldi-github/shirates-core"
            connection = "scm:git:git://github.com/ldi-github/shirates-core.git"
            developerConnection = "scm:git:ssh://git@github.com/ldi-github/shirates-core.git"
        }
    }
}

/**
 * verification
 */
tasks.test {
    useJUnitPlatform()
    jvmArgs = listOf(
        "--add-exports", "java.desktop/sun.awt.image=ALL-UNNAMED",
        "-Xmx4g", "-Xms4g"
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
    finalizedBy(tasks.jacocoTestReport)
}
tasks.jacocoTestReport {
    dependsOn(tasks.test)
}

/**
 * experiment
 */
tasks.register<Test>("runtest") {
    useJUnitPlatform()
    group = "experiment"
    jvmArgs = listOf(
        "--add-exports", "java.desktop/sun.awt.image=ALL-UNNAMED",
        "-Xmx4g", "-Xms4g"
    )

    val envs = System.getenv()
    val runtestPattern = if (envs.keys.contains("runtestPattern")) envs["runtestPattern"] else "*"
    println("runtestPattern=$runtestPattern")
    include(runtestPattern)
}

/**
 * test-spec-report
 */
tasks.register<Test>("test-spec-report-tutorial") {
    useJUnitPlatform()
    group = "test-spec-report"

    filter {
        includeTestsMatching("tutorial*")
        excludeTestsMatching("tutorial.inaction.Drivers*")
        excludeTestsMatching("tutorial.misc.iOSKeyboard1*")
    }

    environment("SR_noLoadRun", "true")
}

/**
 * spec-code-generation
 */
tasks.register<JavaExec>("generateCode") {
    group = "spec-code"
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("shirates.core.task.CodeGeneratorExecute")
}

/**
 * spec-report
 */
tasks.register<JavaExec>("createSpecReport") {
    group = "spec-report"
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("shirates.core.task.SpecReportExecute")
}
tasks.register<JavaExec>("createSummaryReport") {
    group = "spec-report"
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("shirates.core.task.SummaryReportExecute")
}

/**
 * spec-report-collected
 */
tasks.register<JavaExec>("collectSpecReport") {
    group = "spec-report-collected"
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("shirates.core.task.CollectSpecReportExecute")
}
tasks.register<JavaExec>("collectAndCreateSummaryReport") {
    dependsOn("collectSpecReport")
    group = "spec-report-collected"
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("shirates.core.task.SummaryReportFromCollectedExecute")
}
tasks.register<JavaExec>("createSummaryReportFromCollected") {
    group = "spec-report-collected"
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("shirates.core.task.SummaryReportFromCollectedExecute")
}

/**
 * md2html
 */
tasks.register<JavaExec>("md2html") {
    group = "md2html"
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("md2html.Executor")
}

