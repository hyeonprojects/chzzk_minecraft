plugins {
    kotlin("jvm") version "2.2.20"
    kotlin("plugin.serialization") version "2.2.20"

    // Spring Boot 플러그인 버전 관리 (적용 X)
    id("org.springframework.boot") version "4.0.0" apply false
    id("io.spring.dependency-management") version "1.1.7" apply false

    // Shadow 플러그인 (PaperMC 플러그인 빌드용)
    id("com.github.johnrengelman.shadow") version "8.1.1" apply false
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
    // Exposed ORM 및 SQLite 드라이버
    implementation("org.jetbrains.exposed:exposed-core:1.0.0-rc-3")
    implementation("org.jetbrains.exposed:exposed-jdbc:1.0.0-rc-3")
    implementation("org.jetbrains.exposed:exposed-dao:1.0.0-rc-3")
    implementation("org.jetbrains.exposed:exposed-java-time:0.50.1") // 날짜/시간 처리

    // MySQL
    implementation("com.zaxxer:HikariCP:5.1.0")
    implementation("mysql:mysql-connector-java:8.0.33")

    // Kotlin Serialization (JSON)
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")
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