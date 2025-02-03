plugins {
    val kotlin_version = "1.8.21"
    kotlin("jvm") version kotlin_version
//    kotlin("plugin.serialization") version kotlin_version
    id("idea")
    id("maven-publish")
    id("signing")
    id("java")
//    id("java-library")
    id("org.jetbrains.dokka") version "1.6.21"
    id("com.github.gmazzo.buildconfig") version "2.0.2"
    jacoco
}

group = "io.github.ldi-github"
version = "8.0.7"

val appiumClientVersion = "9.4.0"

val userHome = System.getProperty("user.home")

repositories {
    mavenCentral()
//    maven(url = "file:/$userHome/github/ldi-github/md2html/build/repository")
    maven(url = "https://s01.oss.sonatype.org/content/repositories/snapshots/")
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

    // Dokka
    // https://github.com/Kotlin/dokka
    dokkaHtmlPlugin("org.jetbrains.dokka:kotlin-as-java-plugin:1.9.20")

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
    compileKotlin {
        kotlinOptions.jvmTarget = "17"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "17"
    }

    val sourcesJar by registering(Jar::class, fun Jar.() {
        archiveClassifier.set("sources")
        from(sourceSets["main"].allSource)
        mustRunAfter("generateBuildConfig")
    })
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

java {
    withJavadocJar()
    withSourcesJar()
}

/**
 * publishing
 */
publishing {
    repositories {
        maven {
            name = "local"
            url = uri("build/repository")
        }
        maven {
            name = "ossrh"
            val releasesRepoUrl = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2")
            val snapshotsRepoUrl = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
            url = if (version.toString().contains("-")) snapshotsRepoUrl else releasesRepoUrl
            credentials {
                username = System.getenv("SHIRATES_CORE_OSSRH_USERNAME")
                password = System.getenv("SHIRATES_CORE_OSSRH_PASSWORD")
            }
        }
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/ldi-github/shirates-core")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("SHIRATES_CORE_GITHUB_USERNAME")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("SHIRATES_CORE_GITHUB_TOKEN")
            }
        }
    }
    publications {
        create<MavenPublication>("binaryAndSources") {
            from(components["kotlin"])
            artifact(tasks["sourcesJar"])
            artifact(tasks["javadocJar"])
            groupId = "${project.group}"
            artifactId = project.name
            version = "${project.version}"
            pom {
                name.set("shirates-core")
                description.set("Shirates is an integration testing framework that makes it easy and fun to write test code for mobile apps. shirates-core is core library.")
                url.set("https://github.com/ldi-github/shirates-core")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("wave1008")
                        name.set("Nobuhiro Senba")
                    }
                }
                scm {
                    connection.set("https://github.com/ldi-github/shirates-core.git")
                    developerConnection.set("git@github.com:shirates-core.git")
                    url.set("https://github.com/ldi-github/shirates-core")
                }
            }
        }

        register<MavenPublication>("gpr") {
            from(components["kotlin"])
            artifact(tasks["sourcesJar"])
            groupId = "${project.group}"
            artifactId = project.name
            version = "${project.version}"
        }
    }
}

signing {
    sign(publishing.publications["binaryAndSources"])
}

tasks.javadoc {
    if (JavaVersion.current().isJava9Compatible) {
        (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
    }
}

tasks.withType<PublishToMavenRepository>().configureEach {
    onlyIf {
        val r =
            (repository == publishing.repositories["local"] && publication == publishing.publications["binaryAndSources"]) ||
                    (repository == publishing.repositories["ossrh"] && publication == publishing.publications["binaryAndSources"]) ||
                    (repository == publishing.repositories["GitHubPackages"] && publication == publishing.publications["gpr"])
        r
    }
}
tasks.withType<PublishToMavenLocal>().configureEach {
    onlyIf {
        val r = publication == publishing.publications["binaryAndSources"]
        r
    }
}

tasks.register("publishToLocalRepository") {
    group = "publishing"
    description = "Publishes to local"
    dependsOn(tasks.withType<PublishToMavenRepository>().matching {
        it.repository == publishing.repositories["local"]
    })
}

tasks.register("publishToExternalRepository") {
    group = "publishing"
    description = "Publishes to external repository"
    dependsOn(tasks.withType<PublishToMavenRepository>().matching {
//        it.repository == publishing.repositories["GitHubPackages"]
        it.repository == publishing.repositories["ossrh"]
    })
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

