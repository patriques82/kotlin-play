import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.11"
}

group = "kotlin-play"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    compile(kotlin("stdlib-jdk8"))
    compile(kotlin("reflect"))
    compile("org.reflections:reflections:0.9.11")
    compile(files("libs/json-lib-2.4-jdk15.jar"))
    // compile(fileTree("libs") { include("*.jar") })
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}