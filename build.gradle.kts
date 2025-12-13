plugins {
    kotlin("jvm") version "2.2.20" apply false
    kotlin("plugin.serialization") version "2.2.20" apply false

    // Spring Boot 플러그인 버전 관리 (적용 X)
    id("org.springframework.boot") version "4.0.0" apply false
    id("io.spring.dependency-management") version "1.1.7" apply false

    // Shadow 플러그인 (PaperMC 플러그인 빌드용)
    id("com.github.johnrengelman.shadow") version "8.1.1" apply false
}

group = "kr.kro.chzzk.minecraft"
version = "0.1.0-SNAPSHOT"

// 루트 프로젝트는 플러그인 버전 관리만 담당
// 각 서브모듈이 독립적으로 플러그인 적용, repositories, dependencies 정의