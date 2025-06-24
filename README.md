# Order System - Microservices Architecture

Spring Cloud 기반의 마이크로서비스 아키텍처로 구현된 주문 시스템입니다.

## 🏗️ 아키텍처 개요

이 프로젝트는 다음과 같은 마이크로서비스들로 구성되어 있습니다:

- **API Gateway** (포트: 8080) - 모든 요청의 진입점
- **Eureka Server** (포트: 8761) - 서비스 디스커버리
- **Config Server** (포트: 8888) - 중앙화된 설정 관리
- **Member Service** - 회원 관리
- **Product Service** - 상품 관리
- **Ordering Service** - 주문 관리
- **Article Service** - 게시글 관리
- **Like Service** - 좋아요 기능

## 🛠️ 기술 스택

### Backend

- **Java 17**
- **Spring Boot 3.4.2**
- **Spring Cloud 2024.0.0**
- **Spring Data JPA**
- **MySQL**
- **Redis**
- **JWT** (인증)

### Infrastructure

- **Spring Cloud Gateway** - API Gateway
- **Netflix Eureka** - Service Discovery
- **Spring Cloud Config** - Configuration Management
- **Spring Cloud Bus** - Configuration Refresh
- **Apache Kafka** - 메시지 브로커
- **RabbitMQ** - 메시지 브로커

### Development Tools

- **Gradle** - 빌드 도구
- **Lombok** - 코드 간소화
- **Docker Compose** - Kafka/Zookeeper 컨테이너

## 📁 프로젝트 구조

```
inf-msa/
├── apigateway/          # API Gateway 서비스
├── eureka/             # Service Discovery 서버
├── configserver/       # Configuration 서버
├── member/             # 회원 관리 서비스
├── product/            # 상품 관리 서비스
├── ordering/           # 주문 관리 서비스
├── article/            # 게시글 관리 서비스
├── like/               # 좋아요 기능 서비스
└── README.md
```

## 🚀 실행 방법

### 1. 필수 환경 설정

다음 서비스들이 사전에 실행되어야 합니다:

- **MySQL** (포트: 3377)
- **Redis** (포트: 6379)
- **RabbitMQ** (포트: 5672)

### 2. Kafka 실행 (Docker Compose)

```bash
cd ordering
docker-compose up -d
```

### 3. 서비스 실행 순서

1. **Config Server** 실행

```bash
cd configserver
./gradlew bootRun
```

2. **Eureka Server** 실행

```bash
cd eureka
./gradlew bootRun
```

3. **API Gateway** 실행

```bash
cd apigateway
./gradlew bootRun
```

4. **각 마이크로서비스** 실행

```bash
# Member Service
cd member
./gradlew bootRun

# Product Service
cd product
./gradlew bootRun

# Ordering Service
cd ordering
./gradlew bootRun

# Article Service
cd article
./gradlew bootRun

# Like Service
cd like
./gradlew bootRun
```

## 🔐 인증 및 보안

### JWT 인증

- API Gateway에서 JWT 토큰 검증
- 인증이 필요한 경로는 JWT 토큰 필수
- 인증이 불필요한 경로:
  - `/member/create` - 회원가입
  - `/member/doLogin` - 로그인
  - `/member/refresh-token` - 토큰 갱신
  - `/articles/` - 게시글 조회 (모든 경로)

### 헤더 정보

인증된 요청의 경우 다음 헤더가 추가됩니다:

- `X-User-Id`: 사용자 ID
- `X-User-Role`: 사용자 역할 (ROLE\_ 접두사 포함)

## 🌐 API 엔드포인트

### API Gateway (포트: 8080)

모든 요청은 API Gateway를 통해 라우팅됩니다:

- **회원 서비스**: `http://localhost:8080/member-service/**`
- **주문 서비스**: `http://localhost:8080/ordering-service/**`
- **상품 서비스**: `http://localhost:8080/product-service/**`
- **게시글 서비스**: `http://localhost:8080/article-service/**`

### 서비스 디스커버리 (포트: 8761)

- **Eureka Dashboard**: `http://localhost:8761`

### 설정 서버 (포트: 8888)

- **Config Server**: `http://localhost:8888`

## 📊 모니터링

### Actuator 엔드포인트

각 서비스에서 다음 엔드포인트를 통해 상태 확인 가능:

- Health Check: `/actuator/health`
- Info: `/actuator/info`
- Bus Refresh: `/actuator/busrefresh`

## 🔧 설정 관리

### 중앙화된 설정

- GitHub 저장소를 통한 설정 관리
- Spring Cloud Config Server를 통한 설정 중앙화
- Spring Cloud Bus를 통한 설정 동적 갱신

### 환경별 설정

- `application.yml`: 기본 설정
- `application-prod.yml`: 프로덕션 설정 (gitignore됨)

## 📝 개발 가이드

### 새로운 서비스 추가

1. 새로운 Spring Boot 프로젝트 생성
2. `build.gradle`에 필요한 의존성 추가
3. `application.yml`에 서비스명과 Config Server 설정 추가
4. API Gateway에 라우팅 설정 추가

### 설정 변경 시

설정 변경 후 다음 엔드포인트로 갱신:

```
POST http://localhost:8888/actuator/busrefresh
```

## 🤝 기여하기

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 라이선스

이 프로젝트는 MIT 라이선스 하에 배포됩니다.

## 👥 팀

- **개발자**: [Your Name]
- **프로젝트**: Order System MSA

---

**참고**: 이 프로젝트는 학습 목적으로 제작된 마이크로서비스 아키텍처 예제입니다.

