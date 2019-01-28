import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.20"
}

group = "kotlin-play"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    compile(kotlin("stdlib-jdk8"))
    compile(kotlin("reflect"))
    compile(files("libs/json-lib-2.4-jdk15.jar"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.1.1")
    // compile(fileTree("libs") { include("*.jar") })
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}