# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Minecraft Paper plugin written in Kotlin, designed to integrate with the Chzzk platform. The plugin is built using Gradle and targets Paper API 1.21.8 with Java 21.

## Build Commands

### Root Project
- `./gradlew build` - Builds all subprojects (paper_plugin + webhook_server)
- `./gradlew clean` - Cleans all subprojects

### Paper Plugin
- `./gradlew :paper_plugin:build` - Builds the Paper plugin
- `./gradlew :paper_plugin:jar` - Creates the plugin JAR file for deployment
- `./gradlew :paper_plugin:test` - Runs the plugin test suite

### Webhook Server
- `./gradlew :webhook_server:build` - Builds the webhook server
- `./gradlew :webhook_server:run` - Runs the webhook server
- `./gradlew :webhook_server:test` - Runs the webhook server test suite

## Development Commands

### Paper Plugin
- `./gradlew :paper_plugin:compileKotlin` - Compiles the plugin Kotlin source
- `./gradlew :paper_plugin:processResources` - Processes plugin.yml and resources

### Webhook Server
- `./gradlew :webhook_server:compileKotlin` - Compiles the server Kotlin source

## Project Structure

Multi-module Gradle project with separate Paper plugin and webhook server:

```
chzzk_minecraft/
├── paper_plugin/                           # Minecraft Paper Plugin
│   ├── src/main/kotlin/kr/kro/chzzk/minecraft/
│   │   ├── Main.kt                         # Primary plugin class extending JavaPlugin
│   │   ├── api/ChzzkAuthApiClient.kt       # Chzzk API integration
│   │   ├── command/                        # Plugin commands
│   │   ├── database/                       # Database layer
│   │   ├── event/ & listener/              # Minecraft event handlers
│   │   └── util/                           # Utility classes
│   ├── src/main/resources/plugin.yml       # Paper plugin configuration
│   └── build.gradle.kts                    # Plugin build configuration
├── webhook_server/                         # Webhook Server (empty structure)
├── settings.gradle.kts                     # Root project settings
└── gradlew*                                # Gradle wrapper
```

## Plugin Architecture

The plugin is structured as a standard Paper plugin with the main class `kr.kro.chzzk.minecraft.main` extending `JavaPlugin`. The plugin.yml is configured for Paper API version 1.21.8.

Key architectural components:
- Main plugin class handles enable/disable lifecycle
- Separate packages for Chzzk platform integration and Minecraft event handling
- Uses Paper-specific APIs and mappings (spigot namespace in manifest)

## Java/Kotlin Configuration

- Target JVM: Java 21
- Kotlin version: 2.2.0
- Paper API: 1.21.8-R0.1-SNAPSHOT
- Code style: Official Kotlin conventions
- Testing framework: JUnit Platform (via kotlin-test)

## Dependencies

- **Paper API** (`io.papermc.paper:paper-api`) - Core Minecraft server API (compileOnly)
- **Kotlin Test** - Testing framework for Kotlin code

## Plugin Deployment

The built JAR from `./gradlew jar` should be placed in the Paper server's `plugins/` directory. The plugin uses Paper's mapping namespace configuration for proper runtime compatibility.