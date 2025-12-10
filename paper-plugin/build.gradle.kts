plugins {
    kotlin("jvm") version "2.2.20"
    kotlin("plugin.serialization") version "2.2.20"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    // Paper API
    compileOnly("io.papermc.paper:paper-api:1.21.8-R0.1-SNAPSHOT")

    // Kotlin Standard Library
    implementation(kotlin("stdlib"))

    // Exposed ORM 및 SQLite 드라이버
    implementation("org.jetbrains.exposed:exposed-core:1.0.0-rc-3")
    implementation("org.jetbrains.exposed:exposed-jdbc:1.0.0-rc-3")
    implementation("org.jetbrains.exposed:exposed-dao:1.0.0-rc-3")
    implementation("org.jetbrains.exposed:exposed-java-time:0.50.1") // 날짜/시간 처리

    // MySQL + HikariCP
    implementation("com.zaxxer:HikariCP:5.1.0")
    implementation("mysql:mysql-connector-java:8.0.33")

    // Kotlin Serialization (JSON)
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")

    // 코루틴만 유지 (비동기 처리용)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

    testImplementation(kotlin("test"))
}

tasks {
    build {
        dependsOn(shadowJar) // build 시 shadowJar 작업 포함
    }

    shadowJar {
        relocate("kotlin", "kr.kro.chzzk.minecraft.libs.kotlin")
        relocate("org.jetbrains.exposed", "kr.kro.chzzk.minecraft.libs.exposed")

        archiveFileName.set("paper-plugin-${project.version}.jar")
    }
}