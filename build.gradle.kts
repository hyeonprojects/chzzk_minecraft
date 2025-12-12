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

// 루트 프로젝트는 공통 설정만 관리
// 각 서브모듈이 독립적으로 repositories와 dependencies 정의

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