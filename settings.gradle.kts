pluginManagement {
    plugins {
        kotlin("jvm") version "2.2.20"
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "chzzk_minecraft"

// 하위 모듈 등록
include("paper-plugin")
include("spring-server")