plugins {
    val kotlin_version = "1.6.21"
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
version = "1.0.1"

val appiumClientVersion = "8.1.1"

val userHome = System.getProperty("user.home")

repositories {
    mavenCentral()
}

dependencies {

    implementation(kotlin("stdlib"))

    // Appium
    implementation("io.appium:java-client:$appiumClientVersion")

    // JUnit 5
    // (Required) Writing and executing Unit Tests on the JUnit Platform
    implementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.0")

    // Assert J
    implementation("org.assertj:assertj-core:3.23.1")
    testImplementation("org.assertj:assertj-core:3.23.1")

    // Apache POI
    implementation("org.apache.poi:poi:5.2.2")
    implementation("org.apache.poi:poi-ooxml:5.2.2")
    testImplementation("org.apache.poi:poi:5.2.2")
    testImplementation("org.apache.poi:poi-ooxml:5.2.2")

    // SLF4J NOP Binding
    // https://mvnrepository.com/artifact/org.slf4j/slf4j-nop
    testImplementation("org.slf4j:slf4j-nop:2.0.0")

    // Apache Log4j Core
    implementation("org.apache.logging.log4j:log4j-core:2.18.0")

    // Apache Commons IO
    implementation("commons-io:commons-io:2.11.0")
    testImplementation("commons-io:commons-io:2.11.0")

    // Apache Commons Text
    implementation("org.apache.commons:commons-text:1.9")
    testImplementation("org.apache.commons:commons-text:1.9")

    // Apache Commons Exec
    implementation("org.apache.commons:commons-exec:1.3")
    testImplementation("org.apache.commons:commons-exec:1.3")

    // Apache Commons Codec
    implementation("commons-codec:commons-codec:1.15")
    testImplementation("commons-codec:commons-codec:1.15")

    // Jackson Module Kotlin
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.3")
    testImplementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.3")

    // Jackson Dataformat XML
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.13.4")
    testImplementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.13.4")

    // org.json
    // workaround for 'java.lang.RuntimeException: Method getString in org.json.JSONObject not mocked.'
    implementation("org.json:json:20220320")
    testImplementation("org.json:json:20220320")

    // OkHttp
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    testImplementation("com.squareup.okhttp3:okhttp:4.10.0")

    // Dokka
    // https://github.com/Kotlin/dokka
    dokkaHtmlPlugin("org.jetbrains.dokka:kotlin-as-java-plugin:1.7.10")

    // Gson
    implementation("com.google.code.gson:gson:2.9.0")

    // BoofCV
    implementation("org.boofcv:boofcv-core:0.41")
    testImplementation("org.boofcv:boofcv-core:0.41")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "15"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "15"
    }
    val sourcesJar by creating(Jar::class) {
        archiveClassifier.set("sources")
        from(sourceSets["main"].allSource)
    }
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
            url = uri("$buildDir/repository")
        }
        maven {
            name = "ossrh"
            val releasesRepoUrl = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2")
            val snapshotsRepoUrl = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
            url = if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl
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
        "--add-exports", "java.desktop/sun.awt.image=ALL-UNNAMED"
    )

    // Filter test methods
    val envIncludeTestMatching = System.getenv("includeTestsMatching") ?: ""
    filter {
        if (envIncludeTestMatching.isNotBlank()) {
            val list = envIncludeTestMatching.split(",").map { it.trim() }
            for (item in list) {
                println("includeTestMatching($item)")
                includeTestsMatching(item)
            }
        } else {
            includeTestsMatching("*")
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

    val envs = System.getenv()
    val runtestPattern = if (envs.keys.contains("runtestPattern")) envs["runtestPattern"] else "*"
    println("runtestPattern=$runtestPattern")
    include(runtestPattern)
}

/**
 * spec
 */
tasks.register<JavaExec>("generateCode") {
    group = "spec"
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("shirates.core.task.CodeGeneratorExecute")
}
tasks.register<JavaExec>("createSpecReport") {
    group = "spec"
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("shirates.core.task.SpecReportExecute")
}
tasks.register<JavaExec>("createSummaryReport") {
    group = "spec"
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("shirates.core.task.SummaryReportExecute")
}

/**
 * spec-report
 */
tasks.register<Test>("spec-report-tutorial") {
    useJUnitPlatform()
    group = "spec-report"

    filter {
        includeTestsMatching("tutorial*")
        excludeTestsMatching("tutorial.inaction.Drivers*")
        excludeTestsMatching("tutorial.basic.iOSKeyboard1*")
    }

    environment("SR_noLoadRun", "true")
}