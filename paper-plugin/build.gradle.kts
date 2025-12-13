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

        archiveFileName.set("paper-plugin-${project.version}.jar")
    }
}