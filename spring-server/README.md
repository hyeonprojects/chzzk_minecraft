# Chzzk Server - 헥사고날 아키텍처

치지직 마인크래프트 연동 Spring Boot 서버 (Hexagonal Architecture)

## 🏗️ 아키텍처 구조

```
spring-server/
└── src/main/kotlin/kr/kro/chzzk/server/
    ├── domain/                          # 도메인 레이어 (비즈니스 로직의 핵심)
    │   ├── model/                       # 도메인 엔티티 & Value Objects
    │   │   ├── User.kt                  # 사용자 도메인 엔티티
    │   │   ├── ChzzkProfile.kt          # 치지직 프로필 Value Object
    │   │   └── Donation.kt              # 후원 도메인 엔티티
    │   └── port/                        # 포트 인터페이스 (도메인 경계)
    │       ├── incoming/                # Inbound Ports (Use Cases)
    │       │   ├── LinkChzzkAccountUseCase.kt
    │       │   ├── GetUserUseCase.kt
    │       │   └── ProcessDonationUseCase.kt
    │       └── outgoing/                # Outbound Ports (외부 의존성 인터페이스)
    │           ├── UserRepository.kt
    │           ├── DonationRepository.kt
    │           ├── ChzzkApiClient.kt
    │           └── MinecraftServerClient.kt
    │
    ├── application/                     # 애플리케이션 레이어 (Use Case 구현)
    │   └── service/
    │       ├── LinkChzzkAccountService.kt
    │       ├── GetUserService.kt
    │       └── ProcessDonationService.kt
    │
    ├── adapter/                         # 어댑터 레이어 (외부 세계와의 연결)
    │   ├── incoming/                    # Inbound Adapters (외부 → 도메인)
    │   │   └── web/
    │   │       ├── rest/                # REST API Controllers
    │   │       │   ├── UserController.kt
    │   │       │   └── WebhookController.kt
    │   │       └── dto/                 # 요청/응답 DTO
    │   │           ├── UserDto.kt
    │   │           └── DonationDto.kt
    │   └── outgoing/                    # Outbound Adapters (도메인 → 외부)
    │       ├── persistence/             # 영속성 어댑터 (Database)
    │       │   ├── UserPersistenceAdapter.kt
    │       │   ├── DonationPersistenceAdapter.kt
    │       │   └── entity/              # JPA Entities
    │       │       ├── UserJpaEntity.kt
    │       │       └── DonationJpaEntity.kt
    │       └── external/                # 외부 API 어댑터
    │           ├── ChzzkApiClientAdapter.kt
    │           └── MinecraftServerClientAdapter.kt
    │
    ├── config/                          # 설정 클래스
    │   ├── JpaConfig.kt
    │   ├── RestClientConfig.kt
    │   └── WebConfig.kt
    │
    └── ChzzkServerApplication.kt        # Spring Boot Main Application
```

## 📐 헥사고날 아키텍처 레이어 설명

### 1️⃣ Domain Layer (도메인 레이어)
**역할**: 비즈니스 로직의 핵심, 외부 의존성 없음

- **Model**: 순수한 비즈니스 엔티티와 Value Objects
  - `User`: 사용자 도메인 엔티티 (치지직 계정 연결/해제 로직 포함)
  - `ChzzkProfile`: 치지직 프로필 불변 값 객체
  - `Donation`: 후원 도메인 엔티티

- **Ports**: 도메인과 외부 세계 간의 경계 인터페이스
  - **Incoming Ports (Use Cases)**: 애플리케이션이 제공하는 기능
    - `LinkChzzkAccountUseCase`: 치지직 계정 연결
    - `GetUserUseCase`: 사용자 조회
    - `ProcessDonationUseCase`: 후원 처리

  - **Outgoing Ports**: 도메인이 필요로 하는 외부 기능
    - `UserRepository`: 사용자 영속성
    - `ChzzkApiClient`: 치지직 API 통신
    - `MinecraftServerClient`: 마인크래프트 서버 통신

### 2️⃣ Application Layer (애플리케이션 레이어)
**역할**: Use Case 구현, 비즈니스 로직 조율

- **Service**: Use Case 인터페이스 구현체
  - `LinkChzzkAccountService`: 계정 연결 비즈니스 플로우
  - `GetUserService`: 사용자 조회 로직
  - `ProcessDonationService`: 후원 처리 및 알림 전송

### 3️⃣ Adapter Layer (어댑터 레이어)
**역할**: 외부 세계와 도메인 간의 변환

#### Inbound Adapters (외부 → 도메인)
- **REST Controllers**: HTTP 요청을 Use Case 호출로 변환
  - `UserController`: 사용자 관련 API 엔드포인트
  - `WebhookController`: 치지직 웹훅 처리

#### Outbound Adapters (도메인 → 외부)
- **Persistence Adapters**: Domain Repository를 JPA로 구현
  - `UserPersistenceAdapter`: JPA를 이용한 사용자 저장소
  - `DonationPersistenceAdapter`: JPA를 이용한 후원 저장소

- **External Adapters**: 외부 API 통신 구현
  - `ChzzkApiClientAdapter`: 치지직 API 클라이언트
  - `MinecraftServerClientAdapter`: 마인크래프트 서버 통신

## 🔄 데이터 흐름 예시

### 치지직 계정 연결 플로우
```
1. HTTP Request (POST /api/users/{uuid}/link-chzzk)
   ↓
2. UserController (Inbound Adapter)
   - DTO → Command 변환
   ↓
3. LinkChzzkAccountService (Application Layer)
   - 비즈니스 로직 조율
   ↓
4. Outbound Adapters 호출
   - UserRepository.findByMinecraftUuid()
   - ChzzkApiClient.getAccessToken()
   - UserRepository.save()
   ↓
5. Domain Model (User)
   - user.linkChzzkAccount() 비즈니스 로직 실행
   ↓
6. HTTP Response
   - Domain → DTO 변환 후 반환
```

## 🎯 헥사고날 아키텍처의 장점

1. **비즈니스 로직 독립성**
   - Domain Layer는 외부 기술(Spring, JPA, HTTP)에 의존하지 않음
   - 순수 Kotlin 코드로만 작성

2. **테스트 용이성**
   - Port 인터페이스를 Mock으로 대체하여 단위 테스트 작성 가능
   - 비즈니스 로직만 격리하여 테스트

3. **유연한 어댑터 교체**
   - JPA → Exposed ORM 변경
   - REST API → gRPC 변경
   - 변경 시 Domain Layer는 영향 없음

4. **명확한 의존성 방향**
   - 모든 의존성이 외부 → Domain Layer 방향
   - Domain이 중심, Adapter는 플러그인

## 🚀 실행 방법

### 1. 데이터베이스 설정
```yaml
# application.yml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/chzzk_minecraft
    username: root
    password: your_password
```

### 2. 애플리케이션 실행
```bash
./gradlew :spring-server:bootRun
```

### 3. API 테스트
```bash
# 헬스 체크
curl http://localhost:8080/api/webhooks/health

# 사용자 조회
curl http://localhost:8080/api/users/minecraft/{uuid}

# 치지직 계정 연결
curl -X POST http://localhost:8080/api/users/{uuid}/link-chzzk \
  -H "Content-Type: application/json" \
  -d '{"authCode": "example_code", "state": "example_state"}'
```

## 📝 추가 구현 필요 사항

1. **ChzzkApiClientAdapter**: 실제 치지직 API 통신 구현
2. **MinecraftServerClientAdapter**: WebSocket/RPC 연동
3. **인증/인가**: Spring Security 추가
4. **예외 처리**: Global Exception Handler
5. **로깅**: 구조화된 로깅 추가
6. **테스트**: 단위/통합 테스트 작성

## 📚 참고 자료

- [Hexagonal Architecture](https://alistair.cockburn.us/hexagonal-architecture/)
- [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [Ports and Adapters Pattern](https://herbertograca.com/2017/09/14/ports-adapters-architecture/)
