plugins {
    kotlin("jvm") version "2.2.20"
    kotlin("plugin.spring") version "2.2.21"
    kotlin("plugin.serialization") version "2.2.20"
    id("org.springframework.boot") version "4.0.0"
    id("io.spring.dependency-management") version "1.1.7"
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-restclient")
    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    implementation("org.springframework.boot:spring-boot-starter-websocket")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")

    // Exposed ORM 및 SQLite 드라이버
    implementation("org.jetbrains.exposed:exposed-core:1.0.0-rc-3")
    implementation("org.jetbrains.exposed:exposed-jdbc:1.0.0-rc-3")
    implementation("org.jetbrains.exposed:exposed-dao:1.0.0-rc-3")
    implementation("org.jetbrains.exposed:exposed-java-time:0.50.1") // 날짜/시간 처리

    // MySQL
    implementation("mysql:mysql-connector-java:8.0.33")

    testImplementation("org.springframework.boot:spring-boot-starter-restclient-test")
    testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
    testImplementation("org.springframework.boot:spring-boot-starter-websocket-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
