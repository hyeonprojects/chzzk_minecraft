plugins {
    kotlin("jvm") version "2.2.0"
}

group = "kr.kro.chzzk.minecraft"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.8-R0.1-SNAPSHOT")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.jar {
    manifest {
        attributes["paperweight-mappings-namespace"] = "spigot"
    }
}

kotlin {
    jvmToolchain(21)
}
java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}