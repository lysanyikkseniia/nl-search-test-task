plugins {
    kotlin("jvm") version "1.9.23"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    testImplementation(kotlin("test-junit"))
    implementation("org.apache.lucene:lucene-core:8.10.1")
    implementation("org.apache.lucene:lucene-queryparser:8.10.1")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(15)
}