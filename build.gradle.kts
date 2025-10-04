plugins {
    kotlin("jvm") version "2.2.0"
}

group = "kr.kro.chzzk.minecraft"
version = "0.1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencies {
    implementation("org.jetbrains.exposed:exposed-core:0.44.0")
    implementation("org.jetbrains.exposed:exposed-dao:0.44.0")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.44.0")
    implementation("org.xerial:sqlite-jdbc:3.43.0.0")

    // 코루틴만 유지 (비동기 처리용)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

    compileOnly("io.papermc.paper:paper-api:1.21.8-R0.1-SNAPSHOT")
    testImplementation(kotlin("test"))
}

tasks.processResources {
    val props = mapOf(
        "version" to version,
        "group" to project.group
    )
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}