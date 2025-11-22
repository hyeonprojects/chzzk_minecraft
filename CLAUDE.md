# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

치지직(Chzzk) 플랫폼과 연동하여 후원 발생 시 마인크래프트 게임 내 이벤트를 자동 실행하는 Paper 플러그인입니다. Kotlin으로 작성되었으며 Paper API 1.21.8, Java 21을 타겟으로 합니다.

## Build Commands

- `./gradlew build` - 플러그인 빌드
- `./gradlew clean` - 빌드 정리
- `./gradlew jar` - 배포용 JAR 생성
- `./gradlew test` - 테스트 실행
- `./gradlew compileKotlin` - Kotlin 소스 컴파일

## Architecture

### Core Components
- **Main.kt** - JavaPlugin 확장, 의존성 주입 및 생명주기 관리
- **WebhookServer** - 내장 HTTP 서버 (com.sun.net.httpserver), 치지직 웹훅 수신
- **ChzzkApiClient** - 치지직 OAuth 토큰 발급/갱신/폐기 API 클라이언트
- **DatabaseManager** - Exposed ORM + SQLite 기반 사용자 데이터 영속화

### Data Flow
1. 치지직 플랫폼 → WebhookServer (POST /chzzk/notification)
2. WebhookHandlers → 후원 이벤트 파싱
3. Paper Scheduler → 메인 스레드에서 마인크래프트 이벤트 실행

### Package Structure
```
kr.kro.chzzk.minecraft/
├── api/          # 치지직 API 클라이언트 (JDK HttpClient + 코루틴)
├── command/      # /chzzk 명령어 처리
├── database/     # Exposed ORM 테이블 및 리포지토리
├── dto/          # 데이터 전송 객체 (kotlinx-serialization)
├── enum/         # 인증 타입 열거형
├── listener/     # Bukkit 이벤트 리스너
├── model/        # 도메인 모델
├── util/         # 설정, 메시지, 검증 유틸리티
└── webhook/      # 내장 HTTP 서버 및 핸들러
```

## Key Technical Details

- **코루틴**: API 호출은 `Dispatchers.IO`에서 비동기 처리
- **웹훅 서버**: 기본 포트 8080, `config.yml`에서 `webhook.port`, `webhook.enabled` 설정
- **데이터베이스**: `plugins/ChzzkMinecraftPlugin/chzzk_minecraft.db` (SQLite)
- **권한 시스템**: `chzzk.user`, `chzzk.link`, `chzzk.unlink`, `chzzk.status`, `chzzk.admin`

## Dependencies

- **Paper API** 1.21.8-R0.1-SNAPSHOT (compileOnly)
- **Exposed ORM** 0.44.0 (core, dao, jdbc)
- **SQLite JDBC** 3.43.0.0
- **kotlinx-serialization-json** 1.9.0
- **kotlinx-coroutines-core** 1.7.3
- **Socket.IO Client** 2.0.3

## Configuration

- Target JVM: Java 21
- Kotlin: 2.2.20
- plugin.yml: `main: kr.kro.chzzk.minecraft.Main`

## Deployment

빌드된 JAR를 Paper 서버의 `plugins/` 디렉토리에 배치 후 서버 재시작. 설정은 `plugins/ChzzkMinecraftPlugin/config.yml`에서 관리.